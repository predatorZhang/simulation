package com.casic.simulation.event.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "ALARM_ALARM_EVENT")
@SequenceGenerator(name = "SEQ_ALARM_EVENT_ID", sequenceName = "SEQ_ALARM_EVENT_ID", allocationSize=1,
        initialValue=1)
public class AlarmEvent implements Serializable {
    /**人工上报的报警事件
     *
     */
    private Long id;
    private String location;
    private String latitude;
    private String longitude;
    private String message;
    private String itemName;
    private String itemValue;
    private Date recordDate =new Date();
    private Integer messageStatus = 0;
    private Boolean active = true;
    private Boolean isSend = false;

    public AlarmEvent() {
        super();
    }

    public AlarmEvent(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_EVENT_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "LATITUDE")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "MESSAGE", nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "RECORDDATE")
    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    @Column(name = "MESSAGE_STATUS", nullable = false)
    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Column(name = "itemName", nullable = false)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name = "itemValue", nullable = false)
    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Column(name = "ISSEND", nullable = false)
    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

}
