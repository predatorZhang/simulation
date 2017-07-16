package com.casic.simulation.dangerSource.manager;


import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.IoUtils;
import com.casic.simulation.core.util.ServletUtils;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.dangerSource.domain.DangerSource;
import com.casic.simulation.dangerSource.dto.DangerSourceDTO;
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
public class DangerSourceManager extends HibernateEntityDao<DangerSource> {

    private StoreConnector storeConnector;

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }

    public Criteria getCriteria(){
        return getSession().createCriteria(DangerSource.class);
    }


    public Map queryDangerousPoints(int page,int rows,DangerSourceDTO dangerSourceDTO){
        Map map = new HashMap<String,Object>();
        Criteria criteria = getCriteria();
        criteria.addOrder(Order.desc("dbId"));
        criteria.add(Restrictions.eq("active", true));
        if(StringUtils.isNotBlank(dangerSourceDTO.getSourceName())){
            criteria.add(Restrictions.like("sourceName","%"+dangerSourceDTO.getSourceName()+"%"));
        }
        if(StringUtils.isNotBlank(dangerSourceDTO.getSourceGrade())){
            criteria.add(Restrictions.eq("sourceGrade",dangerSourceDTO.getSourceGrade()));
        }
        if(StringUtils.isNotBlank(dangerSourceDTO.getDescription())){
            criteria.add(Restrictions.like("description","%"+dangerSourceDTO.getDescription()+"%"));
        }
        if(StringUtils.isNotBlank(dangerSourceDTO.getErrorMode())){
            criteria.add(Restrictions.eq("errorMode",dangerSourceDTO.getErrorMode()));
        }
        Page res = pagedQuery(criteria,page,rows);
        map.put("rows", dangerSourceDTO.ConvertDTOs((List<DangerSource>)res.getResult()));
        map.put("total", res.getTotalCount());
        return map;
    }
    public void downloadStrategyFile(Long id, HttpServletResponse response) throws Exception {
        InputStream is = null;
        try
        {
            DangerSource dangerSource = get(id);
            String filePath = dangerSource.getFilePath();
//            String pix = filePath.substring(filePath.indexOf("."));
            ServletUtils.setFileDownloadHeader(response, dangerSource.getFileName());
            is = storeConnector.get("DangerrousSource",filePath).getResource().getInputStream();
            IoUtils.copyStream(is, response.getOutputStream());
        }
        finally {
            if(is != null)
            {
                is.close();
            }
        }
    }
    public DangerSourceDTO findDangerSourceDTOById(Long id){
        return new DangerSourceDTO(get(id));
    }

    public boolean isExist(String sourceName)
    {
        int count = this.getCountBySourceName(sourceName);
        if(count>0){
            return true;
        }
        return false;
    }

    public int getCountBySourceName(String sourceName)
    {
        Criteria criteria = getSession().createCriteria(DangerSource.class);
        criteria.add(Restrictions.eq("sourceName", sourceName));
        criteria.add(Restrictions.eq("active", true));
        return getCount(criteria);
    }
}










