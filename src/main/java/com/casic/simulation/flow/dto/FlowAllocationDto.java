package com.casic.simulation.flow.dto;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.domain.FlowAllocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */
public class FlowAllocationDto {
    private Long id;

    /** 对应流程ID */
    private Long flowID;

    /** 对应流程名称 */
    private String flowName;

    /** 对应事件类型 */
    private Integer eventType;

    /** 对应事件类型描述 */
    private String eventTypeDesc;

    /** 规则名称 */
    private String ruleName;

    /** 规则代码 */
    private String ruleCode;

    /** 规则描述 */
    private String descn;

    /** 规则生成时间 */
    private String createTime;

    /** 规则更新时间 */
    private String updateTime = "-";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowID() {
        return flowID;
    }

    public void setFlowID(Long flowID) {
        this.flowID = flowID;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getEventTypeDesc() {
        return eventTypeDesc;
    }

    public void setEventTypeDesc(String eventTypeDesc) {
        this.eventTypeDesc = eventTypeDesc;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static FlowAllocationDto convert(FlowAllocation flow) {
        FlowAllocationDto dto = new FlowAllocationDto();
        dto.setId(flow.getId());
        dto.setFlowID(flow.getFlow().getId());
        dto.setFlowName(flow.getFlow().getFlowName());
        dto.setEventType(flow.getEventType());
        dto.setEventTypeDesc(
            EventTypeEnum.getByIndex(flow.getEventType()).getTypeName()
        );
        dto.setRuleName(flow.getRuleName());
        dto.setRuleCode(flow.getRuleCode());
        dto.setDescn(flow.getDescn());
        if (flow.getCreateTime() != null) {
            dto.setCreateTime(DateUtils.sdf4.format(flow.getCreateTime()));
        }
        if (flow.getUpdateTime() != null) {
            dto.setUpdateTime(DateUtils.sdf4.format(flow.getUpdateTime()));
        }
        return dto;
    }

    public static List<FlowAllocationDto> convert(List<FlowAllocation> flows) {
        List<FlowAllocationDto> dtos = new ArrayList<FlowAllocationDto>();
        for (FlowAllocation flow : flows) {
            if (flow != null) dtos.add(convert(flow));
        }
        return dtos;
    }
}
