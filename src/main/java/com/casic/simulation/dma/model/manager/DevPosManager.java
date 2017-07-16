package com.casic.simulation.dma.model.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.device.manager.DeviceManager;
import com.casic.simulation.device.manager.SensorTypeManager;
import com.casic.simulation.dma.model.domain.DevPos;
import com.casic.simulation.dma.model.domain.PositionInfo;
import com.casic.simulation.dma.model.dto.DevPosForm;
import com.casic.simulation.dma.model.json.DeviceJSON;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DevPosManager extends HibernateEntityDao<DevPos> {

    @Resource
    private SensorTypeManager sensorTypeManager;

    @Resource
    private DeviceManager deviceManager;

    @Resource
    private PositionInfoManager positionInfoManager;

    public Criteria getCriteria() {
        return getSession().createCriteria(DevPos.class);
    }

    /**
     * 根据监测点{@link com.casic.simulation.dma.model.domain.PositionInfo} ID获取设备列表信息
     * @param posID
     * @param pageNum
     * @param rows
     * @return
     */
    public Map<String, Object> findDeviceDTOByPos(Long posID, int pageNum, int rows) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("active", true));
        criteria.createAlias("positionInfo", "pos");
        criteria.add(Restrictions.eq("pos.ID", posID));
        criteria.createAlias("device", "dev");
        criteria.add(Restrictions.eq("dev.active", true));

        Page page = pagedQuery(criteria, pageNum, rows);

        List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
        List<DevPos> devPosList = (List<DevPos>) page.getResult();
        for (DevPos devPos : devPosList) {
            DeviceJSON json = new DeviceJSON(devPos.getDevice());
            String sensorName = sensorTypeManager.findUniqueBy("sensorcode",devPos.getSensorType()).getSensorname();
            json.setMonitorType(sensorName);
            jsons.add(json);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total", page.getTotalCount());
        map.put("rows", jsons);
        return map;
    }

    /**
     * 将监测点PosintionInfo中的设备删除
     * 二院系统采用#软删除#
     * @param devID
     * @param positionID
     * @return
     */
    public Map<String, Object> removeDeviceFromPosition(
            Long devID, Long positionID) {
        Map<String,Object> map = new HashMap<String, Object>();
        Device device = deviceManager.get(devID);
        PositionInfo positionInfo = positionInfoManager.get(positionID);
        String hql = "delete from DevPos where device=:dev and positionInfo=:pos";
        map.put("dev", device);
        map.put("pos", positionInfo);
        batchUpdate(hql,map);
        map.clear();
        map.put("success", true);
        map.put("msg", "设备删除成功！");
        return map;
    }

    /**
     * 增加设备到监测点
     * @param model
     * @return
     */
    public Map<String, Object> appendDeviceIntoPosition(DevPosForm model) {
        Map<String,Object> map = new HashMap<String, Object>();
        if(isInPosition(model)){
            map.put("success",false);
            map.put("msg","设备不能重复挂载到不同的监测点！");
            return map;
        }
        Device device = deviceManager.get(model.getDevId());
        PositionInfo position = positionInfoManager.get(model.getPositionId());
        if (device == null || position == null) {
            map.put("success",false);
            map.put("msg","设备或监测点已经失效！");
            return map;
        }
        String[] sensorTypeArray = model.getSensorType().split(",");
        for (String curSensorType : sensorTypeArray) {
            DevPos devPos = new DevPos();
            devPos.setDevice(device);
            devPos.setPositionInfo(position);
            devPos.setSensorType(curSensorType);
            devPos.setPipeMaterial(model.getPipeMaterial());
            devPos.setPipeSize(model.getPipeSize());
            devPos.setStartTotalValue(model.getStartTotalValue());
            devPos.setLowInstantValue(model.getLowInstantValue());
            devPos.setHighInstantValue(model.getHighInstantValue());
            save(devPos);
        }
        map.put("success",true);
        map.put("msg","设备添加成功！");
        return map;
    }

    /**
     * 设备不能重复挂载到不同的监测点
     * @param model
     * @return
     */
    public boolean isInPosition(DevPosForm model){
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("active",true));
        criteria.add(Restrictions.ne("positionInfo", positionInfoManager.get(model.getPositionId())));
        criteria.add(Restrictions.eq("device", deviceManager.get(model.getDevId())));
        if(criteria.list().size()>0){
            return true;
        }
        return false;
    }
}
