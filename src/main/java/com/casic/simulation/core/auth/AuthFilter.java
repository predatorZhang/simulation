package com.casic.simulation.core.auth;

import com.casic.simulation.permission.HttpUrlSourceFetcher;
import com.casic.simulation.permission.UserObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by admin on 2015/3/3.
 */
public class AuthFilter implements Filter

{
    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    @Resource
    private HttpUrlSourceFetcher userFetcher;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException
    {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        HttpSession session = request.getSession(false);
        if (session == null || session
                .getAttribute(UserObj.SESSION_ATTRIBUTE_KEY) == null) {
            String userName = request.getUserPrincipal().getName();
            UserObj userObj = userFetcher.getSource(userName, "default");
            session.setAttribute(UserObj.SESSION_ATTRIBUTE_KEY, userObj);
            //TODO LIST:
        }
        request.setAttribute("rsURL", rsURL);
        request.setAttribute("logoutURL", logoutURL);
        request.setAttribute("casURL", casURL);
        filterChain.doFilter(request, response);
    }

    private String rsURL;

    public void setRsURL(String rsURL) {
        this.rsURL = rsURL;
    }

    private String logoutURL;

    public void setLogoutURL(String logoutURL) {
        this.logoutURL = logoutURL;
    }

    private String casURL;

    public void setCasURL(String casURL) {
        this.casURL = casURL;
    }
}
