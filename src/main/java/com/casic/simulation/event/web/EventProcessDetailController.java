package com.casic.simulation.event.web;

import com.casic.simulation.core.ext.store.MultipartFileResource;
import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.ext.store.StoreDTO;
import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.event.domain.AlarmRecordTypeEnum;
import com.casic.simulation.event.dto.BackupDto;
import com.casic.simulation.event.manager.AlarmEventManager;
import com.casic.simulation.event.manager.AlarmRecordManager;
import com.casic.simulation.event.manager.PadEventManager;
import com.casic.simulation.flow.domain.EventProcessDetail;
import com.casic.simulation.flow.dto.EventProcessDetailDto;
import com.casic.simulation.flow.manager.EventProcessDetailManager;
import com.casic.simulation.permission.UserObj;
import org.hibernate.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/25.
 */
@Controller
@RequestMapping(value = "event")
public class EventProcessDetailController {
    @Resource
    private StoreConnector storeConnector;
    @Resource
    private PadEventManager padEventManager;
    @Resource
    private AlarmEventManager alarmEventManager;
    @Resource
    private AlarmRecordManager alarmRecordManager;
    @Resource
    private EventProcessDetailManager eventProcessDetailManager;

    //查询设备报警事件列表
    @RequestMapping("getAlarmRecordList")
    @ResponseBody
    public Map<String, Object> getAlarmRecordList(@RequestParam(value = "page", required = true) String page,
                                                  @RequestParam(value = "rows", required = true) String rows,
                                                  @RequestParam(value = "messageValue", required = false) String messageValue,//液位超限、噪声超标等
                                                  @RequestParam(value = "devCode", required = false) String devCode,
                                                  @RequestParam(value = "road", required = false) String road,
                                                  @RequestParam(value = "deviceTypeName", required = false) String typeName,
                                                  @RequestParam(value = "messageStatus", required = false) String messageStatus,
                                                  @RequestParam(value = "beginDate", required = false) String beginDate,
                                                  @RequestParam(value = "endDate", required = false) String endDate, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            UserObj curUser = (UserObj) session.getAttribute(
                    UserObj.SESSION_ATTRIBUTE_KEY
            );//获取用户
            Long userId = curUser == null ? null : curUser.getUserId();
            resultMap = alarmRecordManager.pageQueryAlarmRecord(Integer.parseInt(page), Integer.parseInt(rows), messageStatus, beginDate, endDate, AlarmRecordTypeEnum.getDevTypeByAlarType(messageValue), road, typeName, devCode, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping("getAlarmEventList")
    @ResponseBody
    public Map<String, Object> getAlarmEventList(@RequestParam(value = "page", required = true) String page,
                                                 @RequestParam(value = "rows", required = true) String rows,
                                                 @RequestParam(value = "messageStatus", required = false) String messageStatus,
                                                 @RequestParam(value = "beginDate", required = false) String beginDate,
                                                 @RequestParam(value = "endDate", required = false) String endDate, HttpSession session)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            UserObj curUser = (UserObj) session.getAttribute(
                    UserObj.SESSION_ATTRIBUTE_KEY
            );//获取用户
            Long userId = curUser == null ? null : curUser.getUserId();
            resultMap = alarmEventManager.pageQueryAlarmEvent(Integer.parseInt(page), Integer.parseInt(rows), messageStatus, beginDate, endDate, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    //查询人员上报事件列表
    @RequestMapping("getPadEventList")
    @ResponseBody
    public Map<String, Object> getPadEventList(@RequestParam(value = "page", required = true) String page,
                                               @RequestParam(value = "rows", required = true) String rows,
                                               @RequestParam(value = "messageStatus", required = false) String messageStatus,
                                               @RequestParam(value = "beginDate", required = false) String beginDate,
                                               @RequestParam(value = "endDate", required = false) String endDate, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            UserObj curUser = (UserObj) session.getAttribute(
                    UserObj.SESSION_ATTRIBUTE_KEY
            );//获取用户
            Long userId = curUser == null ? null : curUser.getUserId();
            resultMap = padEventManager.pageQueryPadEvent(Integer.parseInt(page), Integer.parseInt(rows), messageStatus, beginDate, endDate, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    //派发
    @RequestMapping("distribute")
    @ResponseBody
    public Map<String, Object> distribute(@RequestParam(value = "signUid") Long signUid,
                                          @RequestParam(value = "eventId") Long eventId,
                                          @RequestParam(value = "description") String desc,
                                          @RequestParam(value = "eventSrc") Integer eventSrc,
                                          HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        UserObj curUser = (UserObj) session.getAttribute(
                UserObj.SESSION_ATTRIBUTE_KEY
        );//获取用户
        Long userId = curUser == null ? null : curUser.getUserId();
        ExecInfo execInfo = eventProcessDetailManager.doDistribute(userId, signUid, eventId, desc, eventSrc);
        result.put("message", execInfo.getMsg());
        result.put("success", execInfo.isSucc());
        return result;
    }

    //备案
    @RequestMapping("backup")
    public void backup(@ModelAttribute BackupDto backupDto,
                       @RequestParam(value = "eventId") Long eventId,
                       @RequestParam(value = "eventSrc") Integer eventSrc,
                       @RequestParam(value = "uploadFile", required = false) MultipartFile file,
                       HttpSession session,
                       HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserObj curUser = (UserObj) session.getAttribute(
                UserObj.SESSION_ATTRIBUTE_KEY
        );//获取用户
        Long userId = curUser == null ? null : curUser.getUserId();
        if (file!=null)
        {
            if(StringUtils.isNotBlank(file.getOriginalFilename())){
                StoreDTO storeDto = storeConnector.save("eventfiles",
                        new MultipartFileResource(file),
                        file.getOriginalFilename());
                 backupDto.setBackupFile(storeDto.getKey());
                 backupDto.setBackupOriginalPath(file.getOriginalFilename());
            }
        }
        ExecInfo execInfo = eventProcessDetailManager.doBackup(userId, eventId, eventSrc, backupDto);
        result.put("message", execInfo.getMsg());
        result.put("success", execInfo.isSucc());
        JSONTool.writeDataResult(response, result);
    }

    @RequestMapping("getBackUpInfo")
    @ResponseBody
    public Map<String,Object> getBackUpInfo(@RequestParam(value = "eventId") Long eventId,
                                     @RequestParam(value = "eventSrc") Integer eventSrc){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("success",false);
        BackupDto backupDto = eventProcessDetailManager.getBackUpInfoByEventId(eventId,eventSrc);
       if(backupDto==null) return result;
        result.put("success",true);
        result.put("data",backupDto);
        return result;
    }

}
