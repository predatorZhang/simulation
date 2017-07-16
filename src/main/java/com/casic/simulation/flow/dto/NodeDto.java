package com.casic.simulation.flow.dto;

import com.casic.simulation.flow.domain.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */
public class NodeDto {

    private Long id;

    /** 节点名称 */
    private String nodeName;

    /** 节点操作对应的系统名称 */
    private String sysName;

    /** 节点操作对应的系统代号 */
    private String sysCode;

    /** 节点描述 */
    private String descn;

    /** 选中 */
    private boolean selected = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static NodeDto convert(Node node) {
        NodeDto dto = new NodeDto();
        dto.setNodeName(node.getNodeName());
        dto.setId(node.getId());
        dto.setDescn(node.getDescn());
        dto.setSysCode(node.getSysCode());
        dto.setSysName(node.getSysName());
        return dto;
    }

    public static List<NodeDto> convert(List<Node> nodes) {
        List<NodeDto> dtos = new ArrayList<NodeDto>();
        for (Node node : nodes) {
            if (node != null) {
                dtos.add(convert(node));
            }
        }
        return dtos;
    }
}
