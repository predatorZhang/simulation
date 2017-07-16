package com.casic.simulation.device.manager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.device.domain.DeviceSensor;
import com.casic.simulation.device.domain.SensorType;
import com.casic.simulation.dma.model.dto.UsefulDevice;
import com.casic.simulation.dma.model.json.DeviceJSON;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceSensorManager extends HibernateEntityDao<DeviceSensor> {

    public Criteria getCriteria(){
        return getSession().createCriteria(DeviceSensor.class);
    }

    /**
     * 获取不在监测点{@link com.casic.simulation.dma.model.domain.DevPos}的设备
     * （超声波流量监测仪，压力监测仪）列表
     * @return
     */
    public List<DeviceJSON> findNotInPositionDevice() {
        Map<String, Object> paraMap = new HashMap<String, Object>();
       /* String hql = " from Device device " +
                " where (device.deviceSensor.id.sensorcode = '" +
                UsefulDevice.FLOW.getName() +
                "'" +
                " or device.deviceSensor.id.sensorcode = '" +
                UsefulDevice.PRESS.getName() +
                "') " +
                " and device.id not in " +
                " (select devPos.device.id " +
                " from DevPos devPos" +
                " where devPos.active = true ) ";*/

        String hql = " from Device device " +
                " where (device.active = true) and device.id not in " +
                " (select devPos.device.id " +
                " from DevPos devPos" +
                " where devPos.active = true ) ";
        List<Device> list = createQuery(hql, paraMap).list();

        //过滤带有流量、压力传感器数据设备
        List<Device> filterDevices = new ArrayList<Device>();
        for (Device dev : list) {
            for (DeviceSensor deviceSensor : dev.getDeviceSensors()) {
                if (deviceSensor.getId().getSensorcode().equals(UsefulDevice.FLOW.getName()) ||
                        deviceSensor.getId().getSensorcode().equals(UsefulDevice.PRESS.getName())) {
                    filterDevices.add(dev);
                    break;
                }
            }
        }

        List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
        for(Device device:filterDevices){
            jsons.add(new DeviceJSON(device));
        }
        return jsons;
    }

    public Map<String, Object> getSensorOfDevice(Long devId) {
        Map<String,Object> map = new HashMap<String ,Object>();
        Criteria criteria = getCriteria();
        criteria.setProjection(Projections.property("sensorType"));
        criteria.add(Restrictions.eq("active", true));
        criteria.createAlias("device", "device");
        criteria.add(Restrictions.eq("device.id", devId));
        List<SensorType> sensorList = criteria.list();

        StringBuffer sensorTypeIdBuffer = new StringBuffer("");
        StringBuffer sensorTypeNameBuffer = new StringBuffer("");
        for(SensorType sensor : sensorList){
            sensorTypeIdBuffer.append(sensor.getSensorcode() + ",");
            sensorTypeNameBuffer.append(sensor.getSensorname() + ",");
        }
        if(sensorTypeIdBuffer.length()>0){
            sensorTypeIdBuffer.deleteCharAt(sensorTypeIdBuffer.length()-1);
        }
        if(sensorTypeNameBuffer.length()>0){
            sensorTypeNameBuffer.deleteCharAt(sensorTypeNameBuffer.length()-1);
        }
        map.put("sensorTypeId",sensorTypeIdBuffer.toString());
        map.put("sensorTypeName",sensorTypeNameBuffer.toString());
        return map;
    }
}
