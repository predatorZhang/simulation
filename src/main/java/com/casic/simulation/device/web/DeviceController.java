package com.casic.simulation.device.web;

import com.casic.simulation.device.dto.DeviceDetailDTO;
import com.casic.simulation.device.manager.DeviceManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.*;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("device")
public class DeviceController {

    @RequestMapping("getDeviceListDetails")
    @ResponseBody
    @POST
    //todo-list ： 重新实现，加载地球显示设备列表
    public Map<String, Object> getDeviceListDetails() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<DeviceDetailDTO> deviceDetailDTOs = new ArrayList<DeviceDetailDTO>();
            if (deviceDetailDTOs.size() != 0) {
                map.put("success", true);
                map.put("data", deviceDetailDTOs);
            } else {
                map.put("success", false);
            }
        } catch (Exception e) {
            map.put("success" ,false);
            map.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

}
