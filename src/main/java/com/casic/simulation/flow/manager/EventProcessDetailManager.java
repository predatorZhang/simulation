package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.event.domain.AlarmEvent;
import com.casic.simulation.event.domain.AlarmRecord;
import com.casic.simulation.event.domain.PadEvent;
import com.casic.simulation.event.dto.*;
import com.casic.simulation.event.manager.AlarmEventManager;
import com.casic.simulation.event.manager.AlarmRecordManager;
import com.casic.simulation.event.manager.PadEventManager;
import com.casic.simulation.flow.bean.EventSourceEnum;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.domain.EventProcessDetail;
import com.casic.simulation.flow.dto.EventProcessDetailDto;
import com.casic.simulation.flow.web.FlowConnector;
import com.casic.simulation.util.MessageStatusEnum;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/4/24.
 *
 */
@Service
public class EventProcessDetailManager  extends HibernateEntityDao<EventProcessDetail> {

    private final int MAX_ITEMS = 5;

    private List<Integer> padActionStatus = Arrays.asList(
            MessageStatusEnum.SIGN.getIndex(),
            MessageStatusEnum.HANDLE.getIndex()
    );

    @Resource
    private AlarmRecordManager alarmRecordManager;

    @Resource
    private AlarmEventManager alarmEventManager;

    @Resource
    private PadEventManager padEventManager;

    @Resource
    private FlowConnector flowConnector;

    /**
     * 对事件做派发处理
     * @param uid
     * @param signuid
     * @param eventId
     * @param description
     * @param eventSrc
     * @return ExecInfo
     */
    public ExecInfo doDistribute(
            Long uid, Long signuid, Long eventId,
            String description, Integer eventSrc) {
        Integer status = getNextStatus(eventId, eventSrc,
                OperationEnum.DISTRIBUTE.getOperationCode());
        if (status == null) {
            return ExecInfo.fail("无法获取正确下一步状态");
        }
        if (!changeEventStauts(status, eventId, eventSrc)) {
            return ExecInfo.fail("状态无法同步到事件表中");
        }
        EventProcessDetail eventProcessDetail =
                this.getEventDetailByEventId(eventId, eventSrc);
        eventProcessDetail.setDistributeResult(description);
        eventProcessDetail.setDistributeUid(uid);
        eventProcessDetail.setPadSignUid(signuid);
        eventProcessDetail.setMessageStatus(status);
        this.save(eventProcessDetail);
        return ExecInfo.succ();
    }

    /**
     * 对事件做签收处理
     * @param uid
     * @param eventId
     * @param eventSrc
     * @return ExecInfo
     */
    public ExecInfo doSign(Long uid, Long eventId, Integer eventSrc) {
        Integer status = getNextStatus(eventId, eventSrc,
                OperationEnum.SIGN.getOperationCode());
        if (status == null) {
            return ExecInfo.fail("无法获取正确下一步状态");
        }
        if (!changeEventStauts(status, eventId, eventSrc)) {
            return ExecInfo.fail("状态无法同步到事件表中");
        }
        EventProcessDetail eventProcessDetail =
                this.getEventDetailByEventId(eventId, eventSrc);
        if (eventProcessDetail.getId() == null) {
            return ExecInfo.fail("没有进行派遣无法签收");
        }
        eventProcessDetail.setPadSignTime(new Date());
        eventProcessDetail.setPadSignUid(uid);
        eventProcessDetail.setMessageStatus(status);
        this.save(eventProcessDetail);
        return ExecInfo.succ();
    }

