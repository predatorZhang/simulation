<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">

    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="supply-water-breakage-tabs" class="easyui-tabs" style="height: 100%" fit="true">
    <div title="噪声监测仪报警列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="noise-alarm-record-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#noise-alarm-record-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true">ID</th>
                    <th data-options="field:'devCode',align:'center'" width="10%">设备编号</th>
                    <th data-options="field:'devTypeName',align:'center'" width="15%">设备类型</th>
                    <th data-options="field:'pos',align:'center'" width="15%">设备位置</th>
                    <th data-options="
                            field:'itemValue',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.itemValue);
                            }" width="15%">噪声幅度值</th>
                    <th data-options="
                            field:'thresh',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.thresh);
                            }" width="15%">噪声幅度阈值</th>
                    <th data-options="
                            field:'alarmTime',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.alarmTime);
                            }" width="15%">噪声上传时间</th>
                    <th data-options="field:'messageStatus',align:'center'" width="15%">处理状态</th>
                </tr>
                </thead>
            </table>
            <div id="noise-alarm-record-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="noise-alarm-record-query-devCode" style="width:15%" data-options="prompt:'请输入..'">
                <label style="vertical-align: middle;margin-left: 10px;">处理状态：</label>
                <input id="noise-alarm-record-query-messageStatus" class="easyui-combobox" style="width:10%" data-options="
                            valueField:'id',
                            textField:'messageStatusName'">
                <label style="margin-left: 20px;">开始日期：</label>
                <input id="noise-alarm-record-query-startdate" class="easyui-datebox" style="width:110px">
                <label style="margin-left: 10px;">结束日期：</label>
                <input id="noise-alarm-record-query-enddate" class="easyui-datebox" style="width:110px">
                <a href="#" class="easyui-linkbutton" id="noise-alarm-record-query" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="噪声监测仪列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="noise-device-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#noise-device-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true">ID</th>
                    <th data-options="field:'devCode',align:'center'" width="10%">设备编号</th>
                    <th data-options="field:'devTypeName',align:'center'" width="15%">设备类型</th>
                    <th data-options="field:'pos',align:'center'" width="15%">设备位置</th>
                    <th data-options="
                            field:'noise',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.noise);
                            }" width="15%">噪声幅度值</th>
                    <th data-options="
                            field:'cell',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.cell);
                            }" width="15%">设备电量</th>
                    <th data-options="
                            field:'frequency',align:'center',
                            formatter : function(value, row, index) {
                                return breakage.getDefaultValue(row.frequency);
                            }" width="15%">噪声频率值</th>
                    <th data-options="field:'devStatus',align:'center'" width="15%">设备状态</th>
                </tr>
                </thead>
            </table>
            <div id="noise-device-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="noise-device-query-devCode" style="width:15%" data-options="prompt:'请输入设备编号..'">
                <a href="#" class="easyui-linkbutton" id="noise-device-query" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="设备曲线" style="padding:3px">
        <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north'" style="padding:3px;height:35px;">
                    <input type="hidden" id="device-curve-query-rsURL"/>
                    <input type="hidden" id="device-curve-series-means"/>
                    <label style="vertical-align: middle;">设备编号：</label>
                    <input id="device-curve-query-devCode" class="easyui-textbox" style="width:15%" readonly>
                    <label style="margin-left: 20px;">开始日期：</label>
                    <input id="device-curve-query-startdate" class="easyui-datebox" style="width:110px">
                    <label style="margin-left: 10px;">结束日期：</label>
                    <input id="device-curve-query-enddate" class="easyui-datebox" style="width:110px">
                    <a href="#" id="breakage-curve-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                </div>
                <div data-options="region:'center'" style="padding:2px">
                    <div id="device-curve-echarts" class="full-inside-container"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<input type="text" class="hidden" id="context" value="${ctx}"/>
<input type="text" class="hidden" id="rsURL" value="${rsURL}"/>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;
         width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
</body>
</html>

<script src="${ctx}/script/easyui/jquery.min.js"></script>
<script src="${ctx}/script/easyui/jquery.easyui.min.js"></script>
<script src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src='${ctx}/script/echarts/echarts.min.js'></script>

<script src="${ctx}/script/content/breakage.js"></script>
<script>
    parent.IndexJS.expendSouthPanel(260);

    $(function () {
        breakage.init();
    });

    function closes() {
        $("#Loading").fadeOut("normal", function () {
            $(this).remove();
        });
    }

    document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
        if (document.readyState == "complete") { // 当页面加载完成之后执行
            closes();
        }
    }
</script>