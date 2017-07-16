package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_FLOW_DEPARTMENT")
@SequenceGenerator(name = "SEQ_SM_FLOW_DEPARTMENT_ID",
        sequenceName = "SEQ_SM_FLOW_DEPARTMENT_ID",
        allocationSize=1, initialValue=1)
public class FlowDepartment implements Serializable {

    private Long id;

    /** 部门名称 */
    private String depName;

    /** 部门描述 */
    private String descn;

    /** 部门是否有效 */
    private Boolean active;

    /** 对应系统部门ID */
    private Long sysDepID;

    /** 父部门 */
    private FlowDepartment parent;

    /** 部门下直接人员 */
    private List<FlowPerson> persons;

    /** 部门下直接部门 */
    private List<FlowDepartment> children;

    public FlowDepartment() {
        super();
    }

    public FlowDepartment(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_FLOW_DEPARTMENT_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "DEP_NAME")
    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    @Column(name = "DESCN")
    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "SYS_DEP_ID")
    public Long getSysDepID() {
        return sysDepID;
    }

    public void setSysDepID(Long sysDepID) {
        this.sysDepID = sysDepID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public FlowDepartment getParent() {
        return parent;
    }

    public void setParent(FlowDepartment parent) {
        this.parent = parent;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flowDepartment")
    public List<FlowPerson> getPersons() {
        return persons;
    }

    public void setPersons(List<FlowPerson> persons) {
        this.persons = persons;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public List<FlowDepartment> getChildren()
    {
        return children;
    }
    public void setChildren(List<FlowDepartment> children)
    {
        this.children = children;
    }
}