    /**
     * 对事件做处理操作
     * @param eventId
     * @param eventSrc
     * @param padProcessDto
     * @return ExecInfo
     */
    public ExecInfo doProcess(Long eventId, Integer eventSrc,
                              PadProcessDto padProcessDto) {
        Integer status = getNextStatus(eventId, eventSrc,
                OperationEnum.HANDLE.getOperationCode());
        if (status == null) {
            return ExecInfo.fail("无法获取正确下一步状态");
        }
        if (!changeEventStauts(status, eventId, eventSrc)) {
            return ExecInfo.fail("状态无法同步到事件表中");
        }
        EventProcessDetail eventProcessDetail =
                this.getEventDetailByEventId(eventId, eventSrc);
        if (eventProcessDetail.getId() == null) {
            return ExecInfo.fail("没有进行派遣无法处理");
        }
        eventProcessDetail.setPadEventDescription(padProcessDto.getPadEventDescription());
        eventProcessDetail.setPadPicPath(padProcessDto.getPadPicPath());
        eventProcessDetail.setPadProcessTime(padProcessDto.getPadProcessTime());
        eventProcessDetail.setMessageStatus(status);
        this.save(eventProcessDetail);
        return ExecInfo.succ();
    }

    /**
     * 对事件做备案操作或修改备案
     * @param uid
     * @param eventId
     * @param eventSrc
     * @param backupDto
     * @return ExecInfo
     */
    public ExecInfo doBackup(Long uid, Long eventId,
                             Integer eventSrc, BackupDto backupDto) {
        EventProcessDetail eventProcessDetail =
                this.getEventDetailByEventId(eventId, eventSrc);
        if (eventProcessDetail.getId() == null) {
            return ExecInfo.fail("没有进行派遣无法备案");
        }
        Integer status = null;
        if (eventProcessDetail.getBackupTime() == null) {//备案操作
            eventProcessDetail.setBackupTime(new Date());
            eventProcessDetail.setBackupUid(uid);
            status = getNextStatus(eventId, eventSrc,
                    OperationEnum.RECORD.getOperationCode());
        } else {//修改备案
            eventProcessDetail.setBackupUpdateTime(new Date());
            eventProcessDetail.setBackupUid(uid);
            status = getNextStatus(eventId, eventSrc,
                    OperationEnum.MODIFY.getOperationCode());
        }
        if (status == null) {
            return ExecInfo.fail("无法获取正确下一步状态");
        }
        if (!changeEventStauts(status, eventId, eventSrc)) {
            return ExecInfo.fail("状态无法同步到事件表中");
        }
        eventProcessDetail.setBackupFile(backupDto.getBackupFile());
        eventProcessDetail.setBackupOriginalPath(backupDto.getBackupOriginalPath());
        eventProcessDetail.setBackupInfo(backupDto.getBackupInfo());
        eventProcessDetail.setBackupMeasure(backupDto.getBackupMeasure());
        eventProcessDetail.setBackupReason(backupDto.getBackupReason());
        eventProcessDetail.setMessageStatus(status);
        this.save(eventProcessDetail);
        return ExecInfo.succ();
    }

    /**
     * 根据事件id获取事件详情实体类
     *
     * @param eventId
     * @param eventSrc
     * @return
     */
    public EventProcessDetail getEventDetailByEventId(
            Long eventId, Integer eventSrc) {
        Criteria criteria = this.createCriteria(EventProcessDetail.class);
        criteria.add(Restrictions.eq("eventId", eventId))
                .add(Restrictions.eq("eventSrc", eventSrc));
        List<EventProcessDetail> list = criteria.list();
        EventProcessDetail eventProcessDetail = null;
        if (list.size() > 0) {
            eventProcessDetail = list.get(0);
        } else {
            eventProcessDetail = new EventProcessDetail();
            eventProcessDetail.setEventId(eventId);
            eventProcessDetail.setEventSrc(eventSrc);
        }
        return eventProcessDetail;
    }
    //获取备案信息
    public BackupDto getBackUpInfoByEventId(
            Long eventId,Integer eventSrc){
        EventProcessDetail eventProcessDetail = getEventDetailByEventId(eventId,eventSrc);
        if(eventProcessDetail==null) return null;
        BackupDto backupDto = new BackupDto();
        backupDto.setBackupOriginalPath(eventProcessDetail.getBackupOriginalPath());
        backupDto.setBackupInfo(eventProcessDetail.getBackupInfo());
        backupDto.setBackupMeasure(eventProcessDetail.getBackupMeasure());
        backupDto.setBackupReason(eventProcessDetail.getBackupReason());
        return backupDto;
    }

