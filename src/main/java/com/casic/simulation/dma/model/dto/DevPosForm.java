package com.casic.simulation.dma.model.dto;

/**
 * Created by 203 on 2015/9/10.
 */
public class DevPosForm {
    private Long devId;
    private Long positionId;           // 监测点ID
    private String sensorType;         //传感器类型 选择输入 [{供水流量：Data3A}；{供水压力：Data40}；{供水噪声：Data02}；{排水液位：Data50}]
    private String pipeMaterial;       //管材 选择输入：球墨铸铁，铸铁，钢/镀锌，铜，钢筋混凝土/水泥，铅，铅银/铜合金，玻璃钢，陶瓷，PVC，PE
    private Integer pipeSize;       //管径  单位毫米
    private Double startTotalValue;    //起始累计流量 流量传感器必填
    private Double lowInstantValue = 0d;    //低值报警
    private Double highInstantValue = 0d;   //高值报警
    private Long regionId;
    private int page;
    private int rows;

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getPipeMaterial() {
        return pipeMaterial;
    }

    public void setPipeMaterial(String pipeMaterial) {
        this.pipeMaterial = pipeMaterial;
    }

    public Integer getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(Integer pipeSize) {
        this.pipeSize = pipeSize;
    }

    public Double getStartTotalValue() {
        return startTotalValue;
    }

    public void setStartTotalValue(Double startTotalValue) {
        this.startTotalValue = startTotalValue;
    }

    public Double getLowInstantValue() {
        return lowInstantValue;
    }

    public void setLowInstantValue(Double lowInstantValue) {
        this.lowInstantValue = lowInstantValue;
    }

    public Double getHighInstantValue() {
        return highInstantValue;
    }

    public void setHighInstantValue(Double highInstantValue) {
        this.highInstantValue = highInstantValue;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
