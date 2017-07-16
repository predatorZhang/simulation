package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_FLOW")
@SequenceGenerator(name = "SEQ_SM_FLOW_ID", sequenceName = "SEQ_SM_FLOW_ID",
        allocationSize=1, initialValue=1)
public class Flow implements Serializable {

    private Long id;

    /** 流程名称 */
    private String flowName;

    /** 流程代号 */
    private String flowCode;

    /** 流程描述 */
    private String descn;

    /** 流程描述图片路径 */
    private String flowPic;

    /** 流程是否有效 */
    private Boolean active;

    /** 流程状态修改最新时间 */
    private Date updateTime;

    public Flow() {
        super();
    }

    public Flow(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_FLOW_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FLOWNAME")
    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    @Column(name = "FLOWCODE")
    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    @Column(name = "DESCN")
    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    @Column(name = "FLOW_PIC")
    public String getFlowPic() {
        return flowPic;
    }

    public void setFlowPic(String flowPic) {
        this.flowPic = flowPic;
    }

    @Column(name = "ACTIVE", nullable = false)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
