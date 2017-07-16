package com.casic.simulation.event.dto;


import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.device.domain.DeviceTypeEnum;
import com.casic.simulation.event.domain.AlarmRecord;
import com.casic.simulation.event.domain.AlarmRecordTypeEnum;
import com.casic.simulation.util.MessageStatusEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlarmRecordDTO
{

	private String deviceId;
	private String deviceTypeName;
	private String itemName;
	private String message;
	private String dealPerson;
	private String messageStatus;
	private String recordDate;
	private String deviceCode;
    private String roadName;
	private String itemValue;
	private String deviceName;
    private String beginDate;
    private String endDate;
    private String loginDate;
    private String pipeType;
    private Long id;
    private int page;
    private int rows;
    private String operate;
    private String turnX; //设备管线附属种类（给水管线附属物、雨水管线附属物、天然气管线附属物等）
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
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

    public String getDealPerson() {
        return dealPerson;
    }

    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getTurnX() {
        return turnX;
    }

    public void setTurnX(String turnX) {
        this.turnX = turnX;
    }

    public AlarmRecordDTO() {
        this.page = this.page <= 0 ? 1 : this.page;
        this.rows = this.rows <= 0 ? 10 : this.rows;
    }
    public static AlarmRecordDTO ConvertToDTO(AlarmRecord alarmRecord) {
        if (null != alarmRecord) {
            AlarmRecordDTO dto = new AlarmRecordDTO();
            if(null!=alarmRecord.getDevice()){
                dto.setDeviceCode(alarmRecord.getDevice().getDevCode());
                dto.setRoadName(alarmRecord.getDevice().getFactory());
                dto.setDeviceName(alarmRecord.getDevice().getDevName());
                if(null!=alarmRecord.getDevice().getTurnX()){
                    dto.setPipeType(alarmRecord.getDevice().getTurnX());
                }else {
                    dto.setPipeType("--");
                }
                if(alarmRecord.getDevice().getDeviceType()!=null){
                    dto.setDeviceTypeName(alarmRecord.getDevice().getDeviceType().getTypeName());
                }
            }
//            dto.setMessage(alarmRecord.getMessage());
            dto.setMessage(AlarmRecordTypeEnum.getAlarmTypeByDevTypeName(alarmRecord.getDevice().getDeviceType().getTypeName()));
            String statusIndex = String.valueOf(alarmRecord.getMessageStatus());
            dto.setMessageStatus(MessageStatusEnum.getByIndex(statusIndex).getName());
            dto.setId(alarmRecord.getId());
            dto.setItemValue(alarmRecord.getItemValue());
            dto.setDeviceId(alarmRecord.getDevice().getId().toString());
            if(null!=alarmRecord.getRecordDate()) {
                dto.setRecordDate(DateUtils.sdf4.format(alarmRecord.getRecordDate()));
            }
            return dto;
        }
        return new AlarmRecordDTO();
    }

    public static List<AlarmRecordDTO> ConvertToDTOs(List<AlarmRecord> list) {
        List<AlarmRecordDTO> dtoList = new ArrayList<AlarmRecordDTO>();
        for (AlarmRecord alarmRecord : list) {
            dtoList.add(ConvertToDTO(alarmRecord));
        }
        return dtoList;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
