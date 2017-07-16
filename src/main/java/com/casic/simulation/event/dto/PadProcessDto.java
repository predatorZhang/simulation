package com.casic.simulation.event.dto;

import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
public class PadProcessDto {
    private String padPicPath;
    private String padEventDescription;
    private Date padProcessTime;

    public String getPadPicPath() {
        return padPicPath;
    }

    public void setPadPicPath(String padPicPath) {
        this.padPicPath = padPicPath;
    }

    public String getPadEventDescription() {
        return padEventDescription;
    }

    public void setPadEventDescription(String padEventDescription) {
        this.padEventDescription = padEventDescription;
    }

    public Date getPadProcessTime() {
        return padProcessTime;
    }

    public void setPadProcessTime(Date padProcessTime) {
        this.padProcessTime = padProcessTime;
    }
}
