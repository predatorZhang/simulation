package com.casic.simulation.dangerSource.dto;

import com.casic.simulation.dangerSource.domain.DangerSource;
import com.casic.simulation.dangerSource.domain.DangerSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DangerSourceDTO implements Serializable
{
    private Long dbId;
    private String sourceName;
//    private String sourceNameLink;
    private String sourceGrade;
//    private String sourceGradeLabel;
    private String latitude;
    private String longitude;
    private String description;
    private String errorMode;
    private String fileName;
    private String filePath;
    private Boolean active = true;
    private String memo;
    private String detailBtn = "<a href='#' iconCls=\"icon-search\">查看</a>";//更换为图片即可
    private String errorModeBtn = "<a href='#' iconCls=\"icon-search\">查看</a>";
    private String strategyBtn = "<a href='#' iconCls=\"icon-search\">下载</a>";
    private String editBtn = "<a href='javascript:;' iconCls=\"icon-search\">编辑</a>";
    private String deleteBtn = "<a href='javascript:;' iconCls=\"icon-search\" >删除</a>";

    public DangerSourceDTO() {
    }

    public DangerSourceDTO(Long id, String para, String type) {
        this.setDbId(id);
    }

    public DangerSourceDTO(DangerSource dangerSource) {
        this.setDbId(dangerSource.getDbId());
        this.setSourceName(dangerSource.getSourceName());
        this.setSourceGrade(dangerSource.getSourceGrade());
        this.setLatitude(dangerSource.getLatitude());
        this.setLongitude(dangerSource.getLongitude());
        this.setDescription(doNullChange(dangerSource.getDescription()));
        this.setErrorMode(dangerSource.getErrorMode());
        this.setFileName(dangerSource.getFileName());
        this.setFilePath(dangerSource.getFilePath());
        this.setActive(dangerSource.getActive());
        this.setMemo(dangerSource.getMemo());
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceGrade() {
        return sourceGrade;
    }

    public void setSourceGrade(String sourceGrade) {
        this.sourceGrade = sourceGrade;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorMode() {
        return errorMode;
    }

    public void setErrorMode(String errorMode) {
        this.errorMode = errorMode;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getErrorModeBtn() {
        return errorModeBtn;
    }

    public void setErrorModeBtn(String errorModeBtn) {
        this.errorModeBtn = errorModeBtn;
    }

    public String getDetailBtn() {
        return detailBtn;
    }

    public void setDetailBtn(String detailBtn) {
        this.detailBtn = detailBtn;
    }

    public String getStrategyBtn() {
        return strategyBtn;
    }

    public void setStrategyBtn(String strategyBtn) {
        this.strategyBtn = strategyBtn;
    }

    public String getEditBtn() {
        return editBtn;
    }

    public void setEditBtn(String editBtn) {
        this.editBtn = editBtn;
    }

    public String getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(String deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public static List<DangerSourceDTO> ConvertDTOs(List<DangerSource> dangerSources)
    {
        List<DangerSourceDTO> dangerSourceDTOs = new ArrayList<DangerSourceDTO>();
        for (DangerSource dangerSource : dangerSources)
        {
            dangerSourceDTOs.add(new DangerSourceDTO(dangerSource));
        }
        return dangerSourceDTOs;
    }
    public static String doNullChange(Object obj){
        return obj==null?"--":String.valueOf(obj);
    }
}
