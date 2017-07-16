package com.casic.simulation.dma.model.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.PosDMA;
import com.casic.simulation.dma.model.domain.PositionInfo;
import com.casic.simulation.dma.model.dto.PositionForm;
import com.casic.simulation.dma.model.json.PositionInfoJSON;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PosDMAManager extends HibernateEntityDao<PosDMA> {

    @Resource
    private PositionInfoManager positionInfoManager;

    @Resource
    private DMAInfoManager dmaInfoManager;

    public Criteria getCriteria() {
        return getSession().createCriteria(PosDMA.class);
    }

    /**
     * 获取DMA子分区区域下的所有监测点
     * @param dmaInfoID
     * @param pageNum
     * @param rows
     * @return
     */
    public Map<String, Object> findPositionDTOByDma(Long dmaInfoID, int pageNum, int rows) {
        Criteria criteria = getCriteria();
        criteria.setProjection(Projections.property("positionInfo"));
        criteria.add(Restrictions.eq("active", true));
        criteria.createAlias("dmaInfo", "dmaInfo");
        criteria.add(Restrictions.eq("dmaInfo.ID", dmaInfoID));
        criteria.createAlias("positionInfo", "positionInfo");
        criteria.add(Restrictions.eq("positionInfo.active", true));
        criteria.addOrder(Order.asc("positionInfo.id"));
        Page page = pagedQuery(criteria, pageNum, rows);
        List<PositionInfoJSON> jsons = new ArrayList<PositionInfoJSON>();
        List<PositionInfo> pos = (List<PositionInfo>) page.getResult();
        for (PositionInfo p : pos) {
            jsons.add(new PositionInfoJSON(p));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getTotalCount());
        map.put("rows", jsons);
        return map;
    }

    /**
     * 删除子区域绑定的监测点{@link com.casic.simulation.dma.model.domain.PosDMA}
     * @param dmaInfo
     */
    @Transactional
    public void removeByDma(DMAInfo dmaInfo) {
        String hql = "delete from PosDMA posDma where dmaInfo=:dma";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dma", dmaInfo);
        batchUpdate(hql, map);
    }

    /**
     * 增加新的监测点PositionInfo&PosDMA信息
     * @param model
     * @return
     */
    public Map<String, Object> addNewPosition(PositionForm model) {
        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setActive(true);
        positionInfo.setBDataPosType(model.getBDataPosType());
        positionInfo.setComment(model.getComment());
        positionInfo.setIsUse(model.getIsUse());
        positionInfo.setLatitude(model.getLatitude());
        positionInfo.setLongitude(model.getLongitude());
        positionInfo.setName(model.getName());
        positionInfo.setOperator(model.getOperator());
        positionInfo.setOperateTime(new Date());
        positionInfo.setSortCode(model.getSortCode());
        positionInfoManager.save(positionInfo);

        DMAInfo dmaInfo = dmaInfoManager.get(model.getDmaID());

        PosDMA posDma = new PosDMA();
        posDma.setDmaInfo(dmaInfo);
        posDma.setPositionInfo(positionInfo);
        posDma.setDirection(model.getDirection());
        posDma.setActive(true);
        save(posDma);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","新建成功！");
        return map;
    }

    /**
     * 关联已有监测点
     * @param model
     * @return
     */
    public Map<String, Object> appendPosition(PositionForm model) {
        Map<String,Object> map = new HashMap<String, Object>();
        PositionInfo pos = positionInfoManager.get(model.getID());
        DMAInfo dmaInfo = dmaInfoManager.getDMAByID(model.getDmaID());
        if (pos == null || dmaInfo == null) {
            map.put("success", false);
            map.put("msg", "监测点或分区已删除！");
            return map;
        }

        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("dmaInfo", dmaInfo));
        criteria.add(Restrictions.eq("positionInfo", pos));
        PosDMA pd = (PosDMA) criteria.setMaxResults(1).uniqueResult();
        if(null == pd) {
            pd = new PosDMA();
            pd.setDmaInfo(dmaInfo);
            pd.setActive(true);
            pd.setPositionInfo(pos);
        }
        pd.setDirection(model.getDirection());
        save(pd);
        map.put("success",true);
        map.put("msg","添加成功！");
        return map;
    }
}
