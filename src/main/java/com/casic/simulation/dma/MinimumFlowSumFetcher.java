package com.casic.simulation.dma;


import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.dmamanager.MinimumFlowSumManager;
import com.casic.simulation.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class MinimumFlowSumFetcher {

    @Resource
    public DmaLogManager dmaLogManager;

    @Resource
    public MinimumFlowSumManager minimumFlowSumManager;

    protected PropertiesUtil propUtil = new PropertiesUtil("application.properties");


    public MinimumFlowSum getFlowSumByRange(Date _start, Date _end, long regionId) {
        Date start=(Date)_start.clone();
        Date end=(Date)_end.clone();

        MinimumFlowSum minimumFlowSum=new MinimumFlowSum();
        minimumFlowSum.setRegionID((int)regionId);
        minimumFlowSum.setStart(_start);
        minimumFlowSum.setEnd(_end);
        try {
            List<MinimumFlow> minimumFlowList=minimumFlowSumManager.GetMinimumFlowSumByDateAndRegion(start, end, regionId);
            if (minimumFlowList == null || minimumFlowList.size() == 0) {
                minimumFlowSum.setOk(false);

                dmaLogManager.saveLog(regionId,true,"查询时间范围内无最小流量数据" +
                        _start.toLocaleString() + "至" + _end.toLocaleString());
                return minimumFlowSum;
            }
            minimumFlowSum.setOk(true);
            minimumFlowSum.setMinimumFlowList(minimumFlowList);
            minimumFlowSum.setMinFlowSum(getMinimumFlowFromTable(minimumFlowList));

        }catch(Exception ex){
            ex.printStackTrace();
            minimumFlowSum.setOk(false);

        }

        return minimumFlowSum;
    }

    private Double getMinimumFlowFromTable(List<MinimumFlow> minimumFlowList)
    {
        Double sumMinimumFlow = 0.0;
        try {
            for (MinimumFlow minimumFlow : minimumFlowList) {
                sumMinimumFlow += minimumFlow.getMinFlow();
            }
            return sumMinimumFlow * Double.parseDouble(propUtil.getProperty("dma.dayNightFactor"));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            dmaLogManager.save(ex);
            return 0.0;
        }
    }

}
