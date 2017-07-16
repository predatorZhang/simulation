package com.casic.simulation.util;

/**
 * Created by admin on 2017/4/6.
 * TODO：若BaseService中messageStatus修改，则本类需要同步修改 by wp
 */
public enum MessageStatusEnum {

    CONFIRM("待确认",0),
    ABROGATE("废除",1),
    DISTRIBUTE("待派发",2),
    SIGN("待签收",3),
    HANDLE("待处理",4),
    RECORD("待备案",5),
    FINISH("完成",6),
    UNKNOWN("未知",-1);

    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private MessageStatusEnum(String name, int index){
        this.name=name;
        this.index=index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    //覆盖方法
    @Override
    public String toString(){
        return this.name;
    }

    public static MessageStatusEnum getByIndex(String index) {
        try {
            for (MessageStatusEnum temp : values()) {
                if (temp.index == Integer.parseInt(index)) {
                    return temp;
                }
            }
        } catch (Exception e) {}
        return UNKNOWN;
    }

    public static MessageStatusEnum getByName(String name) {
        try {
            for (MessageStatusEnum temp : values()) {
                if (name.equals(temp.name)) {
                    return temp;
                }
            }
        } catch (Exception e) {}
        return UNKNOWN;
    }
}
