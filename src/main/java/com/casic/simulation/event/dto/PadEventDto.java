package com.casic.simulation.event.dto;

import com.casic.simulation.event.domain.PadEvent;
import com.casic.simulation.util.MessageStatusEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/25.
 */
public class PadEventDto {
    private Long dbId;
    private String latitude;
    private String longitude;
    private String eventTime;
    private String descripe;
    private String imageName;
    private String patrolName;
    private String emergyId;
    private String status;//0：未处理；1：正在处理；2：已处理
    private String operate;
    private Long taskId;

    public PadEventDto() {
    }

    public PadEventDto(PadEvent event) {
        this.setDbId(event.getId());
        this.setLatitude(String.valueOf(event.getLatitude()));
        this.setLongitude(String.valueOf(event.getLongitude()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setEventTime(simpleDateFormat.format(event.getEventTime()));
        this.setDescripe(event.getDescripe());
        this.setImageName(event.getImageName());
        this.setStatus(MessageStatusEnum.getByIndex(event.getStatus()+"").getName());
        this.setTaskId(event.getTaskId());
    }

    public static List<PadEventDto> ConvertToDTOs(List<PadEvent> padEvents){
        List<PadEventDto> padEventDtos = new ArrayList<PadEventDto>();
        for(PadEvent padEvent:padEvents){
            padEventDtos.add(new PadEventDto(padEvent));
        }
        return padEventDtos;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getDescripe() {
        return descripe;
    }
    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPatrolName() {
        return patrolName;
    }
    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmergyId() {
        return emergyId;
    }

    public void setEmergyId(String emergyId) {
        this.emergyId = emergyId;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
