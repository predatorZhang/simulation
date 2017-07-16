package com.casic.simulation.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class RegionPermissionTag extends BodyTagSupport {
    public static final long serialVersionUID = 0L;
    private static Logger logger = LoggerFactory.getLogger(RegionPermissionTag.class);

    private String permission = null;
    private String region = null;

    public int doStartTag() throws JspException {

        Object object = this.pageContext.getSession().getAttribute(
                UserObj.SESSION_ATTRIBUTE_KEY
        );

        if (this.permission == null || object == null) {
            logger.error("permission should not be null");
            return 0;
        }

        UserObj currentUser = (UserObj) object;

        String text = ((PermissionDecorator) getBean(PermissionDecorator.class)).decode(this.permission, this.region);

        boolean authorized = ((PermissionChecker) getBean(PermissionChecker.class)).isAuthorized(text, currentUser);

        if (!authorized) {
            return 0;
        }

        return 1;
    }

    protected <T> T getBean(Class<T> clz) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());

        return ctx.getBean(clz);
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
