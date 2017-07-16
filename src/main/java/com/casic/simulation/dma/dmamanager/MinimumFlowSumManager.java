package com.casic.simulation.dma.dmamanager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.dma.MinimumFlow;
import com.casic.simulation.dma.MinimumFlowSum;
import com.casic.simulation.dma.model.domain.DmaMinimumFlow;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yxw on 2017/2/23.
 */
@Service
public class MinimumFlowSumManager extends HibernateEntityDao<MinimumFlowSum> {

    @Resource
    public DmaDeviceManager dmaDeviceManager;

    public List<MinimumFlow> GetMinimumFlowSumByDateAndRegion(Date start, Date end, long regionId) {
        List<MinimumFlow> minimumFlows = new ArrayList<MinimumFlow>();

        Session session=this.getSession();
        Criteria criteria = session.createCriteria(DmaMinimumFlow.class);

        criteria.add(Restrictions.eq("dmaInfo",regionId));
        criteria.add(Restrictions.ge("minFlowTime", start));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DATE, 1);
        criteria.add(Restrictions.le("minFlowTime", calendar.getTime()));

        List<DmaMinimumFlow> dmaMinimumFlows=criteria.list();

        for(DmaMinimumFlow dmaMinimumFlow:dmaMinimumFlows)
        {
            MinimumFlow minimumFlow=new MinimumFlow();
            minimumFlow.setRegionID((int)regionId);
            minimumFlow.setMinFlowTime(dmaMinimumFlow.getMinFlowTime());
            //TODO LIST：如果当天没有最小流量的话，直接后续相加会导致什么？
            minimumFlow.setMinFlow(Double.parseDouble(dmaMinimumFlow.getMinFlowValue()));
            minimumFlow.setOk(true);
            minimumFlows.add(minimumFlow);
        }

        return minimumFlows;
    }

}
