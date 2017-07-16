package com.casic.simulation.event.dto;

import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
public class BackupDto {
    private Date backupTime;
    private Date backupUpdateTime;
    private Long backupUid;
    private String backupFile;
    private String backupOriginalPath;
    private String backupReason;
    private String backupInfo;
    private String backupMeasure;

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

    public String getBackupOriginalPath() {
        return backupOriginalPath;
    }

    public void setBackupOriginalPath(String backupOriginalPath) {
        this.backupOriginalPath = backupOriginalPath;
    }
}
