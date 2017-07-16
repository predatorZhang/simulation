package com.casic.simulation.patrol.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.patrol.domain.Patroler;
import com.casic.simulation.patrol.dto.PatrolerDto;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/28.
 */
@Service
public class PatrolerManager extends HibernateEntityDao<Patroler>{

    public List<PatrolerDto> allPatrolers() {
        List<PatrolerDto> result = new ArrayList<PatrolerDto>();
        Criteria criteria = createCriteria(Patroler.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.addOrder(Order.desc("id"));
        List<Patroler> patrolerList = criteria.list();
        for(Patroler patroler:patrolerList){
            result.add(new PatrolerDto(patroler));
        }
        return result;
    }

}
