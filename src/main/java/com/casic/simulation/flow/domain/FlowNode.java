package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 * 某固定流程就表示了固定的节点拼接，
 * 而节点之间的拼接代表了一类操作
 */
@Entity
@Table(name = "SM_FLOW_NODE")
@SequenceGenerator(name = "SEQ_SM_FLOW_NODE_ID", sequenceName = "SEQ_SM_FLOW_NODE_ID",
        allocationSize=1, initialValue=1)
public class FlowNode implements Serializable {

    private Long id;

    /** 关联流程 */
    private Flow flow;

    /** 关联节点 */
    private Node node;

    /** 提供的操作 */
    private String operation;

    /** 提供的操作代码 */
    private String operationCode;

    /** 提供的操作具体描述 */
    private String operationDesc;

    /** 提供的操作下一个节点 */
    private FlowNode next;

    /** 是否为流程的开始节点 */
    private Boolean head;

    public FlowNode() {
        super();
    }

    public FlowNode(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_FLOW_NODE_ID")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID", nullable = false)
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Column(name = "OPERATION")
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Column(name = "OPERATION_CODE")
    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    @Column(name = "OPERATION_DESC")
    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_FLOW_NODE_ID", nullable = false)
    public FlowNode getNext() {
        return next;
    }

    public void setNext(FlowNode next) {
        this.next = next;
    }

    @Column(name = "HEAD")
    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }
}
