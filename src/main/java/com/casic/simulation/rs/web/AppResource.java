package com.casic.simulation.rs.web;

import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.event.domain.AlarmEvent;
import com.casic.simulation.event.domain.AlarmRecord;
import com.casic.simulation.event.dto.AlarmEventDto;
import com.casic.simulation.event.dto.AlarmRecordDTO;
import com.casic.simulation.event.dto.PadEventDto;
import com.casic.simulation.event.dto.PadProcessDto;
import com.casic.simulation.flow.bean.EventSourceEnum;
import com.casic.simulation.flow.manager.EventProcessDetailManager;
import com.casic.simulation.flow.web.FlowConnector;
import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2015/1/15.
 */

@Controller
@RequestMapping("app")
public class AppResource
{

    @Resource
    private FlowConnector flowConnector;

    @Resource
    EventProcessDetailManager eventProcessDetailManager;
    //查询alarmrecord
    @GET
    @RequestMapping("getAlarmRecord")
    @ResponseBody
    public Map<String,Object> getAlarmRecord(
            @RequestParam(value = "id",required = false) String id,
            @RequestParam(value = "lastId",required = false) String lastId) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            List<AlarmRecordDTO> alarmRecordDTOs = eventProcessDetailManager.getAlarmRecordsForPad(Long.parseLong(id),lastId);
            resultMap.put("success", true);
            resultMap.put("message", alarmRecordDTOs);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //查询alarmrecord
    @GET
    @RequestMapping("getAlarmRecordNumber")
    @ResponseBody
    public Map<String,Object> getAlarmRecordNumber(
            @RequestParam(value = "id",required = false) String id) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            String number = eventProcessDetailManager.getNumberForPad(Long.parseLong(id),
                    EventSourceEnum.ALARM_RECORD.getEventSrc());
            resultMap.put("success", true);
            resultMap.put("message", number);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //查询getAlarmEvent
    @GET
    @RequestMapping("getAlarmEvent")
    @ResponseBody
    public Map<String,Object> getAlarmEvent(
            @RequestParam(value = "id",required = false) String id,
            @RequestParam(value = "lastId",required = false) String lastId) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            List<AlarmEventDto> alarmEventDTOs = eventProcessDetailManager.getAlarmEventsForPad(Long.parseLong(id),lastId);
            resultMap.put("success", true);
            resultMap.put("message", alarmEventDTOs);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //查询getAlarmEvent个数
    @GET
    @RequestMapping("getAlarmEventNumber")
    @ResponseBody
    public Map<String,Object> getAlarmEventNumber(
            @RequestParam(value = "id",required = false) String id) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            String number = eventProcessDetailManager.getNumberForPad(Long.parseLong(id),
                    EventSourceEnum.ALARM_EVENT.getEventSrc());
            resultMap.put("success", true);
            resultMap.put("message", number);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //查询getAlarmEvent
    @GET
    @RequestMapping("getPatrolEvent")
    @ResponseBody
    public Map<String,Object> getPatrolEvent(
            @RequestParam(value = "id",required = false) String id,
            @RequestParam(value = "lastId",required = false) String lastId) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            List<PadEventDto> padEventDtos = eventProcessDetailManager.getPadEventsForPad(Long.parseLong(id),lastId);
            resultMap.put("success", true);
            resultMap.put("message", padEventDtos);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //查询getAlarmEvent个数
    @GET
    @RequestMapping("getPatrolEventNumber")
    @ResponseBody
    public Map<String,Object> getPatrolEventNumber(
            @RequestParam(value = "id",required = false) String id) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (id==null||id.equals(""))
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员不存在！");
                return resultMap;
            }
            String number = eventProcessDetailManager.getNumberForPad(Long.parseLong(id),
                    EventSourceEnum.FEEDBACK.getEventSrc());
            resultMap.put("success", true);
            resultMap.put("message", number);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @GET
    @RequestMapping("doSignEvent")
    @ResponseBody
    public Map<String,Object> doSignEvent(
            @RequestParam(value = "userId",required = false) String userId,
            @RequestParam(value = "eventId",required = false) String eventId,
            @RequestParam(value = "eventSrc",required = false) String eventSrc) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            if (userId==null||eventId==null)
            {
                resultMap.put("success", false);
                resultMap.put("message", "人员和事件不能为空！");
                return resultMap;
            }
            ExecInfo execInfo = eventProcessDetailManager.doSign(Long.parseLong(userId),Long.parseLong(eventId),Integer.valueOf(eventSrc));
            if(execInfo.isSucc())
            {
                resultMap.put("message", "签收成功");
                resultMap.put("success", true);
            }
            else
            {
                resultMap.put("message", "签收失败,"+execInfo.getMsg());
                resultMap.put("success", false);
            }

        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", "签收失败,系统出现错误");
        }
        return resultMap;
    }

    @GET
    @RequestMapping("uploadAlarmRecord")
    @ResponseBody
    public Map<String, Object> uploadAlarmRecord(
            @RequestParam(value = "data",required = false) String data,
            @RequestParam(value = "fileBuffer1",required = false) MultipartFile fileBuffer1,
            @RequestParam(value = "fileBuffer2",required = false) MultipartFile fileBuffer2,
            @RequestParam(value = "fileBuffer3",required = false) MultipartFile fileBuffer3,
            @RequestParam(value = "fileBuffer4",required = false) MultipartFile fileBuffer4,
            @RequestParam(value = "description",required = false) String description) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            Gson gson = new Gson();
            AlarmRecordDTO alarmRecordDTO = gson.fromJson(data, AlarmRecordDTO.class);
            if (alarmRecordDTO==null||alarmRecordDTO.getId()==null)
                return null;
            String eventPicturePath = eventProcessDetailManager.saveFiles(fileBuffer1,fileBuffer2,fileBuffer3,fileBuffer4);
            PadProcessDto padProcessDto = new PadProcessDto();
            padProcessDto.setPadEventDescription(description);
            padProcessDto.setPadPicPath(eventPicturePath);
            padProcessDto.setPadProcessTime(new Date());
            ExecInfo execInfo = eventProcessDetailManager.doProcess(alarmRecordDTO.getId(),EventSourceEnum.ALARM_RECORD.getEventSrc(),padProcessDto);
            if(execInfo.isSucc())
            {
                resultMap.put("message", "上传成功");
                resultMap.put("success", true);
            }
            else
            {
                resultMap.put("message", "上传成功,"+execInfo.getMsg());
                resultMap.put("success", false);
            }
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", "上传失败,系统出现错误");
        }
        return resultMap;
    }

    @GET
    @RequestMapping("uploadAlarmEvent")
    @ResponseBody
    public Map<String,Object> uploadAlarmEvent(
            @RequestParam(value = "data",required = false) String data,
            @RequestParam(value = "fileBuffer1",required = false) MultipartFile fileBuffer1,
            @RequestParam(value = "fileBuffer2",required = false) MultipartFile fileBuffer2,
            @RequestParam(value = "fileBuffer3",required = false) MultipartFile fileBuffer3,
            @RequestParam(value = "fileBuffer4",required = false) MultipartFile fileBuffer4,
            @RequestParam(value = "description",required = false) String description) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            Gson gson = new Gson();
            AlarmEventDto alarmEventDto = gson.fromJson(data, AlarmEventDto.class);
            if (alarmEventDto==null||alarmEventDto.getId()==null)
                return null;
            String eventPicturePath = eventProcessDetailManager.saveFiles(fileBuffer1,fileBuffer2,fileBuffer3,fileBuffer4);
            PadProcessDto padProcessDto = new PadProcessDto();
            padProcessDto.setPadEventDescription(description);
            padProcessDto.setPadPicPath(eventPicturePath);
            padProcessDto.setPadProcessTime(new Date());
            eventProcessDetailManager.doProcess(alarmEventDto.getId(),EventSourceEnum.ALARM_EVENT.getEventSrc(),padProcessDto);
            resultMap.put("message", "上传成功");
            resultMap.put("success", true);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", "上传失败");
        }
        return resultMap;

    }

    @GET
    @RequestMapping("uploadPatrolEvent")
    @ResponseBody
    public Map<String,Object> uploadPatrolEvent(
            @RequestParam(value = "data",required = false) String data,
            @RequestParam(value = "fileBuffer1",required = false) MultipartFile fileBuffer1,
            @RequestParam(value = "fileBuffer2",required = false) MultipartFile fileBuffer2,
            @RequestParam(value = "fileBuffer3",required = false) MultipartFile fileBuffer3,
            @RequestParam(value = "fileBuffer4",required = false) MultipartFile fileBuffer4,
            @RequestParam(value = "description",required = false) String description) throws Exception{

        Map resultMap = new HashMap<String, Object>();
        try
        {
            Gson gson = new Gson();
            PadEventDto padEventDto = gson.fromJson(data, PadEventDto.class);
            if (padEventDto==null||padEventDto.getDbId()== null)
                return null;
            String eventPicturePath = eventProcessDetailManager.saveFiles(fileBuffer1,fileBuffer2,fileBuffer3,fileBuffer4);
            PadProcessDto padProcessDto = new PadProcessDto();
            padProcessDto.setPadEventDescription(description);
            padProcessDto.setPadPicPath(eventPicturePath);
            padProcessDto.setPadProcessTime(new Date());
            eventProcessDetailManager.doProcess(padEventDto.getDbId(),EventSourceEnum.FEEDBACK.getEventSrc(),padProcessDto);
            resultMap.put("message", "上传成功");
            resultMap.put("success", true);
        }
        catch (Exception e)
        {
            resultMap.put("success", false);
            resultMap.put("message", "上传失败");
        }
        return resultMap;

    }

    @GET
    @RequestMapping("getStartStatusForPad")
    @ResponseBody
    public Integer getStartStatusForPad() throws Exception{
        return flowConnector.getStartStatusForPad();
    }
}
