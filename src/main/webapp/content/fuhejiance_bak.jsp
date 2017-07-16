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
<div id="fuhejiance-tabs" class="easyui-tabs" style="height: 100%" fit="true">
    <div title="报警管理" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="liquid-alarm-record-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#liquid-alarm-record-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'devCode',width:80,align:'center'">设备编号</th>
                    <th data-options="field:'devTypeName',width:100,align:'center'">设备类型</th>
                    <th data-options="field:'pipeType',width:80,align:'center'">管线类型</th>
                    <th data-options="field:'pos',width:140,align:'center'">报警位置</th>
                    <th data-options="field:'itemValue',width:60,align:'center'">当前值(m)</th>
                    <th data-options="field:'thresh',width:60,align:'center'">阈值(m)</th>
                    <th data-options="field:'alarmTime',width:100,align:'center'">报警时间</th>
                    <th data-options="field:'message',width:100,align:'center'">报警内容</th>
                    <th data-options="field:'messageStatus',width:80,align:'center'">处理状态</th>
                </tr>
                </thead>
            </table>
            <div id="liquid-alarm-record-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备类型:</label>
                <input class="easyui-textbox" value="全量程液位监测仪" style="width:10%" readonly>
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="liquid-alarm-record-query-devCode" style="width:15%" data-options="prompt:'请输入..'">
                <label style="vertical-align: middle;margin-left: 10px;">处理状态：</label>
                <input id="liquid-alarm-record-query-messageStatus" class="easyui-combobox" style="width:10%" data-options="
                            valueField:'id',
                            textField:'messageStatusName'">
                <label style="margin-left: 20px;">开始日期：</label>
                <input id="liquid-alarm-record-query-startdate" class="easyui-datebox" style="width:110px">
                <label style="margin-left: 10px;">结束日期：</label>
                <input id="liquid-alarm-record-query-enddate" class="easyui-datebox" style="width:110px">
                <a href="#" class="easyui-linkbutton" id="liquid-alarm-record-query" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="全量程液位监测仪" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="liquid-device-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#liquid-device-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id'" hidden="hidden">ID</th>
                    <th data-options="field:'devCode',width:80,align:'center'">设备编号</th>
                    <th data-options="field:'devTypeName',width:100,align:'center'">设备类型</th>
                    <th data-options="field:'pos',width:180,align:'center'">安装位置</th>
                    <th data-options="field:'liuqid',width:80,align:'center'">液位值(m)</th>
                    <th data-options="field:'lastTime',width:100,align:'center'">上传时间</th>
                    <th data-options="field:'cell',width:80,align:'center'">电量(%)</th>
                    <th data-options="field:'devStatus',width:80,align:'center'">设备状态</th>
                </tr>
                </thead>
            </table>
            <div id="liquid-device-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="liquid-device-query-devCode" style="width:15%" data-options="prompt:'请输入设备编号..'">
                <a href="#" class="easyui-linkbutton" id="liquid-device-query" iconCls="icon-search">查询</a>
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
                    <a href="#" id="overflow-curve-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
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

<script src="${ctx}/script/content/fuhejiance.js"></script>
<script>
    parent.IndexJS.expendSouthPanel(260);

    $(function () {
        overflow.init();
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