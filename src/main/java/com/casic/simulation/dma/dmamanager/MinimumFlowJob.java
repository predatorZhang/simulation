package com.casic.simulation.dma.dmamanager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.MinimumFlow;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.DmaMinimumFlow;
import com.casic.simulation.dma.model.domain.SensorFlowRecord;
import com.casic.simulation.util.PropertiesUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yxw on 2017/3/14.
 */
@Service
public class MinimumFlowJob extends HibernateEntityDao<DmaMinimumFlow> /*implements ISystemJob*/ {

    @Resource
    public DmaDeviceManager dmaDeviceManager;

    protected PropertiesUtil propUtil = new PropertiesUtil("application.properties");

/*
    @Override
    public void auotExexcute() {
        //TODO LIST;自定分析代码
        saveMinimumFlowByDay();
    }*/

    public void saveMinimumFlowByDay(Date date) {

        DmaMinimumFlow dmaMinimumFlow = new DmaMinimumFlow();
        List<DMAInfo> dmaInfoList = getAllDmaInfo();//获取现有的DMA分区信息

        for (DMAInfo dmaInfo : dmaInfoList) {
            dmaMinimumFlow.setDmaInfo(dmaInfo.getID());
            MinimumFlow minimumFlow = new MinimumFlow();
            minimumFlow = getMinimumFlowByDmaInfo(dmaInfo, date);//计算单个DMA分区的最小流量
            dmaMinimumFlow.setMinFlowTime(minimumFlow.getMinFlowTime());
            dmaMinimumFlow.setMinFlowValue(String.valueOf(minimumFlow.getMinFlow()));
        }
        this.save(dmaMinimumFlow);
    }

    public void saveMinimumFlowByDay() {
        Date date = new Date();//每天的凌晨一点，计算前一天的最小瞬时流量
        this.saveMinimumFlowByDay(date);
    }

    private List<DMAInfo> getAllDmaInfo() {
        List<DMAInfo> dmaInfos = new ArrayList<DMAInfo>();
        Criteria criteria = this.getSession().createCriteria(DMAInfo.class);
        criteria.add(Restrictions.eq("active", true));
        dmaInfos = criteria.list();
        return dmaInfos;

    }

    private MinimumFlow getMinimumFlowByDmaInfo(DMAInfo dmaInfo, Date date) {
        Date start = new Date();
        start = date;
        //TODO LIST: 减去一天
        start = changeDateByDay(start, -1);//获取前一天的零点

        Session session = this.getSession();
        Map<Device, String> deviceMap = new HashMap<Device, String>();
        deviceMap = dmaDeviceManager.getDevByRegionIdAndType(dmaInfo.getID().intValue(),"000031");
        MinimumFlow minimumFlow = new MinimumFlow();

        try {
            double minFlow = 0;
            Date dt = new Date();
            Date minDt = null;
            for (int i = 0; i < 96; i++) {
                double flow = 0;
                //查对应时间的最小流量，如果为空，则提示用户！
                for (Map.Entry<Device, String> strMap : deviceMap.entrySet()) {
                    Criteria criteria = getSession().createCriteria(SensorFlowRecord.class);
                    dt.setTime(start.getTime() + i * 15 * 60 * 1000);//隔十五分钟计算一次
                    criteria.add(Restrictions.eq("uptime", dt));
                    criteria.add(Restrictions.eq("devId", strMap.getKey().getDevCode()));
                    if (criteria.list().size() > 0) {
                        //TODO LIST:累加出现负值怎么办？
                        List<SensorFlowRecord> sensorFlowRecords = criteria.list();
                        double strInsData = Double.parseDouble(sensorFlowRecords.get(0).getInsData());
                        flow += strInsData * Double.parseDouble(strMap.getValue());
                    }
                }
                if (i == 0) {
                    minFlow = flow;
                    minDt = (Date) dt.clone();
                } else if (flow < minFlow) {
                    minFlow = flow;
                    minDt = (Date) dt.clone();
                }
            }
            minimumFlow.setRegionID(dmaInfo.getID().intValue());
            minimumFlow.setMinFlow(minFlow);
            minimumFlow.setMinFlowTime(minDt);
            return minimumFlow;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Date changeDateByDay(Date date, int Day) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, Day);
            date = calendar.getTime();

            SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formate.format(date);
            Date dt = DateUtils.sdf1.parse(dateString);
            return dt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return date;
        }
    }

}
