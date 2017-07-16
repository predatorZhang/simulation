package com.casic.simulation.health.dto;

import com.casic.simulation.health.domain.HealthInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HealthInfoDTO
{
    private Long dbId;
    private String pipeId;
    private String pipeIdLink;
    private String pipeType;
    private String street;
    private String healthRankLabel;
    private String healthRank;
    private String detectTime;
    private String result;
    private String evalRecordId;
    private String positionBtn = "<a href='#' class='btn mini green'>查看</a>";
    private String suggestion;
    private String suggestionBtn = "<a href='#' class='btn mini blue'>查看</a>";
    private String memo;

    public HealthInfoDTO() {
    }
    public HealthInfoDTO(HealthInfo healthInfo)
    {
        this.setDbId(healthInfo.getDbId());
        this.setPipeId(healthInfo.getPipeId());
        this.setPipeIdLink("<a href='#' class='link'>" + healthInfo.getPipeId() + "</a>");
        this.setPipeType(healthInfo.getPipeType());
        this.setStreet(healthInfo.getStreet());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.setDetectTime(simpleDateFormat.format(healthInfo.getEvalTime()));
        this.setEvalRecordId(healthInfo.getEvalRecordId());
        this.setResult(healthInfo.getResult());
        this.setSuggestion(healthInfo.getSuggestion()==null?"无":healthInfo.getSuggestion());
        this.setMemo(healthInfo.getMemo());
        String rank = healthInfo.getHealthRank();
        this.setHealthRank(rank);
        if (rank.equalsIgnoreCase("健康"))
        {
            this.setHealthRankLabel("<span class='label label-success'>健康</span>");
        }
        else if (rank.equalsIgnoreCase("较健康")) {
            this.setHealthRankLabel("<span class='label label-success'>较健康</span>");
        }
        else if (rank.equalsIgnoreCase("亚健康"))
        {
            this.setHealthRankLabel("<span class='label label-warning'>亚健康</span>");
        }
        else if (rank.equalsIgnoreCase("疾病"))
        {
            this.setHealthRankLabel("<span class='label label-important'>疾病</span>");
        }
        else if (rank.equalsIgnoreCase("严重疾病"))
        {
            this.setHealthRankLabel("<span class='label label-important'>严重疾病</span>");
        }
        else
            this.setHealthRankLabel("<span class='label label-important'>error</span>");
    }

    public static List<HealthInfoDTO> ConvertDTOs(List<HealthInfo> healthInfos)
    {
        List<HealthInfoDTO> healthInfoDTOs = new ArrayList<HealthInfoDTO>();
        for (HealthInfo healthInfo : healthInfos)
        {
            healthInfoDTOs.add(new HealthInfoDTO(healthInfo));
        }
        return healthInfoDTOs;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getPipeId() {
        return pipeId;
    }

    public void setPipeId(String pipeId) {
        this.pipeId = pipeId;
    }

    public String getPipeIdLink() {
        return pipeIdLink;
    }

    public void setPipeIdLink(String pipeIdLink) {
        this.pipeIdLink = pipeIdLink;
    }

    public String getPositionBtn() {
        return positionBtn;
    }

    public void setPositionBtn(String positionBtn) {
        this.positionBtn = positionBtn;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHealthRankLabel() {
        return healthRankLabel;
    }

    public void setHealthRankLabel(String healthRankLabel) {
        this.healthRankLabel = healthRankLabel;
    }

    public String getHealthRank() {
        return healthRank;
    }

    public void setHealthRank(String healthRank) {
        this.healthRank = healthRank;
    }

    public String getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(String detectTime) {
        this.detectTime = detectTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestionBtn() {
        return suggestionBtn;
    }

    public void setSuggestionBtn(String suggestionBtn) {
        this.suggestionBtn = suggestionBtn;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEvalRecordId() {
        return evalRecordId;
    }

    public void setEvalRecordId(String evalRecordId) {
        this.evalRecordId = evalRecordId;
    }
}
