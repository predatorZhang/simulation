package com.casic.simulation.event.dto;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.event.domain.AlarmEvent;
import com.casic.simulation.util.MessageStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/25.
 */
public class AlarmEventDto {
    private Long id;
    private String location;
    private String latitude;
    private String longitude;
    private String message;
    private String itemName;
    private String itemValue;
    private String recordDate;
    private String messageStatus;
    private String operate;
    private Boolean active = true;
    private Boolean isSend = false;
    
    public static AlarmEventDto ConvertToDTO(AlarmEvent alarmEvent){
        if(null!=alarmEvent){
            AlarmEventDto dto = new AlarmEventDto();
            dto.setId(alarmEvent.getId());
            dto.setLocation(alarmEvent.getLocation());
            dto.setActive(alarmEvent.getActive());
            dto.setIsSend(alarmEvent.getIsSend());
            dto.setItemName(alarmEvent.getItemName());
            dto.setItemValue(alarmEvent.getItemValue());
            dto.setMessage(alarmEvent.getMessage());
            String statusIndex = String.valueOf(alarmEvent.getMessageStatus());
            dto.setMessageStatus(MessageStatusEnum.getByIndex(statusIndex).getName());
            if(null!=alarmEvent.getRecordDate()) {
                dto.setRecordDate(DateUtils.sdf4.format(alarmEvent.getRecordDate()));
            } else {
                dto.setRecordDate("");
            }
            dto.setLatitude(alarmEvent.getLatitude());
            dto.setLongitude(alarmEvent.getLongitude());

            return dto;
        }
        return new AlarmEventDto();
    }

    public static List<AlarmEventDto> ConvertToDTOs(List<AlarmEvent> list){
        List<AlarmEventDto> dtoList = new ArrayList<AlarmEventDto>();
        for(AlarmEvent alarmRecord2 : list){
            dtoList.add(ConvertToDTO(alarmRecord2));
        }
        return dtoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
