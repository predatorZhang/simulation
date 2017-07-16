package com.casic.simulation.flow.bean;

/**
 * Created by lenovo on 2017/4/27.
 */
public enum OperationEnum {
    UNKNOWN("未知", "UNKNOWN", "未知操作", "", ""),
    MODIFY("修改备案", "MODIFY", "修改备案操作", "", ""),
    RECORD("备案", "RECORD", "备案操作", "", ""),
    HANDLE("处理", "HANDLE", "处理操作", "", ""),
    SIGN("签收", "SIGN", "签收操作", "", ""),
    DISTRIBUTE("派发", "DISTRIBUTE", "派发操作", "", ""),
    ABROGATE("废除", "ABROGATE", "废除操作", "", ""),
    CONFIRM("确认", "CONFIRM", "确认操作", "", "");

    private String operationName;
    private String operationCode;
    private String operationDesc;
    private String javascriptTag;
    private String androidTag;

    private OperationEnum(String operationName,
                          String operationCode,
                          String operationDesc,
                          String javascriptTag,
                          String androidTag) {
        this.operationName = operationName;
        this.operationCode = operationCode;
        this.operationDesc = operationDesc;
        this.javascriptTag = javascriptTag;
        this.androidTag = androidTag;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public String getJavascriptTag() {
        return javascriptTag;
    }

    public String getAndroidTag() {
        return androidTag;
    }

    public static OperationEnum getByCode(String code) {
        try {
            for (OperationEnum temp : values()) {
                if (code.equals(temp.operationCode)) {
                    return temp;
                }
            }
        } catch (Exception e){}
        return UNKNOWN;
    }
}
