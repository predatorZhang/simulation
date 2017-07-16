package com.casic.simulation.dma.analysis.web;

import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.dma.DMAAnalysis;
import com.casic.simulation.dma.DMAResult;
import com.casic.simulation.dma.analysis.json.LeakageEvaJSON;
import com.casic.simulation.dma.analysis.manager.MockWaterPipeLineAnalysisManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("WaterPipelineAnalysis")
public class WaterPipelineAnalysisController {

    /**
     * predator:获取最新各个分区漏损评估分析结果
     * 读取配置文件中的manager
     */
    @Resource
    public MockWaterPipeLineAnalysisManager waterPipeLineAnalysisManager;

    @Resource
    private DMAAnalysis dmaAnalysis;

    @RequestMapping("getCurrentLeakage")
    @ResponseBody
    @GET
    public Map<String, Object> getCurrentLeakage(
            @RequestParam(value = "page",required = true) Integer page,
            @RequestParam(value = "rows",required = true) Integer rows,
            @RequestParam(value = "beginDate") String beginDate,
            @RequestParam(value = "endDate") String endDate
    ) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Page p = waterPipeLineAnalysisManager.getCurrentLeakge(page, rows);
            List<LeakageEvaJSON> list = (List<LeakageEvaJSON>)p.getResult();
            for (LeakageEvaJSON json : list) {
                DMAResult result = dmaAnalysis.getLeakageRateByRange(
                        DateUtils.sdf1.parse(beginDate),
                        DateUtils.sdf1.parse(endDate),
                        Integer.parseInt(json.dmaId)
                );
                json.setLeakRate(result.getLeakageRate());
                json.setCode(result.getCode());
                json.setErrorMsg(result.getErrorMsg());
            }
            resultMap.put("rows", list);
            resultMap.put("total", p.getTotalCount());
            resultMap.put("success", true);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }
}
