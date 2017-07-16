package com.casic.simulation.dangerArea.dto;

import com.casic.simulation.dangerArea.domain.DangerArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DangerAreaDTO implements Serializable
{
    private Long dbId;
    private String areaName;
//    private String areaNameLink;
    private String areaGrade;
//    private String areaGradeLabel;
    private String location;
    private String description;
    private String fileName;
    private String filePath;
    private Boolean active = true;
    private String memo;
    private String detailBtn = "<a href='#' class='btn mini purple'>查看</a>";
    private String strategyBtn = "<a href='#' class='btn mini green'>下载</a>";
    private String editBtn = "<a class='edit' iconCls='icon-edit' href='javascript:;'>编辑</a>";
    private String deleteBtn = "<a class='delete' iconCls='icon-delete'  href='javascript:;'>删除</a>";

    public DangerAreaDTO() {
    }

    public DangerAreaDTO(Long id, String para, String type) {
        this.setDbId(id);
    }

    public DangerAreaDTO(DangerArea dangerArea) {
        this.setDbId(dangerArea.getDbId());
        this.setAreaName(dangerArea.getAreaName());
        this.setAreaGrade(dangerArea.getAreaGrade());
        this.setLocation(dangerArea.getLocation());
        this.setDescription(dangerArea.getDescription());
        //TODO LIST:predator 涉及文件上传的东西都有自己的domain设计规则，明天统一解决
        this.setFileName(dangerArea.getFileName());
        this.setFilePath(dangerArea.getFilePath());
        this.setActive(dangerArea.getActive());
        this.setMemo(dangerArea.getMemo());
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaGrade() {
        return areaGrade;
    }

    public void setAreaGrade(String areaGrade) {
        this.areaGrade = areaGrade;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static List<DangerAreaDTO> ConvertDTOs(List<DangerArea> dangerAreas)
    {
        List<DangerAreaDTO> dangerAreaDTOs = new ArrayList<DangerAreaDTO>();
        for (DangerArea dangerArea : dangerAreas)
        {
            dangerAreaDTOs.add(new DangerAreaDTO(dangerArea));
        }
        return dangerAreaDTOs;
    }
}
