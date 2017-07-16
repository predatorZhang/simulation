package com.casic.simulation.device.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.model.dto.UsefulDevice;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceManager extends HibernateEntityDao<Device> {

    private List<String> deviceTypeNames = Arrays.asList(
            UsefulDevice.FLOW.getName(),
            UsefulDevice.PRESS.getName()
    );

    public Criteria getCriteria() {
        return getSession().createCriteria(Device.class);
    }

    private String getHQL(String devCode,
                          String deviceTypeName,
                          Map<String, Object> paraMap) {
        StringBuilder hql = new StringBuilder(" from Device device ")
                .append(" where active = true ");
        if (StringUtils.isNotBlank(devCode)) {
            hql.append(" and device.devCode like :devCode ");
            paraMap.put("devCode", "%" + devCode + "%");
        }
        if (StringUtils.isNotBlank(deviceTypeName)) {
            hql.append(" and device.deviceType.typeName = :devTypeName ");
            paraMap.put("devTypeName", deviceTypeName);
        }
        if (!deviceTypeNames.isEmpty()) {
            hql.append(" and (");
            for (String typeName : deviceTypeNames) {
                hql.append("device.deviceType.typeName = '")
                        .append(typeName).append("' or ");
            }
            hql.append(" 1=2 ) ");
        }
        return hql.append(" order by device.deviceType.typeName,")
                .append("device.devCode asc ").toString();
    }

}