package com.casic.simulation.event.web;

import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.event.bean.AlarmRecordRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("alarm")
public class AlarmRecordController {

    @Resource
    private AlarmRecordRequest alarmRecordRequest;

    @RequestMapping("getMessageStatusList")
    public void getMessageStatusList(HttpServletResponse response)
            throws IOException {
        try {
            JSONTool.writeJsonResult(response,
                    alarmRecordRequest.queryMessageStatusList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
