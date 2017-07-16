package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Predator on 2015/6/15.
 * todo:引用“统一用户及权限管理系统”文件
 */
@Entity
@Table(name = "ZX_YWGL_DEPARTMENT")
@SequenceGenerator(name = "SEQ_ZX_YWGL_DEPARTMENT_ID", sequenceName = "SEQ_ZX_YWGL_DEPARTMENT_ID", allocationSize = 1, initialValue = 1)
public class Department implements Serializable
{
    private  long id;

    private String name;

    private Department parent;

    private String code;

    private List<Department> children;

    private List<UserInfo> userInfos;

    private int status = 1;//1:状态为正常,0：为已经删除

    @Column(name="STATUS")
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    public List<UserInfo> getUserInfos()
    {
        return userInfos;
    }
    public void setUserInfos(List<UserInfo> userInfos)
    {
        this.userInfos = userInfos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ZX_YWGL_DEPARTMENT_ID")
    @Column(name = "DBID")
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }

    @Column(name="NAME")
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public Department getParent()
    {
        return parent;
    }
    public void setParent(Department parent)
    {
        this.parent = parent;
    }

    @Column(name="CODE")
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public List<Department> getChildren()
    {
        return children;
    }
    public void setChildren(List<Department> children)
    {
        this.children = children;
    }
}
