package com.casic.simulation.flow.dto;

import java.util.Date;

/**
 * Created by lenovo on 2017/4/24.
 */
public class EventProcessDetailDto {
    private Long id;
    private Long  eventId;
    private Integer eventSrc;
    private Long processId;
    private Date confirmTime;
    private Long confirmUid;
    private String padPicPath;
    private String padEventDescription;
    private Date padSignTime;
    private Long padSignUid;
    private Date padProcessTime;
    private Date backupTime;
    private Date backupUpdateTime;
    private Long backupUid;
    private String backupFile;
    private String backupOriginalPath;
    private String backupReason;
    private String backupInfo;
    private String backupMeasure;
    private int message;
    private int messageStatus;
    private Long distributeUid;
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
