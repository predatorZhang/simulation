package com.casic.simulation.dma.dmamanager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.DevPos;
import com.casic.simulation.dma.model.domain.PosDMA;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yxw on 2017/2/23.
 */
@Service
public class DmaDeviceManager extends HibernateEntityDao<DMAInfo> {

    @Transactional
    public Map getDevByRegionIdAndType(int regionId, String sensorType) {
        Map map = new HashMap<Device, String>();
        String hql = "from PosDMA where dmaInfo.ID =" + regionId +
                " and active = true ";
        List<PosDMA> posDmas=this.getSession().createQuery(hql).list();

        //for each postion
        for (PosDMA posDMA : posDmas) {
            Criteria criteria = this.getSession().createCriteria(DevPos.class);
            criteria.add(Restrictions.eq("active", true));
            criteria.add(Restrictions.eq("sensorType", sensorType));
            criteria.createAlias("positionInfo", "positionInfo");
            criteria.add(Restrictions.eq("positionInfo.ID", posDMA.getPositionInfo().getID()));
            List<DevPos> devPosList=criteria.list();
            if (devPosList != null && devPosList.size() > 0) {
                map.put(devPosList.get(0).getDevice(), posDMA.getDirection());
            }
        }
        return map;
    }

    public DMAInfo getDMAInfoByDBID(double dbId)
    {
        Criteria criteria = this.getSession().createCriteria(DMAInfo.class);
        criteria.add(Restrictions.eq("ID", Math.round(dbId)));
        List<DMAInfo> DMAInfos=criteria.list();
        return DMAInfos.get(0);
    }


}
