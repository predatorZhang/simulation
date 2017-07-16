package com.casic.simulation.event.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "XJ_FEEDBACK")
@SequenceGenerator(name = "SEQ_XJ_FEED_BACK_ID", sequenceName = "SEQ_XJ_FEED_BACK_ID",allocationSize=1,initialValue=1)
public class PadEvent  implements Serializable
{
    private static final long serialVersionUID = 1L;
    private long id;
    private double latitude;
    private double longitude;
    private Date eventTime;
    private String descripe;
    private String imageName;
    private Long taskId;
    private int status=0;//0：未处理；1：正在处理；2：已处理

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XJ_FEED_BACK_ID")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="LATITUDE")
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name="LONGITUDE")
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Column(name="EVENTTIME")
    public Date getEventTime() {
        return eventTime;
    }
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    @Column(name="DESCRIPTION")
    public String getDescripe() {
        return descripe;
    }

    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }

    @Column(name="IMAGENAME")
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Column(name="STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name="TASK_ID")
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
