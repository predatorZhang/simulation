package com.casic.simulation.planfile.domain;

/**
 * Created by lenovo on 2017/4/1.
 */
public enum PlanFileType {
    ONE_LEVEL("特大险情", "ONE_LEVEL", ""),
    TWO_LEVEL("重大险情", "TWO_LEVEL", ""),
    THREE_LEVEL("较大险情", "THREE_LEVEL", ""),
    FOUR_LEVEL("一般险情", "FOUR_LEVEL", ""),
    UNKNOWN("未知", "UNKNOWN", "");


    /** 预案种类名称 */
    private String name;

    /** 预案种类的编码 */
    private String code;

    /** 预案种类的描述 */
    private String desc;

    private PlanFileType(String name, String code, String desc) {
        this.name = name;
        this.code = code;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PlanFileType get(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static boolean existCode(String code) {
        for (PlanFileType temp : values()) {
            if (code.equals(temp.code)) {
                return true;
            }
        }
        return false;
    }
}
