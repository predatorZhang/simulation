package com.casic.simulation.overflow.bean;

import com.casic.simulation.core.util.RsHttpRequestUtil;

/**
 * Created by lenovo on 2017/4/13.
 */
public class LiquidDeviceRequest {

    private String baseURL;
    private String deviceListURL;
    private String alarmRecordURL;

    private String page;
    private String rows;
    private String devCode;
    private Long id;
    private String devName;
    private String turnX;
    private String messageStatus;
    private String beginDate;
    private String endDate;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getDeviceListURL() {
        return deviceListURL;
    }

    public void setDeviceListURL(String deviceListURL) {
        this.deviceListURL = deviceListURL;
    }

    public String getAlarmRecordURL() {
        return alarmRecordURL;
    }

    public void setAlarmRecordURL(String alarmRecordURL) {
        this.alarmRecordURL = alarmRecordURL;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getTurnX() {
        return turnX;
    }

    public void setTurnX(String turnX) {
        this.turnX = turnX;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
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

    public String queryDeviceList() {
        String retJsonStr = "";
        try {
            String url = baseURL + deviceListURL;
            url = RsHttpRequestUtil.appendParam(url, "page", page);
            url = RsHttpRequestUtil.appendParam(url, "rows", rows);
            url = RsHttpRequestUtil.appendParam(url, "devcode", devCode);

            retJsonStr = RsHttpRequestUtil.sendRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retJsonStr;
    }

    public String queryRecord() {
        String url = baseURL + alarmRecordURL;
        url = RsHttpRequestUtil.appendParam(url, "page", page);
        url = RsHttpRequestUtil.appendParam(url, "rows", rows);
        url = RsHttpRequestUtil.appendParam(url, "id", id);
        url = RsHttpRequestUtil.appendParam(url, "devname", devName);
        url = RsHttpRequestUtil.appendParam(url, "devcode", devCode);
        url = RsHttpRequestUtil.appendParam(url, "turnX", turnX);
        url = RsHttpRequestUtil.appendParam(url, "messageStatus", messageStatus);
        url = RsHttpRequestUtil.appendParam(url, "beginDate", beginDate);
        url = RsHttpRequestUtil.appendParam(url, "endDate", endDate);
        return RsHttpRequestUtil.sendRequest(url);
    }
}
