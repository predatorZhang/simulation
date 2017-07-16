package com.casic.simulation.flow.dto;

import com.casic.simulation.flow.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/6/16.
 */
public class UserInfoDTO {

    private Long id;

    private String username;

    private String password;

    private String tel;

    private String address;

    private String descn;

    private Long depId;

    private String depName;

    private String selected;

    private Boolean passed;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public static UserInfoDTO ConverToDTO(UserInfo userInfo) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(userInfo.getId());
        dto.setUsername(userInfo.getUsername());
        dto.setPassword(userInfo.getPassword());
        dto.setTel(userInfo.getTel());
        dto.setAddress(userInfo.getAddress());
        dto.setDescn(userInfo.getDescn());
        dto.setStatus(userInfo.getStatus());
        dto.setPassed(userInfo.getPassed());
        if(userInfo.getDepartment() != null
                && userInfo.getDepartment().getStatus() == 1) {
            dto.setDepId(userInfo.getDepartment().getId());
            dto.setDepName(userInfo.getDepartment().getName());
        } else {
            dto.setDepId(-1l);
            dto.setDepName("无部门");
        }
        return dto;
    }

    public static List<UserInfoDTO> ConvertToDTO(List<UserInfo> list) {
        List<UserInfoDTO> dtos = new ArrayList<UserInfoDTO>();
        for(UserInfo userInfo : list) {
            if (userInfo != null) {
                dtos.add(ConverToDTO(userInfo));
            }
        }
        return dtos;
    }
}
