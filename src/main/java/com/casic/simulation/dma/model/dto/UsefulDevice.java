package com.casic.simulation.dma.model.dto;

/**
 * Created by lenovo on 2017/4/14.
 */
public enum UsefulDevice {
    NOISE("噪声监测仪",1),
    FLOW("000031",2),
    PRESS("000033",3);

    /** 成员变量 */
    private String name;

    /** 设备种类编号 */
    private int index;

    private UsefulDevice(String name, int index){
        this.name=name;
        this.index=index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
