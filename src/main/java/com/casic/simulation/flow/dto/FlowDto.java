package com.casic.simulation.flow.dto;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.flow.domain.Flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */
public class FlowDto {

    public static final String ICON_OK = "icon-ok";
    public static final String ICON_NO = "icon-no";

    private Long id;

    /** 流程名称 */
    private String flowName;

    /** 流程描述 */
    private String descn;

    /** 流程描述图片路径 */
    private String flowPic;

    /** 流程是否有效 */
    private Boolean active;

    /** 流程状态修改最新时间 */
    private String updateTime;

    /** 展示图标 */
    private String iconCls;

    /** 是否勾选 */
    private boolean checked;

    /** 操作描述 */
    private String operations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getFlowPic() {
        return flowPic;
    }

    public void setFlowPic(String flowPic) {
        this.flowPic = flowPic;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    public static FlowDto convert(Flow flow) {
        FlowDto dto = new FlowDto();
        dto.setId(flow.getId());
        dto.setDescn(flow.getDescn());
        dto.setActive(flow.getActive());
        dto.setFlowName(flow.getFlowName());
        dto.setFlowPic(flow.getFlowPic());
        if (flow.getUpdateTime() != null) {
            dto.setUpdateTime(DateUtils.sdf4.format(flow.getUpdateTime()));
        }
        if (flow.getActive() != null && flow.getActive()) {
            dto.setChecked(true);
            dto.setIconCls(ICON_OK);
        } else {
            dto.setChecked(false);
            dto.setIconCls(ICON_NO);
        }
        return dto;
    }

    public static List<FlowDto> convert(List<Flow> flows) {
        List<FlowDto> dtos = new ArrayList<FlowDto>();
        for (Flow flow : flows) {
            if (flow != null) dtos.add(convert(flow));
        }
        return dtos;
    }
}
