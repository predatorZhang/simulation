package com.casic.simulation.dma;

import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
public class BackgroundLeakage {

    private double backNormalWaterSum;
    private Date date;
    private int regionID;
    private boolean ok;

    public double getBackNormalWaterSum() {
        return backNormalWaterSum;
    }

    public void setBackNormalWaterSum(double backNormalWaterSum) {
        this.backNormalWaterSum = backNormalWaterSum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
