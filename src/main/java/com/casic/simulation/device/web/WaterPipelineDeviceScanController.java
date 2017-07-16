package com.casic.simulation.device.web;

import com.casic.simulation.device.manager.DeviceSensorManager;
import com.casic.simulation.dma.model.json.DeviceJSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("WaterPipelineDeviceScan")
public class WaterPipelineDeviceScanController {

    @Resource
    private DeviceSensorManager deviceSensorManager;

    /**
     * 获取所有给水管线设备(超声波流量监测仪，压力监测仪)
     * 这些设备不包括已经关联到监测点{@link com.casic.simulation.dma.model.domain.PositionInfo}的
     * @return
     */
    @RequestMapping("queryDeviceV2")
    @ResponseBody
    @POST
    public List<DeviceJSON> queryDeviceV2() {
        try {
            return deviceSensorManager.findNotInPositionDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<DeviceJSON>();
    }

    /**
     * 获取设备的种类信息
     * @param devId
     * @return
     */
    @RequestMapping("querySensorByDevId")
    @ResponseBody
    @POST
    public Map<String, Object> querySensorByDevId(
            @RequestParam(value = "devId", required = true) Long devId) {
        Map<String, Object> map = new HashMap<String ,Object>();
        try{
            map = deviceSensorManager.getSensorOfDevice(devId);
        }catch (Exception e){
            e.printStackTrace();
            map.put("error", e.getMessage());
        }
        return map;
    }
}
