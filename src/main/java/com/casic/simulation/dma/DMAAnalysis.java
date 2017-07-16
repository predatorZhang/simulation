package com.casic.simulation.dma;

import com.casic.simulation.dma.model.domain.DMAInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class DMAAnalysis {

    @Resource
    public DmaInfoFetcher dmaInfoFetcher;

    @Resource
    public SupplyWaterFetcher supplyWaterFetcher;

    @Resource
    public MinimumFlowSumFetcher minimumFlowSumFetcher;

    @Resource
    public INormalWaterFetcher normalWaterFetcher;

    @Resource
    public IBackgroundLeakageFetcher backgroundLeakageFetcher;

    public DMAResult getLeakageRateByDay(Date day,int regionId) {
        return null;
    }

    public DMAResult getLeakgeRateByMonth(Date date,int regionId) {
        return null;
    }

    public DMAResult getLeakageRateByYear(Date date, int regionId) {
        return null;
    }

    public DMAResult getLeakageRateByRange(Date start, Date end, int regionId) {
        DMAResult result = new DMAResult();

        DMAInfo dmaInfo = dmaInfoFetcher.getDmaInfo(regionId);

        //http://localhost:8080/alarm/alarm/dma-analysis!getLeakageRateByRange.do
        //总供水量 netdata 检查是否缺少数据，缺少数据存日志
        //供水量返回前要判断是否大于0
        SupplyWater supplyWater = supplyWaterFetcher.getSupplyWaterByRange(start, end, regionId);
        if (!supplyWater.isOk()) {
            result.setCode(1);
            result.setErrorMsg("计算供水量出错，请查看数据库日志");
        }

        //夜间最小流量之和
        MinimumFlowSum flowSum = minimumFlowSumFetcher.getFlowSumByRange(start, end, regionId);
        if (!flowSum.isOk()) {
            result.setCode(2);
            result.setErrorMsg("计算最小流量出错，请查看数据库日志" + start.toLocaleString() +
                    "至" + end.toString());
/*
            return result;
*/
        }

        //正常夜间用水量之和
        NormalWater normalWater = normalWaterFetcher.getNormalWater(dmaInfo, start, end);
        if (!normalWater.isOk()) {
            result.setCode(3);
            result.setErrorMsg("计算正常夜间用水量出错，请查看数据库日志");
            return result;
        }

        //背景漏失量之和
        BackgroundLeakageSum backgroundLeakageSum =backgroundLeakageFetcher.
                getBackgroundLeakage(dmaInfo, flowSum, start, end);
        if (!backgroundLeakageSum.isOk()) {
            result.setCode(4);
            result.setErrorMsg("计算背景漏失量出错，请查看数据库日志");
            return result;
        }

        double leakRate = getLeakage(supplyWater.getWaterQuantity(), flowSum.getMinFlowSum(), normalWater.getNormalWaterSum(),
                backgroundLeakageSum.getBackgroundLeakageSum());
        result.setLeakageRate(leakRate);

        return result;
    }

    private double getLeakage(double supplyWater, double flowSum, double normalWater,
                              double backgroundLeakageSum) {
        double leakRate = (flowSum - normalWater - backgroundLeakageSum) / supplyWater * 100;//100%
        return leakRate;
    }

}