package com.casic.simulation.flow.dto;

import java.util.List;

/**
 * Created by admin on 2015/6/16.
 */
public class FlowTreeNodeDto {

    private Long id;

    /** 节点名称 */
    private String text;

    /** 节点种类 */
    private String type;

    /** 节点图标 */
    private String iconCls;

    /** 节点合拢状态 */
    private String state;

    /** 节点是否选中 */
    private boolean checked = false;

    /** 关联的规则ID */
    private Long nodeLimitID;

    /** 对应系统部门ID */
    private List<FlowTreeNodeDto> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<FlowTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<FlowTreeNodeDto> children) {
        this.children = children;
    }

    public Long getNodeLimitID() {
        return nodeLimitID;
    }

    public void setNodeLimitID(Long nodeLimitID) {
        this.nodeLimitID = nodeLimitID;
    }
}
