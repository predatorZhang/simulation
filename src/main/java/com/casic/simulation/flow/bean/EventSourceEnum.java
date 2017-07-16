package com.casic.simulation.flow.bean;

/**
 * Created by lenovo on 2017/4/25.
 */
public enum EventSourceEnum {

    ALARM_RECORD(1, "ALARM_ALARM_RECORD", "设备报警类信息"),
    ALARM_EVENT(2, "ALARM_ALARM_EVENT", "公众上报类信息"),
    FEEDBACK(3, "XJ_FEEDBACK", "巡检报警类信息"),
    UNKNOWN(-1, "UNKNOWN", "未知");

    /** 事件种类index **/
    private Integer eventSrc;

    /** 事件来源信息表 **/
    private String srcTableName;

    /** 事件来源描述 **/
    private String desc;

    private EventSourceEnum(Integer eventSrc,
                            String srcTableName,
                            String desc) {
        this.eventSrc = eventSrc;
        this.srcTableName = srcTableName;
        this.desc = desc;
    }

    public static EventSourceEnum getByEventSrc(Integer eventSrc) {
        for (EventSourceEnum temp : values()) {
            if (temp.eventSrc == eventSrc) {
                return temp;
            }
        }
        return UNKNOWN;
    }

    public Integer getEventSrc() {
        return eventSrc;
    }

    public String getSrcTableName() {
        return srcTableName;
    }

    public String getDesc() {
        return desc;
    }
}
