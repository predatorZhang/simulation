package com.casic.simulation.flow.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/25.
 */
@Entity
@Table(name = "SM_NODE")
@SequenceGenerator(name = "SEQ_SM_NODE_ID", sequenceName = "SEQ_SM_NODE_ID",
        allocationSize=1, initialValue=1)
public class Node implements Serializable {

    private Long id;

    /** 节点名称 */
    private String nodeName;

    /** 节点操作对应的系统名称 */
    private String sysName;

    /** 节点操作对应的系统代号 */
    private String sysCode;

    /** 节点描述 */
    private String descn;

    /**
     * 节点操作对应的status，该status与事件记录表中messageStatus对应
     * TODO：若BaseService中messageStatus修改，则本系统数据库需要修改 by wp
     **/
    private Integer messageStatus;

    public Node() {
        super();
    }

    public Node(Long id) {
        super();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_NODE_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NODENAME")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Column(name = "SYS_NAME")
    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Column(name = "SYS_CODE")
    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    @Column(name = "DESCN")
    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    @Column(name = "MESSAGE_STATUS", nullable = false)
    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

}
