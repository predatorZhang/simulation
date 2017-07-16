package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/24.
 */
@Entity
@Table(name = "SM_EVENT_PROCESS_DETAIL")
@SequenceGenerator(name = "SEQ_EVENT_PROCESS_ID", sequenceName = "SEQ_EVENT_PROCESS_ID", allocationSize=1,
        initialValue=1)
public class EventProcessDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EVENT_PROCESS_ID")
    @Column(name = "DBID")
    private Long id;
    @Column(name = "event_id")
    private Long  eventId;
    @Column(name = "event_src")
    private Integer eventSrc;
    @Column(name = "process_id")
    private Long processId;
    @Column(name = "confirm_time")
    private Date confirmTime;
    @Column(name = "confirm_uid")
    private Long confirmUid;
    @Column(name = "pad_pic_path")
    private String padPicPath;
    @Column(name = "pad_event_description")
    private String padEventDescription;
    @Column(name = "pad_sign_time")
    private Date padSignTime;//pad端签收时间
    @Column(name = "pad_sign_uid")
    private Long padSignUid;
    @Column(name = "pad_process_time")
    private Date padProcessTime;
    @Column(name = "backup_time")
    private Date backupTime;
    @Column(name = "backup_update_time")
    private Date backupUpdateTime;
    @Column(name = "backup_uid")
    private Long backupUid;
    @Column(name="backup_original_path")
    private String backupOriginalPath;
    @Column(name = "backup_file")
    private String backupFile;
    @Column(name = "backup_reason")
   private String backupReason;//备案事件原因
    @Column(name = "backup_info")
    private String backupInfo;//备案信息
    @Column(name = "backup_measure")
    private String backupMeasure;//采取措施
    @Column(name = "message")
    private int message;//暂无用
    @Column(name = "message_status")
    private int messageStatus;

    //派发信息记录
    @Column(name = "distribute_uid")
    private Long distributeUid;
    @Column(name = "distribute_res")
    private String distributeResult;//派发分析结果


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getEventSrc() {
        return eventSrc;
    }

    public void setEventSrc(Integer eventSrc) {
        this.eventSrc = eventSrc;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Long getConfirmUid() {
        return confirmUid;
    }

    public void setConfirmUid(Long confirmUid) {
        this.confirmUid = confirmUid;
    }

    public String getPadPicPath() {
        return padPicPath;
    }

    public void setPadPicPath(String padPicPath) {
        this.padPicPath = padPicPath;
    }

    public String getPadEventDescription() {
        return padEventDescription;
    }

    public void setPadEventDescription(String padEventDescription) {
        this.padEventDescription = padEventDescription;
    }

    public Date getPadSignTime() {
        return padSignTime;
    }

    public void setPadSignTime(Date padSignTime) {
        this.padSignTime = padSignTime;
    }

    public Long getPadSignUid() {
        return padSignUid;
    }

    public void setPadSignUid(Long padSignUid) {
        this.padSignUid = padSignUid;
    }

    public Date getPadProcessTime() {
        return padProcessTime;
    }

    public void setPadProcessTime(Date padProcessTime) {
        this.padProcessTime = padProcessTime;
    }

    public Date getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
    }

    public Date getBackupUpdateTime() {
        return backupUpdateTime;
    }

    public void setBackupUpdateTime(Date backupUpdateTime) {
        this.backupUpdateTime = backupUpdateTime;
    }

    public Long getBackupUid() {
        return backupUid;
    }

    public void setBackupUid(Long backupUid) {
        this.backupUid = backupUid;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }

    public String getBackupReason() {
        return backupReason;
    }

    public void setBackupReason(String backupReason) {
        this.backupReason = backupReason;
    }

    public String getBackupInfo() {
        return backupInfo;
    }

    public void setBackupInfo(String backupInfo) {
        this.backupInfo = backupInfo;
    }

    public String getBackupMeasure() {
        return backupMeasure;
    }

    public void setBackupMeasure(String backupMeasure) {
        this.backupMeasure = backupMeasure;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Long getDistributeUid() {
        return distributeUid;
    }

    public void setDistributeUid(Long distributeUid) {
        this.distributeUid = distributeUid;
    }

    public String getDistributeResult() {
        return distributeResult;
    }

    public void setDistributeResult(String distributeResult) {
        this.distributeResult = distributeResult;
    }

    public String getBackupOriginalPath() {
        return backupOriginalPath;
    }

    public void setBackupOriginalPath(String backupOriginalPath) {
        this.backupOriginalPath = backupOriginalPath;
    }
}
