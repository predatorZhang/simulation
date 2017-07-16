package com.casic.simulation.event.domain;

import com.casic.simulation.device.domain.DeviceTypeEnum;
import org.springframework.util.StringUtils;

/**
 * Created by lenovo on 2017/4/27.
 */
public enum AlarmRecordTypeEnum {
    LIQUID_OVER_THRESH("液位超限", DeviceTypeEnum.LIQUID.getName()),
    NOISE_OVER_THRESH("管线泄漏", DeviceTypeEnum.NOISE.getName()),
    PRESS_OVER_THRESH("压力超限",DeviceTypeEnum.PRESS.getName()),
    GAS_OVER_THRESH("浓度超限", DeviceTypeEnum.GAS.getName()),
    WELL_OPEN("井盖开启",DeviceTypeEnum.WELL.getName());


    private String alarmtype;
    private String alarmDeviceType;

    private AlarmRecordTypeEnum(String alarmType, String alarmDeviceType){
        this.alarmtype = alarmType;
        this.alarmDeviceType =alarmDeviceType;
    }
    public String getAlarmtype() {
        return alarmtype;
    }

    public String getAlarmDeviceType() {
        return alarmDeviceType;
    }
    public static String getDevTypeByAlarType(String devType){
        if(StringUtils.isEmpty(devType)) return "";
        if(devType.equals(LIQUID_OVER_THRESH.getAlarmtype())) return DeviceTypeEnum.LIQUID.getName();
        if(devType.equals(NOISE_OVER_THRESH.getAlarmtype())) return DeviceTypeEnum.NOISE.getName();
        if(devType.equals(PRESS_OVER_THRESH.getAlarmtype())) return DeviceTypeEnum.PRESS.getName();
        if(devType.equals(GAS_OVER_THRESH.getAlarmtype())) return DeviceTypeEnum.GAS.getName();
        if(devType.equals(WELL_OPEN.getAlarmtype())) return DeviceTypeEnum.WELL.getName();
        return "";
    }
    public static String getAlarmTypeByDevTypeName(String deviceTypeName){
        if(StringUtils.isEmpty(deviceTypeName)) return "";
        if(deviceTypeName.equals(DeviceTypeEnum.LIQUID.getName())) return LIQUID_OVER_THRESH.getAlarmtype();
        if(deviceTypeName.equals(DeviceTypeEnum.NOISE.getName())) return NOISE_OVER_THRESH.getAlarmtype();
        if(deviceTypeName.equals(DeviceTypeEnum.PRESS.getName())) return PRESS_OVER_THRESH.getAlarmtype();
        if(deviceTypeName.equals(DeviceTypeEnum.GAS.getName())) return GAS_OVER_THRESH.getAlarmtype();
        if(deviceTypeName.equals(DeviceTypeEnum.WELL.getName())) return WELL_OPEN.getAlarmtype();
        return "";
    }
}
