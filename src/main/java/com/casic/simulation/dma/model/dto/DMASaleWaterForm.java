package com.casic.simulation.dma.model.dto;

public class DMASaleWaterForm {
    private String beginDate;
    private String endDate;
    private String water;
    private String noValueWater;
    private Long dmaID;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getNoValueWater() {
        return noValueWater;
    }

    public void setNoValueWater(String noValueWater) {
        this.noValueWater = noValueWater;
    }

    public Long getDmaID() {
        return dmaID;
    }

    public void setDmaID(Long dmaID) {
        this.dmaID = dmaID;
    }
}
