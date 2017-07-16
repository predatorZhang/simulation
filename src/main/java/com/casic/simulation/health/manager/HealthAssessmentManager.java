package com.casic.simulation.health.manager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.health.domain.HealthInfo;
import com.casic.simulation.health.dto.HealthInfoDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HealthAssessmentManager extends HibernateEntityDao<HealthInfo>
{
    public  List<HealthInfoDTO> findAllHealthInfo()
    {
        List<HealthInfo> healthInfos = getAll();
        List<HealthInfoDTO> healthInfoDTOs=null;
        if (healthInfos.size() != 0)
        {
            healthInfoDTOs = HealthInfoDTO.ConvertDTOs(healthInfos);
        }
        return healthInfoDTOs;
    }

    public Map queryHealthInfoList(int page,int rows,HealthInfoDTO healthInfoDTO){
        Map map = new HashMap<String,Object>();
        Criteria criteria = this.createCriteria(HealthInfo.class);
        criteria.addOrder(Order.desc("dbId"));
//        criteria.add(Restrictions.eq("active", true));
        if(StringUtils.isNotBlank(healthInfoDTO.getHealthRank())){
            criteria.add(Restrictions.eq("healthRank",healthInfoDTO.getHealthRank()));
        }
        if(StringUtils.isNotBlank(healthInfoDTO.getPipeId())){
            criteria.add(Restrictions.like("pipeId","%"+healthInfoDTO.getPipeId()+"%"));
        }
        if(StringUtils.isNotBlank(healthInfoDTO.getResult())){
            criteria.add(Restrictions.like("result","%"+healthInfoDTO.getResult()+"%"));
        }
        Page res = pagedQuery(criteria,page,rows);
        map.put("rows", HealthInfoDTO.ConvertDTOs((List<HealthInfo>)res.getResult()));
        map.put("total", res.getTotalCount());
        return map;
    }


    public HealthInfoDTO findHealthInfoById(Long id)
    {
        HealthInfoDTO healthInfoDTO=null;
        HealthInfo healthInfo = get(id);
        if (healthInfo != null)
        {
            healthInfoDTO = new HealthInfoDTO(healthInfo);
        }
        return healthInfoDTO;
    }

}
