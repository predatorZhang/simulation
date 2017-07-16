package com.casic.simulation.planfile.web;

import com.casic.simulation.core.ext.store.MultipartFileResource;
import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.ext.store.StoreDTO;
import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.permission.UserObj;
import com.casic.simulation.planfile.domain.PlanFile;
import com.casic.simulation.planfile.domain.PlanFileType;
import com.casic.simulation.planfile.dto.PlanFileJson;
import com.casic.simulation.planfile.dto.PlanFileTypeJson;
import com.casic.simulation.planfile.manager.FileManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("fileController")
public class FileController {

    private String storeFolder = "simulation";

    public static final String DEFAULT_CODE_FOR_QUERY_ALL = "ALL";

    @Resource
    private FileManager fileManager;

    @Resource
    private StoreConnector storeConnector;

    /**
     * 文件列表展示
     */
    @RequestMapping("show-file")
    @ResponseBody
    public Map<String, Object> getFileList(
            @RequestParam(value = "planName", required = false) String planName,
            @RequestParam(value = "planType", required = false) String planType,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd,
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "rows", required = false) int rows
    ) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap = fileManager.searchConditionalFile(
                    planName, planType, dateStart,
                    dateEnd, page, rows
            );
        }catch(Exception e){
            e.printStackTrace();
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取上传文件种类列表
     * @return
     */
    @RequestMapping("queryPlanFileTypeJson")
    @ResponseBody
    @POST
    public List<PlanFileTypeJson> queryPlanFileTypeJson() {
        List<PlanFileTypeJson> jsons = new ArrayList<PlanFileTypeJson>();
        for (PlanFileType type : PlanFileType.values()) {
            if (type != PlanFileType.UNKNOWN) {
                jsons.add(new PlanFileTypeJson(type));
            }
        }
        return jsons;
    }

    @RequestMapping("queryPlanAllFileTypeJson")
    @ResponseBody
    @POST
    public List<PlanFileTypeJson> queryPlanAllFileTypeJson() {
        List<PlanFileTypeJson> jsons = new ArrayList<PlanFileTypeJson>();
        jsons.add(getHeadType());
        for (PlanFileType type : PlanFileType.values()) {
            if (type != PlanFileType.UNKNOWN) {
                jsons.add(new PlanFileTypeJson(type));
            }
        }
        return jsons;
    }

    private PlanFileTypeJson getHeadType() {
        PlanFileTypeJson json = new PlanFileTypeJson();
        json.setCode(DEFAULT_CODE_FOR_QUERY_ALL);
        json.setDesc("全部");
        json.setName("全部");
        json.setSelected(true);
        return json;
    }

    /**
     * 上传文档
     * @param model
     * @param file
     * @return
     */
    @RequestMapping("upload-file")
    @ResponseBody
    public void uploadFile(
            @ModelAttribute PlanFileJson model,
            @RequestParam(value = "uploadFile") MultipartFile file,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            UserObj curUser = (UserObj) session.getAttribute(
                    UserObj.SESSION_ATTRIBUTE_KEY
            );
            if (file != null&&StringUtils.isNotBlank(file.getOriginalFilename())) {
                StoreDTO storeDto = storeConnector.save(
                        storeFolder,
                        new MultipartFileResource(file),
                        file.getOriginalFilename()
                );
                model.setFilePath(storeDto.getKey());
                model.setFileDisplayName(file.getOriginalFilename());
                map.put("msg",
                        fileManager.savePlanFile(model, curUser).getMsg());
            } else {
                map.put("msg", "请选择文件重新上传");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }
        JSONTool.writeDataResult(response, map);
    }

    /**
     * 文件下载
     * @param id
     * @param response
     */
    @RequestMapping("file-download")
    public void downloadFile(
            @RequestParam(value = "dbId",required = true)Long id,
            HttpServletResponse response
    ) {
        try {
            fileManager.downloadStrategyFile(id, storeFolder, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件是否存在检查
     * @param id
     * @return
     */
    @RequestMapping("file-check")
    @ResponseBody
    public Map<String,Object> checkFile(
            @RequestParam(value = "dbId",required = true)Long id) {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            ExecInfo result = fileManager.checkFileForDownload(id, storeFolder);
            map.put("success", result.isSucc());
            map.put("msg", result.getMsg());
        } catch(Exception e){
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 文件删除
     * @param id
     * @return
     */
    @RequestMapping("file-delete")
    @ResponseBody
    public Map<String,Object> deleteFile(
            @RequestParam(value = "dbId",required = true)Long id) {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            fileManager.removeById(id);
            map.put("success", true);
        } catch(Exception e){
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 文件预览
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("file-scan")
    @ResponseBody
    public Map<String,Object> fileScan(
            @RequestParam(value = "dbId",required = true)Long id,
            HttpServletRequest request
    ) {
        Map<String,Object> map = new HashMap<String, Object>();
        ExecResult<String> result = fileManager.fileScanUrl(id, storeFolder, request);
        map.put("success", result.isSucc());
        if (result.isSucc()) {
            map.put("filePath", result.getValue());
        } else {
            map.put("msg", result.getMsg());
        }
        return map;
    }
}
