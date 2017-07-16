package com.casic.simulation.dma.model.web;

import com.casic.simulation.dma.model.dto.DMASaleWaterForm;
import com.casic.simulation.dma.model.json.DMASaleWaterJSON;
import com.casic.simulation.dma.model.manager.DMAInfoManager;
import com.casic.simulation.dma.model.manager.DMASaleWaterManager;
import com.casic.simulation.log.manager.SysLogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("DMASaleWater")
public class DMASaleWaterManageController {

    @Resource
    private DMASaleWaterManager dmaSaleWaterManager;

    @Resource
    private DMAInfoManager dmaInfoManager;

    @Resource
    private SysLogManager sysLogManager;

    @RequestMapping("getSaleWaterList")
    @ResponseBody
    @POST
    public Map<String, Object> getSaleWaterList(
            @RequestParam(value = "dmaID", required = true) Long dmaID,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = dmaSaleWaterManager.pagedQueryJson(dmaID, pageNum, rows);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("total", 0);
            map.put("rows", new ArrayList<DMASaleWaterJSON>());
        }
        return map;
    }

    @RequestMapping("delete")
    @ResponseBody
    @POST
    public Map<String, Object> delete(
            @RequestParam(value = "saleWaterID") Long saleWaterID
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = dmaSaleWaterManager.removeModel(saleWaterID);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","售水量删除失败！");
        }
        return map;
    }

    @RequestMapping("addDMASaleWater")
    @ResponseBody
    public Map<String, Object> addDMASaleWater(
            @ModelAttribute DMASaleWaterForm form
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",false);
        try {
            if (form.getDmaID() == -1) {
                map.put("msg", "根分区不能添加售水量！");
            } else if (form.getDmaID() == null){
                map.put("msg", "必须选择分区才能添加售水！");
            } else {
                return dmaSaleWaterManager.addSaleWater(form);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "添加失败[" + e.getMessage() + "].");
        }
        return map;
    }
}
