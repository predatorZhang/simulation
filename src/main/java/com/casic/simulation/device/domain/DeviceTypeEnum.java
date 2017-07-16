package com.casic.simulation.device.domain;

/**
 * Created by admin on 2017/4/6.
 */
public enum DeviceTypeEnum {

    NOISE("噪声监测仪",1),
    FLOW("超声波流量监测仪",2),
    PRESS("压力监测仪",3),
    HYDRANT("消防栓防盗水监测仪",4),
    LIQUID("全量程液位监测仪",5),
    GAS("可燃气体泄露监测仪",6),
    WELL("井盖状态监测仪",7);

    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private DeviceTypeEnum(String name, int index){
        this.name=name;
        this.index=index;
    }
    //覆盖方法
    @Override
    public String toString(){
        return this.name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
