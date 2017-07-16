package com.casic.simulation.flow.bean;

import com.casic.simulation.device.domain.DeviceTypeEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2017/4/25.
 */
public enum EventTypeEnum {

    JISHUI_EVENT(1, EventSourceEnum.ALARM_RECORD,
            "给水管线类", new HashSet<Object>(Arrays.asList(
            DeviceTypeEnum.NOISE.toString(),
            DeviceTypeEnum.FLOW.toString(),
            DeviceTypeEnum.PRESS.toString(),
            DeviceTypeEnum.HYDRANT.toString()
    ))),
    YUWUSHUI_EVENT(2, EventSourceEnum.ALARM_RECORD,
            "雨污水管线类", new HashSet<Object>(Arrays.asList(
            DeviceTypeEnum.LIQUID.toString()
    ))),
    RANQIT_EVENT(3, EventSourceEnum.ALARM_RECORD,
            "燃气管线类", new HashSet<Object>(Arrays.asList(
            DeviceTypeEnum.GAS.toString()
    ))),
    WELL_EVENT(6, EventSourceEnum.ALARM_RECORD,
            "井盖监测类", new HashSet<Object>(Arrays.asList(
            DeviceTypeEnum.WELL.toString()
    ))),

    ALARM_EVENT(101, EventSourceEnum.ALARM_EVENT,
            "公众上报类", Collections.EMPTY_SET),

    FEEDBACK(201, EventSourceEnum.FEEDBACK,
            "人员巡检类", Collections.EMPTY_SET),

    UNKNOWN(-1, EventSourceEnum.UNKNOWN, "未知", Collections.EMPTY_SET);

    private Integer index;
    private EventSourceEnum source;
    private String typeName;
    private Set<Object> infos;

    private EventTypeEnum(Integer index,
                          EventSourceEnum source,
                          String typeName,
                          Set<Object> infos) {
        this.index = index;
        this.source = source;
        this.typeName = typeName;
        this.infos = infos;
    }

    public static EventTypeEnum getByIndex(Integer index) {
        for (EventTypeEnum temp : values()) {
            if (temp.index.equals(index)) {
                return temp;
            }
        }
        return UNKNOWN;
    }

    /**
     *
     * @param source 事件来源类型
     * @param o 事件子信息
     * @return
     */
    public static EventTypeEnum getBySourceAndInfo(
            EventSourceEnum source, Object o) {
        for (EventTypeEnum temp : values()) {
            if (temp.source == source &&
                    (temp.infos.isEmpty()
                    || temp.infos.contains(o))) {
                return temp;
            }
        }
        return UNKNOWN;
    }

    public Integer getIndex() {
        return index;
    }

    public EventSourceEnum getSource() {
        return source;
    }

    public String getTypeName() {
        return typeName;
    }

    public Set<Object> getInfos() {
        return infos;
    }
}
