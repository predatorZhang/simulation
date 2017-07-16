package com.casic.simulation.dma;

import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.dmamanager.DmaLogManager;
import com.casic.simulation.dma.model.domain.DMAInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 */
@Service
public class AveragePressFetcher {
    @Resource
    public DmaLogManager dmaLogManager;

    public AveragePressList getAvgPressList(DMAInfo dmaInfo, MinimumFlowSum flowSum, Date start, Date end) {
        AveragePressList averagePressList = new AveragePressList();
        try {
            //1.获取分区内所有多功能设备列表
            List<Device> deviceList = (List<Device>) getDevieListInDma(dmaInfo.getID());
            //2.计算日期范围内每天凌晨1点到5点分区内平均压力列表，单位
            averagePressList = getAvePressListInDma(dmaInfo, deviceList, flowSum, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            dmaLogManager.saveLog(dmaInfo.getID(), false,
                    "分区'" + dmaInfo.getName() + "'," + getClass().getName() + "," + e.getMessage());
        }
        return averagePressList;
    }

    //TODO LIST:统一一下根据分区找设备的方式
    private List<Device> getDevieListInDma(long dmaId) {
        try {
            return dmaLogManager.getDeviceByDmaIdAndType(dmaId,"000033");//1:压力
        } catch (Exception e) {
            e.printStackTrace();
            dmaLogManager.saveLog(dmaId, false,
                    "获取分区" + dmaId + "的设备出错," + getClass().getName() + "," + e.getMessage());
            return null;
        }
    }

    private AveragePressList getAvePressListInDma(DMAInfo dmaInfo, List<Device> deviceList, MinimumFlowSum flowSum,
                                                  Date start, Date end) {
        return dmaLogManager.getAvrPress(dmaInfo, deviceList,
                flowSum, start, end);
    }
}