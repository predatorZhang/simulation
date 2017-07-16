package com.casic.simulation.dangerSource.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "YJ_WXY_INFO")
@SequenceGenerator(name = "SEQ_YJ_WXY_INFO", sequenceName = "SEQ_YJ_WXY_INFO",allocationSize=1,initialValue=1)
public class DangerSource implements Serializable
{
	private static final long serialVersionUID = 1L;
    private Long dbId;
    private String sourceName;
    private String sourceGrade;
    private String latitude;
    private String longitude;
    private String description;
    private String errorMode;
    private String fileName;
    private String filePath;
    private Boolean active = true;
    private String memo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_YJ_WXY_INFO")
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    @Column(name = "SOURCE_NAME")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    @Column(name = "SOURCE_GRADE")
    public String getSourceGrade() {
        return sourceGrade;
    }

    public void setSourceGrade(String sourceGrade) {
        this.sourceGrade = sourceGrade;
    }

    @Column(name = "LATITUDE")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ERRORMODE")
    public String getErrorMode() {
        return errorMode;
    }

    public void setErrorMode(String errorMode) {
        this.errorMode = errorMode;
    }

    @Column(name = "FILENAME")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "FILEPATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "MEMO")
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

}
