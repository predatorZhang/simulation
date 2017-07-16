package com.casic.simulation.dma;

import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
public class MinimumFlow {

    private double minFlow; //最小流量值
    private Date minFlowTime; //最小流量最小时间
    private int regionID;//分区ID
    private String errorMsg;
    private boolean ok;

    public double getMinFlow() {
        return minFlow;
    }

    public void setMinFlow(double minFlow) {
        this.minFlow = minFlow;
    }

    public Date getMinFlowTime() {
        return minFlowTime;
    }

    public void setMinFlowTime(Date minFlowTime) {
        this.minFlowTime = minFlowTime;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
