package com.casic.simulation.dma;

import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.DMASaleWater;
import com.casic.simulation.dma.model.manager.DMASaleWaterManager;
import com.casic.simulation.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
//一种正常夜间用水量计算方式
@Service
public class NormalWaterFetcherImp implements INormalWaterFetcher {
    @Resource
    public DMASaleWaterManager dmaSaleWaterManager;

    @Resource
    public DmaLogManager dmaLogManager;
    protected PropertiesUtil propUtil = new PropertiesUtil("application.properties");

   /* public NormalWaterFetcherImp(WebApplicationContext webApplicationContext) {
        dmaLogManager = (DmaLogManager) webApplicationContext.getBean("dmaLogManager");
        dmaSaleWaterManager = (DMASaleWaterManager) webApplicationContext.getBean("DMASaleWaterManager");
    }*/

    //计算这段时间内的正常夜间用水总量，单位m3
    //公式：夜间正常用水量(m3/h)  = 夜间用水人数比例×户数×户均用水量(m3/h)
    @Override
    public NormalWater getNormalWater(DMAInfo dmaInfo, Date start, Date end) {
        NormalWater normalWater = new NormalWater();
        try {
            double userRate = 0.06;//夜间用水人数比例
            double familyNum = dmaInfo.getUserCount();//户数
            double quanPerFamily = getQuanPerFamily(dmaInfo, start, end);
            double dayNightFactor = Double.parseDouble(propUtil.getProperty("dma.dayNightFactor"));
            double normalWaterValue = userRate * familyNum * quanPerFamily * dayNightFactor;
            int range = DateUtils.getDateDiff(start, end);

            normalWater.setRegionID(dmaInfo.getID());
            normalWater.setNormalWaterSum(normalWaterValue * range);
            normalWater.setStart(start);
            normalWater.setEnd(end);
            normalWater.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            normalWater.setOk(false);
            if(null == dmaInfo) {
                dmaLogManager.saveLog(-1, false, "分区信息获取失败," + getClass().getName());
            } else {
                dmaLogManager.saveLog(dmaInfo.getID(), false,
                        "分区'" + dmaInfo.getName() + "'," + getClass().getName() + "," + e.getMessage());
            }
        }
        return normalWater;
    }

    //计算区域内户均用水量，单位m3/h
    private double getQuanPerFamily(DMAInfo dmaInfo, Date start, Date end) {
        double numerator = 0, denominator = 0;

        try {
            /*Map<String, Object> paraMap = new HashMap<String, Object>();
            String hql = " from DMASaleWater dmaSaleWater " +
                         " where active = 1 " +
                         " and id = :dmaId ";
            paraMap.put("dmaId", dmaInfo.getID());
            List<DMASaleWater> dmaSaleWaterList = (List<DMASaleWater>) dmaSaleWaterManager.
                    createQuery(hql, paraMap).list();
*/
            List<DMASaleWater> dmaSaleWaterList = dmaSaleWaterManager.
                    getSaleWaterListByDMA(dmaInfo);
            for(DMASaleWater dmaSaleWater : dmaSaleWaterList) {
                if(dmaSaleWater.getFamilyNum() != 0) {
                    double factor = DateUtils.getDateDiff(dmaSaleWater.getStartDate(), dmaSaleWater.getEndDate());
                   // numerator += (dmaSaleWater.getSaleWater() + dmaSaleWater.getNoValueWater()) * factor;
                    numerator += (dmaSaleWater.getSaleWater() + dmaSaleWater.getNoValueWater());
                    denominator += factor;
                }
            }
            if(0 == denominator) {
                dmaLogManager.saveLog(dmaInfo.getID(), true,
                        "分区'" + dmaInfo.getName() + "'无有效售水量，取默认值");
                return 1.7e-3;//单位m3/h
            } else {
                return (numerator / denominator) / 24.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dmaLogManager.saveLog(dmaInfo.getID(), false,
                    "计算户均用水量出错，分区'" + dmaInfo.getName() + "'," + getClass().getName() + "," + e.getMessage());
            return 0;
        }

    }
}