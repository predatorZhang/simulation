package com.casic.simulation.dma;


import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.model.domain.DMAInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class DmaInfoFetcher {

    @Resource
    public DmaLogManager dmaLogManager;


    public DMAInfo getDmaInfo(long regionId) {
        try {
            Map<String, Object> paraMap = new HashMap<String, Object>();
            String hql = " from DMAInfo dmaInfo " +
                         " where active = 1 " +
                         " and dmaInfo.ID = :dmaId ";
            paraMap.put("dmaId", regionId);
            //List<DMAInfo> dmaInfoList = (List<DMAInfo>) dmaLogManager.createQuery(hql, paraMap).list();
            List<DMAInfo> dmaInfoList = dmaLogManager.getDmaByID(hql, paraMap);
            if(dmaInfoList.size() == 1) {
                return dmaInfoList.get(0);
            } else if(dmaInfoList.size() == 0) {
                dmaLogManager.saveLog(0, false, "获取分区信息出错，分区ID:" + regionId + "," + getClass().getName());
                return null;
            } else {
                dmaLogManager.saveLog(0, false, "分区ID重复，返回多个结果，分区ID:" + regionId + "," + getClass().getName());
                return null;
            }
        } catch (Exception e) {
            dmaLogManager.saveLog(0, false, "获取分区信息出错," + getClass().getName() + "," + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}