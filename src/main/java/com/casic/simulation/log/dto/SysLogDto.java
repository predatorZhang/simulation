package com.casic.simulation.log.dto;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.log.domain.SysLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/5/9.
 */
public class SysLogDto {
    private Long id;
    private String businessName;
    private String operationType;
    private String content;
    private String createUser;
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static SysLogDto Convert2SysLogDto(SysLog sysLog){
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setId(sysLog.getId());
        sysLogDto.setBusinessName(sysLog.getBusinessName());
        sysLogDto.setContent(sysLog.getContent());
        sysLogDto.setCreateTime(DateUtils.sdf4.format(sysLog.getCreateTime()));
        sysLogDto.setCreateUser(sysLog.getCreateUser());
        sysLogDto.setOperationType(sysLog.getOperationType());
        return sysLogDto;
    }

    public static List<SysLogDto> Convert2SysLogDtos(List<SysLog> sysLogList){
        List<SysLogDto> sysLogDtos = new ArrayList<SysLogDto>();
        for(SysLog sysLog:sysLogList){
            sysLogDtos.add(Convert2SysLogDto(sysLog));
        }
        return sysLogDtos;
    }
}
