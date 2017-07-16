package com.casic.simulation.permission.web;

import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.log.manager.SysLogManager;
import com.casic.simulation.permission.HttpUrlSourceFetcher;
import com.casic.simulation.permission.UserObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class LoginController {

    private HttpUrlSourceFetcher userFetcher;

    @Resource
    private SysLogManager logManager;

    @Resource
    public void setUserFetcher(HttpUrlSourceFetcher userFetcher) {
        this.userFetcher = userFetcher;
    }

    @RequestMapping("login")
    @ResponseBody
    public Map login( @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpSession session) {

        Map map = new HashMap();
        UserObj userObj =  userFetcher.getSource(username, password);
        if (StringUtils.isEmpty(userObj.getPassword())) {
            map.put("success",false);
            map.put("message","error");
            logManager.saveSysLog(
                    "登录信息", "login",
                    "登录失败{name:" + username +
                            ",password:" + password + "}",
                    username);
            return map;
        }

        map.put("success",true);
        map.put("message","success");
        map.put("user", userObj);
        session.setAttribute(UserObj.SESSION_ATTRIBUTE_KEY, userObj);
        logManager.saveSysLog(
                "登录信息", "login[" + password + "]",
                "登录成功{name:" + username + "}",
                username);
        return map;
    }

    @RequestMapping("logout")
    @ResponseBody
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect(casURL + "/logout?service=" + logoutURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${logout.url}")
    private String logoutURL;

    @Value("${cas.server}")
    private String casURL;

}
