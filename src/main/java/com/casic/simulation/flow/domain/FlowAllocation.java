package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_FLOW_ALLOCATION")
@SequenceGenerator(name = "SEQ_SM_FLOW_ALLOCATION_ID",
        sequenceName = "SEQ_SM_FLOW_ALLOCATION_ID",
        allocationSize=1, initialValue=1)
public class FlowAllocation implements Serializable {

    private Long id;

    /** 对应流程 */
    private Flow flow;

    /** 对应事件类型 */
    private Integer eventType;

    /** 规则名称 */
    private String ruleName;

    /** 规则代码 */
    private String ruleCode;

    /** 规则描述 */
    private String descn;

    /** 规则生成时间 */
    private Date createTime;

    /** 规则更新时间 */
    private Date updateTime;

    public FlowAllocation() {
        super();
    }

    public FlowAllocation(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_FLOW_ALLOCATION_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLOW_ID", nullable = false)
    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    @Column(name = "EVENT_TYPE")
    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    @Column(name = "RULENAME")
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Column(name = "RULECODE")
    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    @Column(name = "DESCN")
    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
