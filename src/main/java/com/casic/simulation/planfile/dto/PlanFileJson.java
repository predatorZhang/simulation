package com.casic.simulation.planfile.dto;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.planfile.domain.PlanFile;
import com.casic.simulation.planfile.domain.PlanFileType;

import java.text.SimpleDateFormat;

/**
 * Created by lenovo on 2017/4/1.
 */
public class PlanFileJson {
    private long id;
    private String fileName;
    private String filePath;
    private PlanFileType fileType;
    private String fileTypeName;
    private String fileDisplayName;
    private String upDateTimes;
    private String upPerson;

    public PlanFileJson(){}

    public PlanFileJson(PlanFile file) {
        this.id = file.getDbid();
        this.fileName = file.getFileName();
        this.filePath = file.getFilePath();
        this.fileType = PlanFileType.get(file.getFileType());
        this.fileTypeName = fileType.getName();
        this.upDateTimes = DateUtils.sdf4.format(file.getUpDateTimes());
        this.upPerson = file.getUpPerson();
        this.fileDisplayName = file.getFileDisplayName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PlanFileType getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = PlanFileType.get(fileType);
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getUpDateTimes() {
        return upDateTimes;
    }

    public void setUpDateTimes(String upDatetimes) {
        this.upDateTimes = upDatetimes;
    }

    public String getUpPerson() {
        return upPerson;
    }

    public void setUpPerson(String upPerson) {
        this.upPerson = upPerson;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }
}
