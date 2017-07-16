package com.casic.simulation.dma;

import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
public class AveragePress {

    private double avgPress;
    private Date date;
    private long regionID;
    private boolean ok;

    public double getAvgPress() {
        return avgPress;
    }

    public void setAvgPress(double avgPress) {
        this.avgPress = avgPress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRegionID() {
        return regionID;
    }

    public void setRegionID(long regionID) {
        this.regionID = regionID;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
