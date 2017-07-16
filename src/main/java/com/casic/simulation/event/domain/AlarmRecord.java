package com.casic.simulation.event.domain;

import com.casic.simulation.device.domain.Device;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "ALARM_ALARM_RECORD")
@SequenceGenerator(name = "SEQ_ALARM_RECORD_ID", sequenceName = "SEQ_ALARM_RECORD_ID", allocationSize=1,initialValue=1)
public class AlarmRecord implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1658147150462234453L;
    private Long id;

    private String message;
    private String itemName = "default";
    private String itemValue = "default";
    private Device device;
    private String deviceCode;
    private String deviceTypeName;
    private Date recordDate =new Date();
    private Integer messageStatus = 0;//0：未处理，1：正在处理，2：已处理
    private Boolean active = true;
    private Boolean isSend = false;

    public AlarmRecord() {
        super();
    }

    public AlarmRecord(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_RECORD_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "MESSAGE", nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEVICE_ID", nullable = true)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Column(name = "DEVICE_CODE", nullable = false)
    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @Column(name = "DEVICE_TYPE_NAME", nullable = false)
    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
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