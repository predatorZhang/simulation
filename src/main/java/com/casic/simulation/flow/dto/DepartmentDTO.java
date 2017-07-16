package com.casic.simulation.flow.dto;

import com.casic.simulation.flow.domain.Department;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/6/16.
 */
public class DepartmentDTO {

    private Long id;

    private String name;

    private String code;

    private boolean selected = false;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DepartmentDTO() {}

    public static DepartmentDTO ConvertDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setCode(department.getCode());
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    public static List<DepartmentDTO> ConvertDTOs(List<Department> departments) {
        List<DepartmentDTO> dtos = new ArrayList<DepartmentDTO>();
        for (Department department : departments) {
            if (department != null) {
                dtos.add(ConvertDTO(department));
            }
        }
        return dtos;
    }
}
