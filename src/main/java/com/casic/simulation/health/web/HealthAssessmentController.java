package com.casic.simulation.health.web;

import com.casic.simulation.core.mapper.JsonMapper;
import com.casic.simulation.health.domain.HealthInfo;
import com.casic.simulation.health.dto.HealthInfoDTO;
import com.casic.simulation.health.manager.HealthAssessmentManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("health")
public class HealthAssessmentController {
    private HealthAssessmentManager healthAssessmentManager;

    @Resource
    public void setHealthAssessmentManager(HealthAssessmentManager healthAssessmentManager) {
        this.healthAssessmentManager = healthAssessmentManager;
    }

    @POST
    @RequestMapping("health-info-list")
    public
    @ResponseBody
    String coatingAnalysisInfo(HttpServletRequest request) {
        try {
            int rows = Integer.parseInt(request.getParameter("rows"));
            int page = Integer.parseInt(request.getParameter("page"));
            HealthInfoDTO healthInfoDTO = new HealthInfoDTO();
            healthInfoDTO.setPipeId(request.getParameter("no"));
            healthInfoDTO.setHealthRank(request.getParameter("level"));
            healthInfoDTO.setResult(request.getParameter("desc"));
            Map map = healthAssessmentManager.queryHealthInfoList(page, rows, healthInfoDTO);
            return new JsonMapper().toJson(map);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequestMapping("health-detail-show")
    public String showDetail(@RequestParam(value = "dbId", required = false) Long dbId,
                             Model model) {
        HealthInfoDTO healthInfoDTO = new HealthInfoDTO();
        if (dbId != null) {
            healthInfoDTO = healthAssessmentManager.findHealthInfoById(dbId);
        }

        model.addAttribute("model", healthInfoDTO);
        return "foreWarning/healthInfo";
    }

    @POST
    @RequestMapping("health-info-save")
    public void save(@RequestParam(value = "pipeId", required = false) String pipeId) {
        try {
            HealthInfo healthInfo = new HealthInfo();
            healthInfo.setPipeType("给水管线");
            healthInfo.setPipeId("GX_JSL_3000_HDL_152");
            healthInfo.setHealthRank("亚健康");
            healthInfo.setEvalTime(new Date());
            healthInfo.setResult("asdasdasdas");
            healthInfo.setStreet("湖东路");
            healthAssessmentManager.save(healthInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}