    /**
     * 获取操作过后下一步的status
     * @param eventId
     * @param eventSrc
     * @param operationCode
     * @return
     */
    private Integer getNextStatus(Long eventId, Integer eventSrc,
                                  String operationCode) {
        return flowConnector.getNextStatus(
                EventSourceEnum.getByEventSrc(eventSrc),
                eventId,
                operationCode
        );
    }

    private boolean changeEventStauts(Integer status, Long eventID,
                                      Integer source) {
        if (source == EventSourceEnum.ALARM_EVENT.getEventSrc()) {
            AlarmEvent event = alarmEventManager.get(eventID);
            event.setMessageStatus(status);
            alarmEventManager.save(event);
            return true;
        } else if (source == EventSourceEnum.ALARM_RECORD.getEventSrc()) {
            AlarmRecord record = alarmRecordManager.get(eventID);
            record.setMessageStatus(status);
            alarmRecordManager.save(record);
            return true;
        } else if (source == EventSourceEnum.FEEDBACK.getEventSrc()) {
            PadEvent event = padEventManager.get(eventID);
            event.setStatus(status);
            padEventManager.save(event);
            return true;
        }
        return false;
    }

    /**
     * pad端获取设备报警记录
     * @param userID
     * @param lastId
     * @return
     */
    public List<AlarmRecordDTO> getAlarmRecordsForPad(
            Long userID, String lastId) {
        List<EventProcessDetail> details = getForPad(
                userID, lastId,
                EventSourceEnum.ALARM_RECORD.getEventSrc()
        );
        List<AlarmRecordDTO> recordDTOs = new ArrayList<AlarmRecordDTO>();
        for (EventProcessDetail detail : details) {
            AlarmRecord record = alarmRecordManager.get(detail.getEventId());
            AlarmRecordDTO recordDTO = AlarmRecordDTO.ConvertToDTO(record);
            List<OperationEnum> operations =
                    flowConnector.getOperations(
                            EventSourceEnum.ALARM_RECORD, recordDTO.getId());
            if (operations.size() > 0)
                recordDTO.setOperate(operations.get(0).getOperationCode());
            recordDTOs.add(recordDTO);
        }
        return recordDTOs;
    }

    /**
     * pad端获取alarm event
     * @param userID
     * @param lastId
     * @return
     */
    public List<AlarmEventDto> getAlarmEventsForPad(
            Long userID, String lastId) {
        List<EventProcessDetail> details = getForPad(
                userID, lastId,
                EventSourceEnum.ALARM_EVENT.getEventSrc()
        );
        List<AlarmEventDto> eventDTOs = new ArrayList<AlarmEventDto>();
        for (EventProcessDetail detail : details) {
            AlarmEvent event = alarmEventManager.get(detail.getEventId());
            AlarmEventDto eventDTO = AlarmEventDto.ConvertToDTO(event);
            List<OperationEnum> operations =
                    flowConnector.getOperations(
                            EventSourceEnum.ALARM_EVENT, eventDTO.getId());
            if (operations.size() > 0)
                eventDTO.setOperate(operations.get(0).getOperationCode());
            eventDTOs.add(eventDTO);
        }
        return eventDTOs;
    }

    /**
     * pad端获取pad event
     * @param userID
     * @param lastId
     * @return
     */
    public List<PadEventDto> getPadEventsForPad(
            Long userID, String lastId) {
        List<EventProcessDetail> details = getForPad(
                userID, lastId,
                EventSourceEnum.FEEDBACK.getEventSrc()
        );
        List<PadEventDto> eventDTOs = new ArrayList<PadEventDto>();
        for (EventProcessDetail detail : details) {
            PadEvent event = padEventManager.get(detail.getEventId());
            PadEventDto eventDTO = new PadEventDto(event);
            List<OperationEnum> operations =
                    flowConnector.getOperations(
                            EventSourceEnum.FEEDBACK, eventDTO.getDbId());
            if (operations.size() > 0)
                eventDTO.setOperate(operations.get(0).getOperationCode());
            eventDTOs.add(eventDTO);
        }
        return eventDTOs;
    }

