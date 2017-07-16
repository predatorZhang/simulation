package com.casic.simulation.dma;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
public class MinimumFlowSum {

    private double minFlowSum; //夜间最小流量之和 m3
    private List<MinimumFlow> minimumFlowList; //m3/h
    private Date start;
    private Date end;
    private int regionID;//分区ID
    private boolean ok;

    public double getMinFlowSum() {
        return minFlowSum;
    }

    public void setMinFlowSum(double minFlowSum) {
        this.minFlowSum = minFlowSum;
    }

    public List<MinimumFlow> getMinimumFlowList() {
        return minimumFlowList;
    }

    public void setMinimumFlowList(List<MinimumFlow> minimumFlowList) {
        this.minimumFlowList = minimumFlowList;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
