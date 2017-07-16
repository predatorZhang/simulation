package com.casic.simulation.dma.dmamanager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.SupplyWater;
import com.casic.simulation.dma.model.domain.SensorFlowRecord;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yxw on 2017/2/20.
 */
@Service
public class SupplyWaterManager extends HibernateEntityDao<SupplyWater> {

    @Resource
    private DmaDeviceManager dmaDeviceManager;

    @Resource
    public DmaLogManager dmaLogManager;

    public SupplyWater GetSupplyWaterByTimeRange(Date start,Date end,int regionId)
    {
        SupplyWater supplyWater=new SupplyWater();
        Session session=this.getSession();
        long dmaInfoId=dmaDeviceManager.getDMAInfoByDBID(regionId).getID();

        try{
            supplyWater.setDateStart(start);
            supplyWater.setDateEnd(end);
            supplyWater.setRegionID(regionId);

            Map<Device, String>  map = dmaDeviceManager.getDevByRegionIdAndType(regionId, "000031"); //0:流量监测点
            if(map==null)
            {
                dmaLogManager.saveLog(dmaInfoId,false,"分区设备获取失败");
                return null;
            }
            double waterQuantity=getWaterQuantity(session,start,end,map,regionId);
            supplyWater.setWaterQuantity(waterQuantity);
            if(waterQuantity==0) {
                supplyWater.setOk(false);
                dmaLogManager.saveLog(dmaInfoId,false,"时间区间"+start+"至"+end+"中供水量监测数据缺失");
            }
            else{
                supplyWater.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            supplyWater.setOk(false);
            dmaLogManager.saveLog(dmaInfoId,false,"供水量计算失败");
        }
        return supplyWater;
    }

    private double getWaterQuantity(Session session,Date start,Date _end,Map<Device, String> map,int regionId)
    {
        Date end=(Date)_end.clone();

        end.setTime(end.getTime()+24*60*60*1000);//推迟一天
        double startFlow = getHqlNetData(start,map,regionId);
        double endFlow = getHqlNetData(end,map,regionId);

        double waterQuatity;
        if(startFlow > 0 && endFlow > 0)
        {
            waterQuatity=endFlow-startFlow;
        }
        else
        {
            waterQuatity=0;
        }
        return waterQuatity;
    }

    private double getHqlNetData(Date _date, Map<Device, String> map,int regionId)
    {
        Date date=(Date)_date.clone();

        double flowData=0;
        for (Map.Entry<Device, String> strMap : map.entrySet()) {
            double netData=GetNetDataFromTable(strMap,date);
            if(netData==0)
            {
                //TODO LIST：predator 判断这个设备究竟有没有流量传感器:
                dmaLogManager.saveLog(regionId,false,
                        "设备:"+strMap.getKey()+"在"+date.toString()+"无数据");
                return 0;
            }
            else
            {
                flowData+=netData*Double.parseDouble(strMap.getValue());
            }
        }
        return flowData;
    }

    private double GetNetDataFromTable(Map.Entry<Device, String> map,Date date) {
        Criteria criteria = getSession().createCriteria(SensorFlowRecord.class);
        criteria.add(Restrictions.eq("uptime", date));
        criteria.add(Restrictions.eq("devId", map.getKey().getDevCode()));
        double netData = 0;
        if(criteria.list().size()>0)
        {
            List<SensorFlowRecord> sensorFlowRecords = criteria.list();
            netData=Double.parseDouble(sensorFlowRecords.get(0).getNetData());
        }
        else
        {
            return 0;
        }
        return netData;
    }

}
