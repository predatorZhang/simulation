package com.casic.simulation.dma.model.manager;

/**
 * Created by Administrator on 2015/8/30.
 */

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.dma.model.domain.DMAArea;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.dto.PointDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DMAAreaManager extends HibernateEntityDao<DMAArea> {

    /**
     * 删除子区域的区域图形信息
     * @param dmaInfo
     */
    @Transactional
    public void removeByDma(DMAInfo dmaInfo) {
        String hql = "delete from DMAArea where dmaInfo=:dma";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("dma",dmaInfo);
        batchUpdate(hql,map);
    }

    /**
     * 获取子区域的区域图形信息
     * @param dmaId
     * @return
     */
    public List<PointDTO> findPointsOfArea(Long dmaId) {
        Criteria criteria = getSession().createCriteria(DMAArea.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.createAlias("dmaInfo","dmaInfo");
        criteria.add(Restrictions.eq("dmaInfo.ID",dmaId));
        DMAArea dmaArea = (DMAArea) criteria.setMaxResults(1).uniqueResult();
        List<PointDTO> dtos = new ArrayList<PointDTO>();
        if(null != dmaArea && StringUtils.isNotBlank(dmaArea.getRegionErea())){
            String[] area = dmaArea.getRegionErea().split("-");
            for(String point : area){
                String[] xy = point.split(":");
                if(xy.length == 2){
                    dtos.add(new PointDTO(
                            Double.parseDouble(xy[0]),
                            Double.parseDouble(xy[1])
                    ));
                }
            }
        }
        return dtos;
    }
}
