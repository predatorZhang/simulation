package com.casic.simulation.event.bean;

import com.casic.simulation.core.util.RsHttpRequestUtil;

/**
 * Created by lenovo on 2017/4/12.
 */
public class AlarmRecordRequest {
    private String baseURL;
    private String messageStatusListURL;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getMessageStatusListURL() {
        return messageStatusListURL;
    }

    public void setMessageStatusListURL(String messageStatusListURL) {
        this.messageStatusListURL = messageStatusListURL;
    }

    public String queryMessageStatusList() {
        String url = baseURL + messageStatusListURL;
        return RsHttpRequestUtil.sendRequest(url);
    }
}
