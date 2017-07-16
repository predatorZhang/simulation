package com.casic.simulation.dma.model.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.dma.model.domain.PositionInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PositionInfoManager extends HibernateEntityDao<PositionInfo> {

    @Resource
    private PosDMAManager posDMAManager;

    @Resource
    private DevPosManager devPosManager;

    public Criteria getCriteria() {
        return getSession().createCriteria(PositionInfo.class);
    }

    /**
     * DMA模型管理
     * 获取已有的有效监测点，并且这些监测点不在数组中[ids]
     * @param ids
     * @return
     */
    public List<PositionInfo> findActivePositionInfoAndNotIn(Set<Long> ids) {
        Criteria criteria = getSession().createCriteria(PositionInfo.class);
        criteria.addOrder(Order.asc("ID"));
        criteria.add(Restrictions.eq("active", true));
        if (!ids.isEmpty()) {
            criteria.add(Restrictions.not(Restrictions.in("ID", ids)));
        }
        return criteria.list();
    }

    /**
     * 删除与监测点关联的信息，包括PositionInfo、PosDMA、DevPos
     * 二院系统采用#软删除#
     * @param positionInfoID
     * @return
     */
    @Transactional
    public Map<String, Object> removeModel(Long positionInfoID) {
        PositionInfo positionInfo = get(positionInfoID);
        Map<String, Object> map = new HashMap<String, Object>();

        String hql = "delete from PosDMA where positionInfo=:pos";
        map.put("pos", positionInfo);
        posDMAManager.batchUpdate(hql, map);

        hql = "delete from DevPos where positionInfo=:pos";
        devPosManager.batchUpdate(hql, map);
        remove(positionInfo);

        map.clear();
        map.put("success", true);
        map.put("msg", "删除成功！");

        return map;
    }
}
