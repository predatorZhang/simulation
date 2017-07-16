package com.casic.simulation.dma.model.dto;

public class PositionForm {
    private Long ID;
    private Long dmaID;
    private String Name; // 监测点名称
    private String Longitude; // 经度
    private String Latitude; // 纬度
    private String BDataPosType; // 监测点类型
    private String SortCode; // 排序码
    private String Comment; // 备注
    private Boolean IsUse; // 是否启用
    private String Operator; // 操作人
    private String Direction;//流向

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Long getDmaID() {
        return dmaID;
    }

    public void setDmaID(Long dmaID) {
        this.dmaID = dmaID;
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

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }
}
