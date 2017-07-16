package com.casic.simulation.dma;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
public class AveragePressList {

    private List<AveragePress> averagePressList;
    private Date start;
    private Date end;
    private long regionID;
    private boolean ok;

    public List<AveragePress> getAveragePressList() {
        return averagePressList;
    }

    public void setAveragePressList(List<AveragePress> averagePressList) {
        this.averagePressList = averagePressList;
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
