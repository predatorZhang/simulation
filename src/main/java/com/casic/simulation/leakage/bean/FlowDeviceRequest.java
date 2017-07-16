package com.casic.simulation.leakage.bean;

import com.casic.simulation.core.util.RsHttpRequestUtil;

/**
 * Created by lyl on 2017/4/12.
 */
public class FlowDeviceRequest {
    private String baseURL;
    private String deviceListURL;

    private String page;
    private String rows;
    private String devCode;
    private Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}