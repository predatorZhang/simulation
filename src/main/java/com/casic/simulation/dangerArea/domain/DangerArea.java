package com.casic.simulation.dangerArea.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "YJ_WXQU_INFO")
@SequenceGenerator(name = "SEQ_YJ_WXQU_INFO", sequenceName = "SEQ_YJ_WXQU_INFO",allocationSize=1,initialValue=1)
public class DangerArea implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long dbId;
	private String areaName;
    private String areaGrade;
    private String location;
    private String description;
    private String fileName;
    private String filePath;
    private Boolean active = true;
    private String memo;

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_YJ_WXQU_INFO")
    @Column(name = "DBID")

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Column(name = "AREA_NAME")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    @Column(name = "AREA_GRADE")
    public String getAreaGrade() {
        return areaGrade;
    }

    public void setAreaGrade(String areaGrade) {
        this.areaGrade = areaGrade;
    }
    @Column(name = "AREA_LOCATION")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
