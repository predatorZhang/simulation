package com.casic.simulation.device.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ALARM_SENSOR")
public class SensorType implements java.io.Serializable {
    private String sensorcode;
    private String sensorname;
    private Boolean isuse = true;
    private String defaultid;

    public SensorType() {
    }

    public SensorType(String sensorcode) {
        this.sensorcode = sensorcode;
    }

    public SensorType(String sensorcode, String sensorname, Boolean isuse, String defaultid) {
        this.sensorcode = sensorcode;
        this.sensorname = sensorname;
        this.isuse = isuse;
        this.defaultid = defaultid;
    }

    @Id
    @Column(name = "SENSORCODE", nullable = false, unique = true)
    public String getSensorcode() {
        return this.sensorcode;
    }

    public void setSensorcode(String sensorcode) {
        this.sensorcode = sensorcode;
    }

    @Column(name = "SENSORNAME", nullable = false)
    public String getSensorname() {
        return this.sensorname;
    }

    public void setSensorname(String sensorname) {
        this.sensorname = sensorname;
    }

    @Column(name = "ACTIVE", precision = 1, scale = 0)
    public Boolean getIsuse() {
        return this.isuse;
    }

    public void setIsuse(Boolean isuse) {
        this.isuse = isuse;
    }

    @Column(name = "DEFAULTID")
    public String getDefaultid() {
        return this.defaultid;
    }

    public void setDefaultid(String defaultid) {
        this.defaultid = defaultid;
    }

}