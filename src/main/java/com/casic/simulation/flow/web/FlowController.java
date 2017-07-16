package com.casic.simulation.flow.web;

import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.dto.FlowDto;
import com.casic.simulation.flow.manager.FlowManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("flow")
public class FlowController {

    @Resource
    private FlowManager flowManager;

    @POST
    @RequestMapping("fire-flow")
    @ResponseBody
    public Map<String, Object> fireFlow(
            @RequestParam(value = "flows[]") Long[] flows
    ) {
        Map<String, Object> result = new HashMap<String, Object>();
        ExecInfo execInfo = flowManager.fireFlows(flows);
        result.put("success", execInfo.isSucc());
        result.put("msg", execInfo.getMsg());
        return result;
    }

    @RequestMapping("flow-show")
    public String showFlows(Model model) {
        Flow defaultFlow = flowManager.getDefaultFlow();
        model.addAttribute("defaultFlow", defaultFlow);
        return "model-flowConfig";
    }

    @POST
    @RequestMapping("flow-list")
    @ResponseBody
    public Map<String, Object> listFlows() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<FlowDto> dtos = flowManager.getFlows();
        map.put("total", dtos.size());
        map.put("rows", dtos);
        return map;
    }

    @POST
    @RequestMapping("queryAllFlow")
    @ResponseBody
    public List<FlowDto> queryAllFlow() {
        return flowManager.getAllActiveFlow();
    }
}