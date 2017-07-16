
package com.casic.simulation.planfile.manager;

import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.*;
import com.casic.simulation.permission.UserObj;
import com.casic.simulation.planfile.domain.PlanFile;
import com.casic.simulation.planfile.domain.PlanFileType;
import com.casic.simulation.planfile.dto.PlanFileJson;
import com.casic.simulation.planfile.web.FileController;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@Service
public class FileManager extends HibernateEntityDao<PlanFile> {

    @Resource
    private StoreConnector storeConnector;

    /** 文档预览缓存文件夹名称 */
    private String preViewFolder = "filepreview";

    /**
     * 分页获取预案记录
     * @param planName
     * @param planTypeCode
     * @param datestrat
     * @param dateend
     * @param page
     * @param rows
     * @return
     * @throws ParseException
     */
    public Map<String, Object> searchConditionalFile(
            String planName, String planTypeCode, String datestrat,
            String dateend, int page, int rows) throws ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from PlanFile where 1=1 ");
        if (StringUtils.isNotBlank(planName)) {
            hql.append(" and fileName like :fileName");
            resultMap.put("fileName", "%" + planName + "%");
        }
        if (StringUtils.isNotBlank(planTypeCode)
                && !FileController.DEFAULT_CODE_FOR_QUERY_ALL
                        .equals(planTypeCode)
                && PlanFileType.existCode(planTypeCode)) {
            hql.append(" and fileType like :fileType");
            resultMap.put("fileType", "%" + planTypeCode + "%");
        }
        if(StringUtils.isNotBlank(datestrat)) {
            hql.append(" and to_char(upDateTimes,'yyyy-mm-dd')>=:beg ");
            resultMap.put("beg", datestrat);
        }
        if(StringUtils.isNotBlank(dateend)) {
            hql.append(" and to_char(upDateTimes,'yyyy-mm-dd')<=:end ");
            resultMap.put("end", dateend);
        }
        hql.append(" order by id desc ");
        Page pg = pagedQuery(hql.toString(), page, rows, resultMap);
        List<PlanFile> fileInfoList = (List<PlanFile>) pg.getResult();
        List<PlanFileJson> fileInfoWrapperList =
                new ArrayList<PlanFileJson>();
        for (PlanFile fileInfo : fileInfoList){
            fileInfoWrapperList.add(new PlanFileJson(fileInfo));
        }
        resultMap.clear();
        resultMap.put("rows", fileInfoWrapperList);
        resultMap.put("total", pg.getTotalCount());
        return resultMap;
    }

    /**
     * 存储
     * @param json
     * @param curUser
     * @return
     */
    public ExecInfo savePlanFile(PlanFileJson json, UserObj curUser) {
        PlanFile file = new PlanFile();
        if (StringUtils.isBlank(json.getFileName())
                || StringUtils.isBlank(json.getFilePath())
                || json.getFileType() == null) {
            return ExecInfo.fail("请将信息填充完整");
        }
        if (curUser == null
                || StringUtils.isBlank(curUser.getUserName())) {
            return ExecInfo.fail("无法获取用户信息不能上传");
        }
        file.setFileName(json.getFileName());
        file.setFilePath(json.getFilePath());
        file.setFileType(json.getFileType().getCode());
        file.setFileDisplayName(json.getFileDisplayName());
        file.setUpDateTimes(new Date());
        file.setUpPerson(curUser.getUserName());
        save(file);
        return ExecInfo.succ("添加成功");
    }

    /**
     * 下载文件
     * @param id
     * @param folder
     * @param response
     * @throws Exception
     */
    public void downloadStrategyFile(
            Long id, String folder,
            HttpServletResponse response
    ) throws Exception {
        InputStream is = null;
        try {
            PlanFile fileInfo = get(id);
            response.setHeader("Content-Disposition",
                    "attachment;filename="
                    + new String(fileInfo.getFileDisplayName()
                            .getBytes("gb2312"), "ISO8859-1" ));
            String filePath = fileInfo.getFilePath();
            ExecInfo result = storeConnector.checkFile(folder, filePath);
            if (result.isSucc()) {
                is = storeConnector.get(folder, filePath)
                        .getResource().getInputStream();
                IoUtils.copyStream(is, response.getOutputStream());
            }
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }

    /**
     * 下载文件前检查文件是否存在
     * @param id
     * @param folder
     * @return
     */
    public ExecInfo checkFileForDownload(Long id, String folder) {
        PlanFile fileInfo = get(id);
        String filePath = fileInfo.getFilePath();
        return storeConnector.checkFile(folder, filePath);
    }

    /**
     * 根据ID进行在线浏览
     * @param id
     * @param folder
     * @return
     */
    public ExecResult<String> fileScanUrl(
            Long id, String folder,
            HttpServletRequest request
    ) {
        PlanFile fileInfo = get(id);
        try {
            String sourceFile = storeConnector.get(folder, fileInfo.getFilePath()).getResource().getFile().getAbsolutePath();
            ExecResult<String> result =  OfficeConverter.office2PDF(sourceFile, getDestFilePath(fileInfo, request));
            if (result.isSucc()) {
                String destFile = result.getValue();
                String returnDest = destFile.substring(destFile.lastIndexOf(preViewFolder));
                result = ExecResult.succ(returnDest);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ExecResult.fail(e.getMessage());
        }
    }

    /**
     * 获取存储缓存文件目录，一定要放在web资源目录下，
     * 这是能够网页预览的机制（需要获取访问权限）
     * @param fileInfo
     * @param request
     * @return
     */
    private String getDestFilePath(PlanFile fileInfo, HttpServletRequest request) {
        String filePath = fileInfo.getFilePath().substring(0, fileInfo.getFilePath().lastIndexOf(File.separator));
        return request.getSession().getServletContext().getRealPath("") +
                File.separator + "filepreview" + File.separator + filePath;
    }
}

