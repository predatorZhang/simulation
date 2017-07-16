package com.casic.simulation.overflow.web;

import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.overflow.manager.AdDjLiquidManager;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
* Created by lenovo on 2017/3/29.
*/
@Controller
@RequestMapping("overflow")
public class OverflowController {
    @Resource
    private AdDjLiquidManager adDjLiquidManager;

//    @Resource
//    private LiquidDeviceRequest liquidDeviceRequest;

//    @RequestMapping("getLiquidDeviceList")
//    public void getLiquidDeviceList(
//            @RequestParam(value = "page",required = true) String page,
//            @RequestParam(value = "rows",required = true) String rows,
//            @RequestParam(value = "devCode",required = false) String devCode,
//            HttpServletResponse response
//    ) throws IOException {
//        try {
//            liquidDeviceRequest.setPage(page);
//            liquidDeviceRequest.setRows(rows);
//            liquidDeviceRequest.setDevCode(devCode);
//            JSONTool.writeJsonResult(response,
//                    liquidDeviceRequest.queryDeviceList());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @RequestMapping("getLiquidAlarmRecordList")
//    public void getLiquidAlarmRecordList(
//            @RequestParam(value = "page",required = true) String page,
//            @RequestParam(value = "rows",required = true) String rows,
//            @RequestParam(value = "devCode",required = false) String devcode,
//            @RequestParam(value = "messageStatus",required = false) String messageStatus,
//            @RequestParam(value = "beginDate",required = false) String beginDate,
//            @RequestParam(value = "endDate",required = false) String endDate,
//            @RequestParam(value = "id",required = false) String id,
//            @RequestParam(value = "roadName",required = false) String roadName,
//            HttpServletResponse response
//    ) throws IOException {
//        try {
//            liquidDeviceRequest.setPage(page);
//            liquidDeviceRequest.setRows(rows);
//            liquidDeviceRequest.setDevCode(devcode);
//            liquidDeviceRequest.setId(Long.valueOf(
//                    StringUtils.isBlank(id) ? "0" : id
//            ));
//            liquidDeviceRequest.setTurnX(roadName);
//            liquidDeviceRequest.setMessageStatus(messageStatus);
//            liquidDeviceRequest.setBeginDate(beginDate);
//            liquidDeviceRequest.setEndDate(endDate);
//            JSONTool.writeJsonResult(response,
//                    liquidDeviceRequest.queryRecord());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @RequestMapping("getLiquidAlarmRecordList")
    public void getLiquidAlarmRecordList(
            @RequestParam(value = "page",required = true) String page,
            @RequestParam(value = "rows",required = true) String rows,
            @RequestParam(value = "roadName",required = false) String roadName,
            HttpServletResponse response
    ) throws IOException {
        try {
           Map map = adDjLiquidManager.getLiquidInfo(roadName,Integer.parseInt(page),Integer.parseInt(rows));
            JSONTool.writeJsonResult(response,new Gson().toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
