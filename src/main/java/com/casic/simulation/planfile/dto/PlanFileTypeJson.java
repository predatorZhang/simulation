package com.casic.simulation.planfile.dto;

import com.casic.simulation.planfile.domain.PlanFileType;

/**
 * Created by lenovo on 2017/4/1.
 */
public class PlanFileTypeJson {
    private String name;

    private String code;

    private String desc;

    private boolean selected = false;

    public PlanFileTypeJson() {}

    public PlanFileTypeJson(PlanFileType type) {
        this.name = type.getName();
        this.code = type.getCode();
        this.desc = type.getDesc();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