    /**
     * PAD端获取event统一查询语句
     * @param userID
     * @param lastId
     * @param eventSrc
     * @return
     */
    private List<EventProcessDetail> getForPad(
            Long userID, String lastId, Integer eventSrc) {
        Criteria criteria = getSession().
                createCriteria(EventProcessDetail.class);
        criteria.add(Restrictions.eq("padSignUid", userID));
        criteria.add(Restrictions.eq("eventSrc", eventSrc));
        criteria.add(Restrictions.in("messageStatus", padActionStatus));
        criteria.add(Restrictions.lt("eventId", Long.parseLong(lastId)));
        criteria.addOrder(Order.desc("eventId"));
        criteria.setMaxResults(MAX_ITEMS);
        return criteria.list();
    }

    /**
     * PAD端获取event个数统一查询语句
     * @param userID
     * @param eventSrc
     * @return
     */
    public String getNumberForPad(
            Long userID, Integer eventSrc) {
        Criteria criteria = getSession().
                createCriteria(EventProcessDetail.class);
        criteria.add(Restrictions.eq("padSignUid", userID));
        criteria.add(Restrictions.eq("eventSrc", eventSrc));
        criteria.add(Restrictions.in("messageStatus", padActionStatus));
        if(criteria.list()!=null)
        {
            return criteria.list().size()+"";
        }
        return "0";
    }

    public String saveFiles(MultipartFile file1,MultipartFile file2,MultipartFile file3,MultipartFile file4) {

       try
        {
            String [] fileNames = new String[4];
            String [] fileBuffers = new String[4];
            if (file1!=null)
            {
                fileNames[0]=file1.getOriginalFilename();
                byte[] fis = file1.getBytes();
                fileBuffers[0] = new String(Base64.encode(fis, Base64.BASE64DEFAULTLENGTH));
            }
            if (file2!=null)
            {
                fileNames[1]=file2.getOriginalFilename();
                byte[] fis = file2.getBytes();
                fileBuffers[1] = new String(Base64.encode(fis,Base64.BASE64DEFAULTLENGTH));
            }
            if (file3!=null)
            {
                fileNames[2]=file3.getOriginalFilename();
                byte[] fis = file3.getBytes();
                fileBuffers[2] = new String(Base64.encode(fis,Base64.BASE64DEFAULTLENGTH));
            }
            if (file4!=null)
            {
                fileNames[3]=file4.getOriginalFilename();
                byte[] fis = file4.getBytes();
                fileBuffers[3] = new String(Base64.encode(fis,Base64.BASE64DEFAULTLENGTH));
            }

            HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String strDirPath = request.getSession().getServletContext().getRealPath("");
            FileOutputStream fos = null;
            String toDir = strDirPath+"/eventImage";   //存储路径
            String fileName = "";
            for (int i = 0;i < fileBuffers.length;i++)
            {
                if (fileBuffers[i] != null&&fileNames[i]!=null)
                {
                    fileName += fileNames[i]+",";
                    byte[] buffer = new BASE64Decoder().decodeBuffer(fileBuffers[i]);   //对android传过来的图片字符串进行解码
                    File destDir = new File(toDir);
                    if(!destDir.exists())
                        destDir.mkdirs();
                    File imageFile = new File(destDir,fileNames[i]);
                    fos = new FileOutputStream(imageFile);   //保存图片
                    fos.write(buffer);
                    fos.flush();
                    fos.close();
                }
            }
            return fileName;
        }
        catch (Exception e)
        {

        }
        return "";
    }
}
