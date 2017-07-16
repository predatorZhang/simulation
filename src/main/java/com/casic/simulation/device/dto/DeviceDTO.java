package com.casic.simulation.device.dto;


import com.casic.simulation.device.domain.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceDTO {
    private Long id;
    private String devCode;
    private String devName;
    private String latitude;
    private String longtitude;
    private String gaocheng;
    private String setupDate;
    private Long deviceTypeId;
    private String deviceTypeName;
    private String modelLocation;
    private String roadName;
    private String ownerId;//责任人编号
    private String devPerson;//责任人名称
    private String attachLayer;
    private String attachFeature;
    private String factory;
    private String alarmMessage;
    private String showData;
    private String dmaName;
    private String positionName;
    private String devTurnx;//设备管线附属种类（给水管线附属物、雨水管线附属物、天然气管线附属物等）
    private boolean isNormalDev;
    private boolean isNormal;
    private String flag;//用于接收报表参数
    private String wellDepth;
    private String liquidDepth;

    public String getAlarmData() {
        return alarmData;
    }

    public void setAlarmData(String alarmData) {
        this.alarmData = alarmData;
    }

    private String alarmData;

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public String getShowData() {
        return showData;
    }

    public void setShowData(String showData) {
        this.showData = showData;
    }

    private String timeData;

    public String getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(String modelLocation) {
        this.modelLocation = modelLocation;
    }

    public String getSetupDate()
    {
        return setupDate;
    }

    public void setSetupDate(String setupDate)
    {
        this.setupDate = setupDate;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDevCode()
    {
        return devCode;
    }

    public void setDevCode(String devCode)
    {
        this.devCode = devCode;
    }

    public String getDevName()
    {
        return devName;
    }

    public void setDevName(String devName)
    {
        this.devName = devName;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongtitude()
    {
        return longtitude;
    }

    public void setLongtitude(String longtitude)
    {
        this.longtitude = longtitude;
    }

    public String getGaocheng()
    {
        return gaocheng;
    }

    public void setGaocheng(String gaocheng)
    {
        this.gaocheng = gaocheng;
    }

    public Long getDeviceTypeId()
    {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId)
    {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName()
    {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName)
    {
        this.deviceTypeName = deviceTypeName;
    }

    public String getRoadName()
    {
        return roadName;
    }

    public void setRoadName(String roadName)
    {
        this.roadName = roadName;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getAttachLayer()
    {
        return attachLayer;
    }

    public void setAttachLayer(String attachLayer)
    {
        this.attachLayer = attachLayer;
    }

    public String getAttachFeature()
    {
        return attachFeature;
    }

    public void setAttachFeature(String attachFeature)
    {
        this.attachFeature = attachFeature;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getDmaName() {
        return dmaName;
    }

    public void setDmaName(String dmaName) {
        this.dmaName = dmaName;
    }

    public String getDevTurnx() {
        return devTurnx;
    }

    public void setDevTurnx(String devTurnx) {
        this.devTurnx = devTurnx;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDevPerson() {
        return devPerson;
    }

    public void setDevPerson(String devPerson) {
        this.devPerson = devPerson;
    }

    public boolean isNormalDev() {
        return isNormalDev;
    }

    public void setNormalDev(boolean isNormalDev) {
        this.isNormalDev = isNormalDev;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public DeviceDTO() {
    }

    public DeviceDTO(Long id, String devCode) {
        this.id = id;
        this.devCode = devCode;
    }

    public static DeviceDTO ConvertToDTO(Device device){
        if(null!=device){
            DeviceDTO dto = new DeviceDTO();
            dto.setId(device.getId());
            dto.setDevCode(device.getDevCode());
            dto.setDevName(device.getDevName());
            dto.setLatitude(device.getLatitude());
            dto.setLongtitude(device.getLongtitude());
            dto.setGaocheng(device.getGaocheng());
            dto.setFactory(device.getFactory());
            dto.setSetupDate(device.getInstallDate().toString());
            dto.setDevPerson(device.getAcceptPerson().getPersonName());

            if (device.getDeviceType() != null & device.getDeviceType().getActive() == true)
            {
                dto.setDeviceTypeId(device.getDeviceType().getId());
                dto.setModelLocation(device.getDeviceType().getLocation());
                String devTypeName = device.getDeviceType().getTypeName();
                String attachLayer = device.getTurnX();
                dto.setDeviceTypeName( (devTypeName.equals("液位监测仪") ? attachLayer.substring(0,2) : "") + devTypeName);
            }

            if (device.getAcceptPerson() != null & device.getAcceptPerson().getActive() == true)
            {
                dto.setOwnerId(device.getAcceptPerson().getId().toString());
            }

            //将道路名称保存到factory里面
            dto.setRoadName(device.getFactory());
            //转turnX转换为attach layer
            dto.setAttachLayer(device.getTurnX());
            //转turnY转为attach feature
            dto.setAttachFeature(device.getTurnY());
            //设置管线附属物种类名称
            dto.setDevTurnx(device.getTurnX());

            return dto;
        }
        return new DeviceDTO();
    }

    public static List<DeviceDTO> ConvertToDTOs(List<Device> list){
        List<DeviceDTO> dtoList = new ArrayList<DeviceDTO>();
        for(Device device : list){
            dtoList.add(ConvertToDTO(device));
        }
        return dtoList;
    }

    public String getWellDepth() {
        return wellDepth;
    }

    public void setWellDepth(String wellDepth) {
        this.wellDepth = wellDepth;
    }

    public String getLiquidDepth() {
        return liquidDepth;
    }

    public void setLiquidDepth(String liquidDepth) {
        this.liquidDepth = liquidDepth;
    }
}
