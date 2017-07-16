package com.casic.simulation.dma.model.web;

import com.casic.simulation.dma.model.dto.DevPosForm;
import com.casic.simulation.dma.model.manager.DevPosManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("WaterPipelineDevice")
public class WaterPipelineDeviceManageController {

    @Resource
    private DevPosManager devPosManager;

    @RequestMapping("deleteDevice")
    @ResponseBody
    @POST
    public Map<String, Object> deleteDevice(
            @RequestParam(value = "devID") Long devID,
            @RequestParam(value = "positionID") Long positionID
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = devPosManager.removeDeviceFromPosition(devID, positionID);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","删除失败！");
        }
        return map;
    }

    @RequestMapping("addDevice")
    @ResponseBody
    public Map<String, Object> addDevice(
            @ModelAttribute DevPosForm form
    ) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            if(form.getPositionId() == null){
                map.put("success",false);
                map.put("msg","请选择挂载设备的监测点！");
            }else {
                map = devPosManager.appendDeviceIntoPosition(form);
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","添加设备失败！");
        }
        return map;
    }
}
