package com.casic.simulation.dma.model.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.DMASaleWater;
import com.casic.simulation.dma.model.dto.DMASaleWaterForm;
import com.casic.simulation.dma.model.json.DMASaleWaterJSON;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class DMASaleWaterManager extends HibernateEntityDao<DMASaleWater> {

    @Resource
    private DMAInfoManager dmaInfoManager;

    public Criteria getCriteria() {
        return getSession().createCriteria(DMASaleWater.class);
    }

    /**
     * 获取子区域内所有有效售水信息
     * @param dmaID
     * @param page
     * @param rows
     * @return
     */
    public Map<String, Object> pagedQueryJson(Long dmaID, int page, int rows) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("dmaInfo", dmaInfoManager.get(dmaID)));
        criteria.addOrder(Order.desc("id"));

        Page pg = pagedQuery(criteria,page,rows);

        List<DMASaleWaterJSON> dmaSaleWaterJSONList = new ArrayList<DMASaleWaterJSON>();
        List<DMASaleWater> saleWaterList = (List<DMASaleWater>) pg.getResult();
        for (DMASaleWater dmaSaleWater : saleWaterList) {
            dmaSaleWaterJSONList.add(new DMASaleWaterJSON(dmaSaleWater));
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total", pg.getTotalCount());
        map.put("rows", dmaSaleWaterJSONList);
        return map;
    }

    /**
     * 删除子区域关联的售水信息
     * @param dmaInfo
     */
    @Transactional
    public void removeByDma(DMAInfo dmaInfo) {
        String hql = "update DMASaleWater set active=:active where dmaInfo=:dma";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("active",Boolean.FALSE);
        map.put("dma",dmaInfo);
        batchUpdate(hql, map);
    }

    public List<DMASaleWater> getSaleWaterListByDMA(DMAInfo dmaInfo) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        String hql = " from DMASaleWater " +
                " where active = :active " +
                " and dmaInfo.id = :dmaId ";
        paraMap.put("dmaId", dmaInfo.getID());
        paraMap.put("active",Boolean.TRUE);
        List<DMASaleWater> dmaSaleWaterList = (List<DMASaleWater>) this.
                createQuery(hql, paraMap).list();
        return dmaSaleWaterList;
    }

    public Map<String, Object> removeModel(DMASaleWater model) {
        Map<String,Object> map = new HashMap<String, Object>();
        DMASaleWater saleWater = get(model.getId());
        if(saleWater!=null){
            saleWater.setActive(false);
            save(saleWater);
        }
        map.put("success",true);
        map.put("msg","售水量删除成功！");
        return map;
    }

    /**
     * 删除某一条售水信息
     * @param saleWaterID
     * @return
     */
    public Map<String, Object> removeModel(Long saleWaterID) {
        Map<String,Object> map = new HashMap<String, Object>();
        DMASaleWater saleWater = get(saleWaterID);
        if(null != saleWater){
            saleWater.setActive(false);
            save(saleWater);
        }
        map.put("success",true);
        map.put("msg","售水量删除成功！");
        return map;
    }



    /**
     * 增加一条售水信息
     * @param form
     * @return
     * @throws ParseException
     */
    public Map<String, Object> addSaleWater(DMASaleWaterForm form) throws ParseException {
        DMASaleWater model = new DMASaleWater();
        model.setStartDate(DateUtils.sdf1.parse(form.getBeginDate()));
        model.setEndDate(DateUtils.sdf1.parse(form.getEndDate()));
        model.setSaleWater(Double.parseDouble(form.getWater()));
        model.setNoValueWater(Double.parseDouble(form.getNoValueWater()));
        model.setInsertDate(new Date());
        model.setUpdateDate(new Date());
        model.setActive(Boolean.TRUE);
        DMAInfo dmaInfo = dmaInfoManager.getDMAByID(form.getDmaID());
        model.setDmaInfo(dmaInfo);
        save(model);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","添加成功！");
        return map;
    }

}
