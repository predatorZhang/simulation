package com.casic.simulation.dma.dmamanager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.AveragePress;
import com.casic.simulation.dma.AveragePressList;
import com.casic.simulation.dma.MinimumFlowSum;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.DmaLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DmaLogManager extends HibernateEntityDao<DmaLog> {
    //不要改regionId类型，这个id是DMA分区的dbid，默认是long类型
    public boolean saveLog(long regionId, boolean success, String message) {
        try {
            DmaLog dmaLog = new DmaLog();
            dmaLog.setCreateTime(new Date());
            dmaLog.setMessage(message);
            dmaLog.setRegionId(regionId);
            dmaLog.setSuccess(success);
            save(dmaLog);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DMAInfo> getDmaByID(String hql, Map<String, Object> map) {
        List<DMAInfo> dmaInfoList = (List<DMAInfo>) this.createQuery(hql, map).list();
        return dmaInfoList;

    }

    @Resource
    public DmaDeviceManager dmaDeviceManager;
    public List<Device> getDeviceByDmaIdAndType(long dmaId, String type) {

        Map<Device, String> map = dmaDeviceManager.getDevByRegionIdAndType((int) dmaId,type);
        List<Device> devs = new ArrayList<Device>();

        for (Map.Entry<Device, String> strMap : map.entrySet()) {
            devs.add(strMap.getKey());
        }
        return devs;
        //TODO LIST:predator 是否考虑逻辑删除的情况
       /* Map<String, Object> paraMap = new HashMap<String, Object>();
        String hql = " select devPos.device " +
                " from DevPos devPos " +
                " where devPos.active = 1 " +
                " and devPos.sensorType = " +
                " (select sensorType.sensorcode " +
                " from SensorType sensorType " +
                " where sensorType.sensorname = '压力') " +
                " and devPos.positionInfo.ID in " +
                " (select posDma.positionInfo.ID " +
                " from PosDma posDma " +
                " where posDma.dmaInfo.ID = :dmaId) ";
        paraMap.put("dmaId", dmaId);
        List<Device> deviceList = (List<Device>) this.createQuery(hql, paraMap).list();*/
    }

    public AveragePressList getAvrPress(DMAInfo dmaInfo, List<Device> deviceList, MinimumFlowSum flowSum,
                                        Date start, Date end) {
        AveragePressList averagePressList = new AveragePressList();
        try {
            if(deviceList.size() == 0) {
                this.saveLog(dmaInfo.getID(), false,
                        "分区'" + dmaInfo.getName() + "'," + getClass().getName() + ",压力数据缺失，计算终止");
                return null;
            }
            Map<String, Object> paraMap = new HashMap<String, Object>();
            String sql = " select AVG(t.pressdata), round(t.uptime - 0.5) " +
                    " from ad_dj_press t " +
                    " where to_char(t.uptime, 'hh24')>=1 " +
                    " and to_char(t.uptime, 'hh24') <=5 " +
                    " and (1=0 ";
            for(Device device : deviceList) {
                sql += " or devcode = '" + device.getDevCode() + "' ";
            }
            sql += ")" +
                    " and t.uptime >= :start " +
                    " and t.uptime <  :end " +
                    " group by round(t.uptime - 0.5) " +
                    " order by round(t.uptime - 0.5)";
            paraMap.put("start", start);
            paraMap.put("end", DateUtils.AddDate(end, 1));
            List<Object[]> avePressList = this.getSession().createSQLQuery(sql).setProperties(paraMap).list();

            List<AveragePress> averagePressList1 = new ArrayList<AveragePress>();
            for(Object[] objects : avePressList) {
                AveragePress averagePress = new AveragePress();
                averagePress.setAvgPress(Double.valueOf(objects[0].toString()));
                averagePress.setDate(DateUtils.sdf1.parse(objects[1].toString()));
                averagePress.setRegionID(dmaInfo.getID());
                averagePress.setOk(true);
                averagePressList1.add(averagePress);
            }
            if(avePressList.size() == 0) {
                this.saveLog(dmaInfo.getID(), false,
                        "分区'" + dmaInfo.getName() + "'," + getClass().getName() + ",该时间段内压力数据缺失，计算终止");
                return null;
            }
            averagePressList.setAveragePressList(averagePressList1);
            averagePressList.setStart(start);
            averagePressList.setEnd(end);
            averagePressList.setRegionID(dmaInfo.getID());
            averagePressList.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            averagePressList.setOk(false);
            this.saveLog(dmaInfo.getID(), false,
                    "分区平均压力计算出错，" + dmaInfo.getName() + "'," + getClass().getName() + "," + e.getMessage());
        }
        return averagePressList;
    }
}