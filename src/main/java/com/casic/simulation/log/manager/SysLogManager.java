package com.casic.simulation.log.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.log.domain.SysLog;
import com.casic.simulation.log.dto.SysLogDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysLogManager extends HibernateEntityDao<SysLog> {

    public void saveSysLog(String businessName,
                           String operationType,
                           String content,String userName) {
        String personName = "NULL";
        if(userName!=null) personName = userName;
        SysLog sysLog = new SysLog();
        sysLog.setBusinessName(businessName);
        sysLog.setOperationType(operationType);
        sysLog.setContent(content);
        sysLog.setCreateUser(personName);
        sysLog.setCreateTime(new Date());
        save(sysLog);
    }

    public  Map<String, Object> pageQuerySysLogList(
            int page,int rows, String createUser,
            String beginDate, String endDate){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        StringBuilder hql = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        hql.append("from SysLog where 1=1");
        if (StringUtils.isNotBlank(beginDate)) {
            hql.append(" and to_char(createTime,'yyyy-mm-dd')>=:beg ");
            map.put("beg", beginDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            hql.append(" and to_char(createTime,'yyyy-mm-dd')<=:end ");
            map.put("end", endDate);
        }
        if (StringUtils.isNotBlank(createUser)) {
            hql.append(" and createUser like :createUser");
            map.put("createUser", "%" + createUser + "%");
        }
        hql.append(" order by createTime desc ");
        Page p = pagedQuery(hql.toString(), page, rows, map);
        List<SysLog> sysLogs =(List<SysLog>) p.getResult();
        List<SysLogDto> sysLogDtos = SysLogDto.Convert2SysLogDtos(sysLogs);
        resultMap.put("rows", sysLogDtos);
        resultMap.put("total", p.getTotalCount());
        return resultMap;
    }
}
