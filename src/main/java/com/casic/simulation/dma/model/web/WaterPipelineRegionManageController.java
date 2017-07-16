package com.casic.simulation.dma.model.web;

import com.casic.simulation.dma.model.dto.WaterPipelineRegionForm;
import com.casic.simulation.dma.model.manager.DMAInfoManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("WaterPipelineRegion")
public class WaterPipelineRegionManageController {

    @Resource
    private DMAInfoManager dmaInfoManager;

    @RequestMapping("delSubDMA")
    @ResponseBody
    @POST
    public Map<String, Object> delSubDMA(
            @RequestParam(value = "dmaID", required = true) Long dmaID
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = dmaInfoManager.removeModel(dmaID);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","删除失败！");
        }
        return map;
    }

    @RequestMapping("addSubDMA")
    @ResponseBody
    public Map<String,Object> addSubDMA(
            @ModelAttribute WaterPipelineRegionForm form
    ) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            if(null != form.getBDataParent_DMA()){
                map = dmaInfoManager.saveModel(form);
            } else {
                map.put("success",false);
                map.put("msg","请选择父DMA分区！");
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","保存失败！");
        }
        return map;
    }

}
