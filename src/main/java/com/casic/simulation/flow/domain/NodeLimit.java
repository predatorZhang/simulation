package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_NODE_LIMIT")
@SequenceGenerator(name = "SEQ_SM_NODE_LIMIT_ID", sequenceName = "SEQ_SM_NODE_LIMIT_ID",
        allocationSize=1, initialValue=1)
public class NodeLimit implements Serializable {

    private Long id;

    /** 对应节点 */
    private Node node;

    /** 对应事件类型 */
    private Integer eventType;

    /** 可操作人员的全局人员ID */
    private Long sysPersonID;

    /** 可操作人员的处置人员ID */
    private Long flowPersonID;

    /** 可操作部门的全局部门ID */
    private Long sysDepID;

    /** 可操作部门的处置部门ID */
    private Long flowDepID;

    public NodeLimit() {
        super();
    }

    public NodeLimit(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_NODE_LIMIT_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID", nullable = false)
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Column(name = "EVENT_TYPE")
    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    @Column(name = "SYS_PERSON_ID")
    public Long getSysPersonID() {
        return sysPersonID;
    }

    public void setSysPersonID(Long sysPersonID) {
        this.sysPersonID = sysPersonID;
    }

    @Column(name = "FLOW_PERSON_ID")
    public Long getFlowPersonID() {
        return flowPersonID;
    }

    public void setFlowPersonID(Long flowPersonID) {
        this.flowPersonID = flowPersonID;
    }

    @Column(name = "SYS_DEP_ID")
    public Long getSysDepID() {
        return sysDepID;
    }

    public void setSysDepID(Long sysDepID) {
        this.sysDepID = sysDepID;
    }

    @Column(name = "FLOW_DEP_ID")
    public Long getFlowDepID() {
        return flowDepID;
    }

    public void setFlowDepID(Long flowDepID) {
        this.flowDepID = flowDepID;
    }
}
