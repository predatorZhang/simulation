package com.casic.simulation.flow.dto;

import com.casic.simulation.flow.domain.NodeLimit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */
public class NodeLimitDto {

    private Long id;

    /** 对应节点 */
    private Long nodeID;

    /** 对应节点名称 */
    private String nodeName;

    /** 对应事件类型index */
    private Integer eventType;

    /** 对应事件类型名称 */
    private String eventTypeName;

    /** 可操作人员的全局人员ID */
    private Long sysPersonID;

    /** 可操作人员的处置人员ID */
    private Long flowPersonID;

    /** 可操作人员的处置人员名称 */
    private String flowPersonName;

    /** 可操作部门的全局部门ID */
    private Long sysDepID;

    /** 可操作部门的处置部门ID */
    private Long flowDepID;

    /** 可操作部门的处置部门名称 */
    private String flowDepName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Long getSysPersonID() {
        return sysPersonID;
    }

    public void setSysPersonID(Long sysPersonID) {
        this.sysPersonID = sysPersonID;
    }

    public Long getFlowPersonID() {
        return flowPersonID;
    }

    public void setFlowPersonID(Long flowPersonID) {
        this.flowPersonID = flowPersonID;
    }

    public String getFlowPersonName() {
        return flowPersonName;
    }

    public void setFlowPersonName(String flowPersonName) {
        this.flowPersonName = flowPersonName;
    }

    public Long getSysDepID() {
        return sysDepID;
    }

    public void setSysDepID(Long sysDepID) {
        this.sysDepID = sysDepID;
    }

    public Long getFlowDepID() {
        return flowDepID;
    }

    public void setFlowDepID(Long flowDepID) {
        this.flowDepID = flowDepID;
    }

    public String getFlowDepName() {
        return flowDepName;
    }

    public void setFlowDepName(String flowDepName) {
        this.flowDepName = flowDepName;
    }

    public static NodeLimitDto convert(NodeLimit node) {
        NodeLimitDto dto = new NodeLimitDto();
        dto.setId(node.getId());
        dto.setEventType(node.getEventType());
        dto.setFlowDepID(node.getFlowDepID());
        dto.setFlowPersonID(node.getFlowPersonID());
        dto.setNodeID(node.getNode().getId());
        dto.setNodeName(node.getNode().getNodeName());
        dto.setSysDepID(node.getSysDepID());
        dto.setSysPersonID(node.getSysPersonID());
        return dto;
    }

    public static List<NodeLimitDto> convert(List<NodeLimit> nodes) {
        List<NodeLimitDto> dtos = new ArrayList<NodeLimitDto>();
        for (NodeLimit node : nodes) {
            if (node != null) {
                dtos.add(convert(node));
            }
        }
        return dtos;
    }
}
