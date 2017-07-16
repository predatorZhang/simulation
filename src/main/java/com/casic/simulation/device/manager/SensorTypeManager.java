package com.casic.simulation.device.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.device.domain.SensorType;
import com.casic.simulation.log.manager.SysLogManager;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SensorTypeManager extends HibernateEntityDao<SensorType> {

    @Resource
    private SysLogManager sysLogManager;

    public Criteria getCriteria(){
        return getSession().createCriteria(SensorType.class);
    }

}
