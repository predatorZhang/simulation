package com.casic.simulation.flow.bean;

/**
 * Created by lenovo on 2017/4/27.
 */
public enum TreeNodeEnum {
    ROOT("root", "icon-root"),
    DEPARTMENT("department", "icon-root"),
    PERSON("person", "icon-client");

    private String type;
    private String iconCls;

    private TreeNodeEnum(String type, String iconCls) {
        this.type = type;
        this.iconCls = iconCls;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }
}
