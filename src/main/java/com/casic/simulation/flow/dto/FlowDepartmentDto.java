package com.casic.simulation.flow.dto;

import com.casic.simulation.flow.domain.FlowDepartment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/6/16.
 */
public class FlowDepartmentDto {

    private Long id;

    /** 部门名称 */
    private String depName;

    /** 部门描述 */
    private String descn;

    /** 部门是否有效 */
    private Boolean active;

    /** 对应系统部门ID */
    private Long sysDepID;

    private boolean selected = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getSysDepID() {
        return sysDepID;
    }

    public void setSysDepID(Long sysDepID) {
        this.sysDepID = sysDepID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public FlowDepartmentDto() {}

    public static FlowDepartmentDto ConvertDTO(FlowDepartment department) {
        FlowDepartmentDto dto = new FlowDepartmentDto();
        dto.setActive(department.getActive());
        dto.setDepName(department.getDepName());
        dto.setDescn(department.getDescn());
        dto.setId(department.getId());
        dto.setSysDepID(department.getSysDepID());
        return dto;
    }

    public static List<FlowDepartmentDto> ConvertDTOs(List<FlowDepartment> departments) {
        List<FlowDepartmentDto> dtos = new ArrayList<FlowDepartmentDto>();
        for (FlowDepartment department : departments) {
            if (department != null) {
                dtos.add(ConvertDTO(department));
            }
        }
        return dtos;
    }
}
