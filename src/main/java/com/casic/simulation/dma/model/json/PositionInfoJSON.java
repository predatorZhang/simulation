package com.casic.simulation.dma.model.json;

import com.casic.simulation.dma.model.domain.PositionInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PositionInfoJSON {
    private Long ID; // ID
    private String Name; // 监测点名称
    private String Longitude; // 经度
    private String Latitude; // 纬度
    private String BDataPosType; // 监测点类型
    private String SortCode; // 排序码
    private String Comment; // 备注
    private Boolean IsUse; // 是否启用
    private String Operator; // 操作人
    private Date OperateTime; // 操作时间

    public PositionInfoJSON(PositionInfo positionInfo) {
        this.ID = positionInfo.getID();
        this.Name = positionInfo.getName();
        this.Longitude = positionInfo.getLongitude();
        this.Latitude = positionInfo.getLatitude();
        this.BDataPosType = positionInfo.getBDataPosType();
        this.SortCode = positionInfo.getSortCode();
        this.Comment = positionInfo.getComment();
        this.IsUse = positionInfo.getIsUse();
        this.Operator = positionInfo.getOperator();
        this.OperateTime = positionInfo.getOperateTime();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getBDataPosType() {
        return BDataPosType;
    }

    public void setBDataPosType(String bDataPosType) {
        BDataPosType = bDataPosType;
    }

    public String getSortCode() {
        return SortCode;
    }

    public void setSortCode(String sortCode) {
        SortCode = sortCode;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Boolean getIsUse() {
        return IsUse;
    }

    public void setIsUse(Boolean isUse) {
        IsUse = isUse;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public Date getOperateTime() {
        return OperateTime;
    }

    public void setOperateTime(Date operateTime) {
        OperateTime = operateTime;
    }

    public static List<PositionInfoJSON> convertTo(List<PositionInfo> infos) {
        List<PositionInfoJSON> list = new ArrayList<PositionInfoJSON>();
        for (PositionInfo info : infos) {
            list.add(new PositionInfoJSON(info));
        }
        return list;
    }
}
