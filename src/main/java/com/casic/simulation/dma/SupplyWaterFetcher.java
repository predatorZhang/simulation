package com.casic.simulation.dma;


import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.dmamanager.SupplyWaterManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class SupplyWaterFetcher{

    @Resource
    public DmaLogManager dmaLogManager;

    @Resource
    public SupplyWaterManager supplyWaterManager;
/*
    public SupplyWaterFetcher(WebApplicationContext webApplicationContext) {
        dmaLogManager = (DmaLogManager) webApplicationContext.getBean("dmaLogManager");
        supplyWaterManager = (SupplyWaterManager) webApplicationContext.getBean("supplyWaterManager");
    }*/

    public  SupplyWater getAvgPressByDay(Date date,long regionId) {
        return null;
    }

    public  SupplyWater getAvgPressByMonth(Date date,long regionId) {
        return null;
    }

    public  SupplyWater geSupplyWaterByYear(Date date, long regionId) {
        return null;
    }

    public  SupplyWater getSupplyWaterByRange(Date start, Date end, int regionId) {

        SupplyWater supplyWater = null;
        try {
                supplyWater = supplyWaterManager.GetSupplyWaterByTimeRange(start, end, regionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplyWater;

    }

}