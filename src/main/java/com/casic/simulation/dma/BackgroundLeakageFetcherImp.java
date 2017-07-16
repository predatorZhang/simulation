package com.casic.simulation.dma;


import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class BackgroundLeakageFetcherImp implements IBackgroundLeakageFetcher {

    @Resource
    public DmaLogManager dmaLogManager;

    @Resource
    public AveragePressFetcher averagePressFetcher;

    protected PropertiesUtil propUtil = new PropertiesUtil("application.properties");

    //公式：背景漏失量(m3/h)= 1/24×(18×主管道长度(km) + 0.80×管道连接数(个) + 25×私有管道长度(个))×管道压力(mpa)
    @Override
    public BackgroundLeakageSum getBackgroundLeakage(DMAInfo dmaInfo, MinimumFlowSum flowSum, Date start, Date end) {
        BackgroundLeakageSum backgroundLeakageSum = new BackgroundLeakageSum();

        try {
            //每日夜间最小流量期间的平均压力List
            AveragePressList avgPressList = averagePressFetcher.
                    getAvgPressList(dmaInfo, flowSum, start, end);
            if(null == avgPressList) {
                backgroundLeakageSum.setOk(false);
                return backgroundLeakageSum;
            }

            //获取参数
            double mainPipeLength = dmaInfo.getPipeLeng();//主管道长度，单位km
            double pipeLinkNum = dmaInfo.getPipeLinks();//管道连接数，单位个
            double privatePipeLength = dmaInfo.getUserPipeLeng();//私有管道长度，单位km
            double pressSum = getPressSum(dmaInfo, avgPressList,start, end);
            double dayNightFactor = Double.parseDouble(propUtil.getProperty("dma.dayNightFactor"));
            //总和：这段时间内的总和，单位m3
            double backLeakSum = 1 / 24.0 * dayNightFactor
                    * (18 * mainPipeLength + 0.8 * pipeLinkNum + 25 * privatePipeLength) * pressSum;

            backgroundLeakageSum.setBackgroundLeakageSum(backLeakSum);
            backgroundLeakageSum.setRegionID(dmaInfo.getID());
            backgroundLeakageSum.setStart(start);
            backgroundLeakageSum.setEnd(end);
            backgroundLeakageSum.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            backgroundLeakageSum.setOk(false);
            dmaLogManager.saveLog(dmaInfo.getID(), false, "计算分区'" + dmaInfo.getName() +
                    "'的背景漏失量出错," + getClass().getName() + "," + e.getMessage());
        }
        return backgroundLeakageSum;
    }

    //一段时间内管道压力之和, 单位m
    private double getPressSum(DMAInfo dmaInfo, AveragePressList avgPressList, Date start, Date end) {
        double sum = 0;
        try {
            List<AveragePress> averagePressList = avgPressList.getAveragePressList();
            double pressSum = 0;
            for(AveragePress averagePress : averagePressList) {
                pressSum += averagePress.getAvgPress();
            }
            //计算天数：没有问题
            int days = DateUtils.getDateDiff(start, end) + 1;
            sum = pressSum/averagePressList.size()*days;//单位MPa
            //单位换算:换算为m, 1MPa=101.972m
/*
            sum *= 101.972;
*/
        } catch (Exception e) {
            e.printStackTrace();
            dmaLogManager.saveLog(dmaInfo.getID(), false, "计算分区'" + dmaInfo.getName() +
                    "'的平均压力出错," + getClass().getName() + "," + e.getMessage());
        }
        return sum;
    }
}