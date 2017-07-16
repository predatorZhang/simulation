package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_FLOW_PERSON")
@SequenceGenerator(name = "SEQ_SM_FLOW_PERSON_ID",
        sequenceName = "SEQ_SM_FLOW_PERSON_ID",
        allocationSize=1, initialValue=1)
public class FlowPerson implements Serializable {

    private Long id;

    /** 人员名称 */
    private String userName;

    /** 人员是否有效 */
    private Boolean active;

    /** 对应系统人员ID */
    private Long sysPersonID;

    /** 人员所处直接部门 */
    private FlowDepartment flowDepartment;

    public FlowPerson() {
        super();
    }

    public FlowPerson(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_FLOW_PERSON_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "USERNAME", nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "SYS_PERSON_ID", nullable = false)
    public Long getSysPersonID() {
        return sysPersonID;
    }

    public void setSysPersonID(Long sysPersonID) {
        this.sysPersonID = sysPersonID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLOW_DEPARTMENT_ID")
    public FlowDepartment getFlowDepartment() {
        return flowDepartment;
    }

    public void setFlowDepartment(FlowDepartment flowDepartment) {
        this.flowDepartment = flowDepartment;
    }
}
