package com.casic.simulation.dma.model.web;

import com.casic.simulation.dma.model.dto.PositionForm;
import com.casic.simulation.dma.model.manager.PosDMAManager;
import com.casic.simulation.dma.model.manager.PositionInfoManager;
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
@RequestMapping("WaterPipelinePosition")
public class WaterPipelinePositionManageController {

    @Resource
    private PositionInfoManager positionInfoManager;

    @Resource
    private PosDMAManager posDMAManager;

    @RequestMapping("deletePosition")
    @ResponseBody
    @POST
    public Map<String, Object> deletePosition(
            @RequestParam(value = "posID") Long positionInfoID
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = positionInfoManager.removeModel(positionInfoID);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","删除失败！");
        }
        return map;
    }

    @RequestMapping("addPosition")
    @ResponseBody
    public Map<String, Object> addPosition(
            @ModelAttribute PositionForm form) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (form.getDmaID() != null) {
                if (form.getDmaID() == -1L) {
                    map.put("success", false);
                    map.put("msg", "该分区不能挂载监测点！");
                } else {
                    map = posDMAManager.addNewPosition(form);
                }
            } else {
                map.put("success", false);
                map.put("msg", "请选择监测点所属DMA分区！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "新建失败！");
        }
        return map;
    }

    @RequestMapping("addExistsPosition")
    @ResponseBody
    public Map<String, Object> addExistsPosition(
            @ModelAttribute PositionForm form
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(null != form.getDmaID()){
                if(form.getDmaID() == -1L){
                    map.put("success",false);
                    map.put("msg","ROOT分区下不能添加监测点！");
                } else {
                    map = posDMAManager.appendPosition(form);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","添加监测点失败！");
        }
        return map;
    }

}
