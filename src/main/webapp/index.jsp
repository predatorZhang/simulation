<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" content="ie=edge"/>
    <title>宁波-事件仿真及协同处置系统</title>
    <link href="css/index.css" rel="stylesheet" type="text/css">
    <!-- core js jquery 1.11 -->
    <script src='script/easyui/jquery.min.js'></script>
    <script src='script/easyui/jquery.easyui.min.js'></script>

    <!-- 引用easyUI样式 -->
    <link href="script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="script/easyui/themes/icon.css" rel="stylesheet" type="text/css">

    <!-- 引入gis相关js -->
    <script  type="text/javascript" src="script/layer/layer.min.js"></script>

    <script type="text/javascript" src='script/gis/config.js'></script>
    <script type="text/javascript" src='script/gis/gis.js'></script>
    <script type="text/javascript" src='script/gis/gisTools.js'></script>
    <script type="text/javascript" src='script/gis/noneGisTools.js'></script>
    <script type="text/javascript" src='script/gis/gisEffect.js'></script>
</head>

<body class="easyui-layout">
<div data-options="region:'north'" style="height:50px;">
    <%@include file="/common/layout/header.jsp" %>
</div>

<div data-options="region:'west',title:'操作选项'" style="width: 205px;">
    <%@include file="/common/layout/left.jsp" %>
</div>

<div data-options="region:'center'">
    <div id="tab_div">
        <%@include file="/common/layout/content.jsp" %>
    </div>
</div>

<div id="closeValveDiv" title="需要关闭的阀门列表" style="padding:0px; overflow-y: auto" closed="true">
    <div style="width: 260px; height: 225px;">
        <table id="dg_closeValve"></table>
    </div>
</div>

<input type="hidden" id="context" value="${ctx}"/>
<input type="hidden" id="rsUrl" value="${rsURL}"/>
<input type="hidden" id="clickEvent" value=""/>

<script src='script/content/index.js'></script>
<script src='script/gis/deviceManager.js'></script>

<script type="text/javascript">

$(function () {
    IndexJS.showSensorMarker();
    IndexJS.init();
});

document.onreadystatechange = function() {
    if (document.readyState == "complete") {
        Gis.initEarth();
        Gis.loadData();
        Gis.initGisTools();

    }
}
</script>
</body>
</html>
