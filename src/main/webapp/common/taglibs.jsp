<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%pageContext.setAttribute("ctx", request.getContextPath());%>
<%pageContext.setAttribute("locale", request.getLocale());%>
<%pageContext.setAttribute("rsURL", request.getAttribute("rsURL"));%>
<%pageContext.setAttribute("logoutURL", request.getAttribute("logoutURL"));%>
<%pageContext.setAttribute("casURL", request.getAttribute("casURL"));%>
