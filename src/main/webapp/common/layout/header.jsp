<%@ page import="com.casic.simulation.permission.UserObj" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<div class="head-img">
    <span class="left-img"><img src="${ctx}/images/banner_L_img.png" style=" height:48px"/></span> 
   
    <span class="right-img"><img src="${ctx}/images/banner_R_img.png" style="height:48px"/></span>
</div>
<div class="back-img">
    <img style="height:48px" src="${ctx}/images/banner.png"/>
</div>
<div style="position: absolute; right: 15px; bottom:15px;">
    <span style="margin-right:25px; ">
        <label id="userName" style="font-weight: bold;">用户名:
        <%=((UserObj)session.getAttribute(
                UserObj.SESSION_ATTRIBUTE_KEY
        )).getUserName()%>
        </label>
    </span>
    <span style="cursor:pointer">
        <a href="javascript:void(0);" onclick="logoutWithConfirm();">
            注销
        </a>
    </span>
</div>
<script>
    function logoutWithConfirm() {
        if (window.confirm('您确定退出吗?')) {
            window.location.href = "${ctx}/user/logout.do";
        }
    }
</script>