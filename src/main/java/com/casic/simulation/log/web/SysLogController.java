package com.casic.simulation.log.web;

import com.casic.simulation.log.manager.SysLogManager;
import com.casic.simulation.permission.UserObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/29.
 */
@Controller
@RequestMapping("SysLog")
public class SysLogController {

    @Resource
    private SysLogManager sysLogManager;

    @RequestMapping("saveSysLog")
    @ResponseBody
    public void saveSysLog(
            @RequestParam(value = "businessName") String businessName,
            @RequestParam(value = "operationType") String operationType,
            @RequestParam(value = "content") String content,HttpSession session
    ) {
        UserObj curUser = (UserObj) session.getAttribute(
                UserObj.SESSION_ATTRIBUTE_KEY
        );//获取用户
        String userName = null;
        if(curUser==null) return;
        userName = curUser.getUserName();
        sysLogManager.saveSysLog(businessName, operationType, content,userName);
    }

    @RequestMapping("SysLogList")
    @ResponseBody
    public Map<String, Object> getLogList(@RequestParam(value = "page", required = true) String page,
                                          @RequestParam(value = "rows", required = true) String rows,
                                          @RequestParam(value = "createUser",required = false)String createUser,
                                          @RequestParam(value = "beginDate", required = false) String beginDate,
                                          @RequestParam(value = "endDate", required = false) String endDate){
        Map<String,Object> result = sysLogManager.pageQuerySysLogList(Integer.parseInt(page),Integer.parseInt(rows),createUser,beginDate,endDate);
        return result;
    }
}
