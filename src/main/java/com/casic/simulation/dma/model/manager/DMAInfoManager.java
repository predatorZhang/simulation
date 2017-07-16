package com.casic.simulation.dma.model.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.dma.model.domain.DMAArea;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.PosDMA;
import com.casic.simulation.dma.model.domain.PositionInfo;
import com.casic.simulation.dma.model.dto.WaterPipelineRegionForm;
import com.casic.simulation.dma.model.json.WaterPipelineRegionDataJSON;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DMAInfoManager extends HibernateEntityDao<DMAInfo> {

    @Resource
    private PosDMAManager posDMAManager;

    @Resource
    private DevPosManager devPosManager;

    @Resource
    private DMAAreaManager dmaAreaManager;

    @Resource
    private DMASaleWaterManager dmaSaleWaterManager;

    public Criteria getCriteria() {
        return getSession().createCriteria(DMAInfo.class);
    }

    /**
     * DMA模型管理
     * 获取DMA模型分区结构，要求分区active===true
     * @return
     */
    public List<DMAInfo> findAllActiveDmaInfo() {
        Criteria criteria = getSession().createCriteria(DMAInfo.class);
        criteria.addOrder(Order.desc("id"));
        criteria.add(Restrictions.eq("active", true));
        return criteria.list();
    }

    /**
     * 根据父区域NO.获取子分区信息
     * @param parentNo
     * @param pageNum
     * @param rows
     * @return
     */
    public Map<String, Object> querySubDMAByID(Long parentNo, int pageNum, int rows) {
        Criteria criteria = getSession().createCriteria(DMAInfo.class);
        criteria.addOrder(Order.desc("ID"));
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("BDataParent_DMA", parentNo.toString()));
        Page page = pagedQuery(criteria, pageNum, rows);
        List<DMAInfo> dmaList = (List<DMAInfo>) page.getResult();
        List<WaterPipelineRegionDataJSON> jsons = new ArrayList<WaterPipelineRegionDataJSON>();
        for (DMAInfo dmaInfo : dmaList) {
            jsons.add(new WaterPipelineRegionDataJSON(dmaInfo));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getTotalCount());
        map.put("rows", jsons);
        return map;
    }

    /**
     * 根据DMA分区NO获取DMAInfo
     * @param dmaid
     * @return
     */
    public DMAInfo getDMAByID(Long dmaid) {
        Criteria criteria = getSession().createCriteria(DMAInfo.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("ID", dmaid));
        return (DMAInfo)criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * 删除分区
     * @param dmaID
     * @return
     */
    @Transactional
    public Map<String, Object> removeModel(Long dmaID) {
        Map<String, Object> map = new HashMap<String, Object>();
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("ID", dmaID));
        DMAInfo dmaInfo = (DMAInfo)criteria.setMaxResults(1).uniqueResult();
        if (hasChildren(dmaInfo)) {
            map.put("success", false);
            map.put("msg", "请先删除子分区！");
            return map;
        }
        if(null != dmaInfo){
            for (PosDMA pos : dmaInfo.getPosInDmaList()) {
                deletePosInfo(pos.getPositionInfo());
            }
            dmaAreaManager.removeByDma(dmaInfo);
            dmaSaleWaterManager.removeByDma(dmaInfo);
            dmaInfo.setActive(false);
            save(dmaInfo);
        }
        map.put("success",true);
        map.put("msg","删除成功！");
        return map;
    }

    private boolean hasChildren(DMAInfo dmaInfo) {
        if (null != dmaInfo) {
            Criteria criteria = getCriteria();
            criteria.add(Restrictions.eq("active", true));
            criteria.add(Restrictions.eq("BDataParent_DMA", dmaInfo.getID().toString()));
            if (criteria.list().size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    private void deletePosInfo(PositionInfo positionInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        String hql = "delete from PosDMA where positionInfo=:pos";
        map.put("pos", positionInfo);
        posDMAManager.batchUpdate(hql, map);
    }

    /**
     * 增加分区信息
     * @param model
     * @return
     */
    @Transactional
    public Map<String, Object> saveModel(WaterPipelineRegionForm model) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (isExist(model)) {
            map.put("success", false);
            map.put("msg", "分区编号或分区编码重复！");
            return map;
        }
        DMAInfo dma = new DMAInfo();
        dma.setName(model.getName());
        dma.setNO(model.getNo());
        dma.setBDataParent_DMA(model.getBDataParent_DMA());
        dma.setIcf(model.getIcf());
        dma.setLeakControlRate(model.getLeakControlRate());
        dma.setNormalWater(model.getNormalWater());
        dma.setPipeLeng(model.getPipeLeng());
        dma.setPipeLinks(model.getPipeLinks());
        dma.setSaleWater(model.getSaleWater());
        dma.setUserCount(model.getUserCount());
        dma.setUserPipeLeng(model.getUserPipeLeng());
        save(dma);

        DMAArea dmaArea = new DMAArea();
        dmaArea.setActive(true);
        dmaArea.setDmaInfo(dma);
        dmaArea.setRegionErea(model.getRegion());
        dmaAreaManager.save(dmaArea);
        map.put("success", true);
        map.put("msg", "保存成功！");
        return map;
    }

    public boolean isExist(WaterPipelineRegionForm model) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.or(Restrictions.eq("NO", model.getNo()), Restrictions.eq("name", model.getName())));
        if (criteria.list().size() > 0) {
            return true;
        }
        return false;
    }

}
