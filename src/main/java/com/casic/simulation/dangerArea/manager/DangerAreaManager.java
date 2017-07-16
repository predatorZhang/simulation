package com.casic.simulation.dangerArea.manager;


import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.IoUtils;
import com.casic.simulation.core.util.ServletUtils;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.dangerArea.domain.DangerArea;
import com.casic.simulation.dangerArea.dto.DangerAreaDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DangerAreaManager extends HibernateEntityDao<DangerArea> {

    private StoreConnector storeConnector;

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }
    public Criteria getCriteria(){
        return getSession().createCriteria(DangerArea.class);
    }

    public Map queryDangerousArea(int page,int rows,DangerAreaDTO dangerAreaDTO){
        Map map = new HashMap<String,Object>();
        Criteria criteria = getCriteria();
        criteria.addOrder(Order.desc("dbId"));
        criteria.add(Restrictions.eq("active", true));
        if(StringUtils.isNotBlank(dangerAreaDTO.getAreaName())){
            criteria.add(Restrictions.like("areaName","%"+dangerAreaDTO.getAreaName()+"%"));
        }
        if(StringUtils.isNotBlank(dangerAreaDTO.getAreaGrade())){
            criteria.add(Restrictions.eq("areaGrade",dangerAreaDTO.getAreaGrade()));
        }
        if(StringUtils.isNotBlank(dangerAreaDTO.getDescription())){
            criteria.add(Restrictions.like("description","%"+dangerAreaDTO.getDescription()+"%"));
        }
        Page res = pagedQuery(criteria,page,rows);
        map.put("rows", DangerAreaDTO.ConvertDTOs((List<DangerArea>)res.getResult()));
        map.put("total", res.getTotalCount());
        return map;
    }
    public void downloadStrategyFile(Long id, HttpServletResponse response) throws Exception {
        InputStream is = null;
        try
        {
            DangerArea dangerArea = get(id);
            String filePath = dangerArea.getFilePath();
            String pix = filePath.substring(filePath.indexOf("."));
            ServletUtils.setFileDownloadHeader(response, dangerArea.getFileName());
            is = storeConnector.get("DangerrousArea",filePath).getResource().getInputStream();
            IoUtils.copyStream(is, response.getOutputStream());
        }finally {
            if(is != null)
            {
                is.close();
            }
        }
    }

    public DangerAreaDTO findDangerAreaDTOById(Long id){
        return new DangerAreaDTO(get(id));
    }

    public boolean isExist(String areaName)
    {
        Map<String,Object> map = new HashMap<String, Object>();
        int count = this.getCountByAreaName(areaName);
        if(count>0){
            return true;
        }
        return false;
    }

    public int getCountByAreaName(String areaName)
    {
        Criteria criteria = getSession().createCriteria(DangerArea.class);
        criteria.add(Restrictions.eq("areaName", areaName));
        criteria.add(Restrictions.eq("active", true));
        return getCount(criteria);
    }
}
