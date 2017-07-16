package com.casic.simulation.device.dto;

import java.util.Arrays;

public class DeviceDetailDTO {

    private String id;
    private String typeName;
    private String devCode;
    private boolean isNormal;
    private boolean isNormalDev;
    private String currentValue;
    private String alarmMsg;
    private String roadName;
    private String alarmTime;
    private String currentTime;
    private String itemValue;
    private String itemName;
    private String attachLayer;
    private String rule;
    private String pipeType;
    private double dis;

    private String co;
    private String o2;
    private String h2s;
    private String fireGas;

    public DeviceDetailDTO() {}

    public DeviceDetailDTO(String id, String typeName, String devCode, String currentValue, String currentTime, String roadName, String attachLayer) {
        this.id = id;
        this.typeName = typeName;
        this.devCode = devCode;
        this.currentValue = currentValue;
        this.currentTime = currentTime;
        this.roadName = roadName;
        this.attachLayer = attachLayer;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setIsNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }

    public boolean isNormalDev() {
        return isNormalDev;
    }

    public void setNormalDev(boolean isNormalDev) {
        this.isNormalDev = isNormalDev;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getAlarmMsg() {
        return alarmMsg;
    }

    public void setAlarmMsg(String alarmMsg) {
        this.alarmMsg = alarmMsg;
    }

    public String getAttachLayer() {
        return attachLayer;
    }

    public void setAttachLayer(String attachLayer) {
        this.attachLayer = attachLayer;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {//根据turnx字段提取管线类型

        if(null!=pipeType){
            this.pipeType = pipeType.substring(0,pipeType.indexOf("附属物"));
        }else {
            this.pipeType="-" ;
        }
        /*String [] pipeTypeList = pipeType.split(",");
        this.pipeType = "";
        for(int i=0; i<pipeTypeList.length; i++) {
            String pipeTy = pipeTypeList[i];
            if(!pipeTy.equals("-")) {
                this.pipeType += pipeTy.substring(0,pipeType.indexOf("附属物"));
            } else {
                this.pipeType += "-";
            }
            this.pipeType += ",";
        }
        this.pipeType = this.pipeType.substring(0, this.pipeType.length()-1);*/
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCo() {
        return co;
    }

    public String getO2() {
        return o2;
    }

    public String getH2s() {
        return h2s;
    }

    public String getFireGas() {
        return fireGas;
    }

    /**
     * 将有害气体监测值currentValue进行分解，逐一展示
     *
     * @param values
     */
    public void setHarmfulGas(String[] values) {
        if (values.length < 4) {
            String[] temp = new String[4];
            System.arraycopy(values, 0, temp, 0, values.length);
            Arrays.fill(temp, values.length, 4, "-");
        }
        this.co = values[0];
        this.o2 = values[1];
        this.h2s = values[2];
        this.fireGas = values[3];
    }
}
