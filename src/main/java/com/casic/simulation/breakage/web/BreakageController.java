package com.casic.simulation.breakage.web;

import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.breakage.bean.NoiseDeviceRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("breakage")
public class BreakageController {

    @Resource
    private NoiseDeviceRequest noiseDeviceRequest;

    @RequestMapping("getNoiseDeviceList")
    public void getNoiseDeviceList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "devCode",required = false) String devCode,
            HttpServletResponse response
    ) throws IOException {
        try {
            noiseDeviceRequest.setPage(page);
            noiseDeviceRequest.setRows(rows);
            noiseDeviceRequest.setDevCode(devCode);
            JSONTool.writeJsonResult(response,
                    noiseDeviceRequest.queryDeviceList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("getNoiseAlarmRecordList")
    public void getNoiseAlarmRecordList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "devCode",required = false) String devcode,
            @RequestParam(value = "messageStatus",required = false) String messageStatus,
            @RequestParam(value = "beginDate",required = false) String beginDate,
            @RequestParam(value = "endDate",required = false) String endDate,
            HttpServletResponse response
    ) throws IOException {
        try {
            noiseDeviceRequest.setPage(page);
            noiseDeviceRequest.setRows(rows);
            noiseDeviceRequest.setDevCode(devcode);
            noiseDeviceRequest.setMessageStatus(messageStatus);
            noiseDeviceRequest.setBeginDate(beginDate);
            noiseDeviceRequest.setEndDate(endDate);
            JSONTool.writeJsonResult(response,
                    noiseDeviceRequest.queryRecord());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
