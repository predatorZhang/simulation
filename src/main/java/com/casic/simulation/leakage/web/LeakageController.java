package com.casic.simulation.leakage.web;

import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.leakage.bean.FlowDeviceRequest;
import com.casic.simulation.leakage.bean.PressDeviceRequest;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("leakage")
public class LeakageController {

    @Resource
    private PressDeviceRequest pressDeviceRequest;

    /**
     * 获取压力报警记录信息
     * @param page
     * @param rows
     * @param devcode
     * @param messageStatus
     * @param beginDate
     * @param endDate
     * @param id
     * @param roadName
     * @param response
     * @throws IOException
     */
    @RequestMapping("getPressAlarmRecordList")
    public void getPressAlarmRecordList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "devCode",required = false) String devcode,
            @RequestParam(value = "messageStatus",required = false) String messageStatus,
            @RequestParam(value = "beginDate",required = false) String beginDate,
            @RequestParam(value = "endDate",required = false) String endDate,
            @RequestParam(value = "id",required = false) String id,
            @RequestParam(value = "roadName",required = false) String roadName,
            HttpServletResponse response
    ) throws IOException {
        try {
            pressDeviceRequest.setPage(page);
            pressDeviceRequest.setRows(rows);
            pressDeviceRequest.setDevCode(devcode);
            pressDeviceRequest.setId(Integer.valueOf(
                    StringUtils.isBlank(id) ? "0" : id
            ));
            pressDeviceRequest.setTurnX(roadName);
            pressDeviceRequest.setMessageStatus(messageStatus);
            pressDeviceRequest.setBeginDate(beginDate);
            pressDeviceRequest.setEndDate(endDate);
            JSONTool.writeJsonResult(response,
                    pressDeviceRequest.queryAlarmRecordList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取压力设备列表
     * @param page
     * @param rows
     * @param devCode
     * @param response
     * @throws IOException
     */
    @RequestMapping("getPressDeviceList")
    public void getPressDeviceList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "devCode",required = false) String devCode,
            HttpServletResponse response
    ) throws IOException {
        try {
            pressDeviceRequest.setPage(String.valueOf(page));
            pressDeviceRequest.setRows(String.valueOf(rows));
            pressDeviceRequest.setDevCode(devCode);
            JSONTool.writeJsonResult(response,
                    pressDeviceRequest.queryFlowDeviceList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Resource
    private FlowDeviceRequest flowDeviceRequest;

    /**
     * 获取流量设备列表
     * @param page
     * @param rows
     * @param devCode
     * @param response
     * @throws IOException
     */
    @RequestMapping("getFlowDeviceList")
    public void getFlowDeviceList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "devCode",required = false) String devCode,
            HttpServletResponse response
    ) throws IOException {
        try {
            flowDeviceRequest.setPage(page);
            flowDeviceRequest.setRows(rows);
            flowDeviceRequest.setDevCode(devCode);
            JSONTool.writeJsonResult(response,
                    flowDeviceRequest.queryDeviceList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
