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
<div id="supply-water-leakage-tabs" class="easyui-tabs" style="height: 100%" fit="true">
    <div title="分区列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="parition-list"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   pagination:true,
                   fitColumns:true,
                   toolbar:'#parition-table-tool',
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'dmaId',hidden:true">分区ID</th>
                    <th data-options="field:'dmaInfoName',align:'center'" width="15%">分区名称</th>
                    <th data-options="field:'ReportDate',align:'center'" width="20%">评估日期</th>
                    <th data-options="field:'LeakRate',align:'center'" width="20%">漏损率(%)</th>
                    <th data-options="
                        field:'LeakControlRate',
                        align:'center',
                        formatter : function(value, row, index) {
                            if(row.LeakRate<10) {
                                return '正常'
                            } else if(row.LeakRate>=10 && row.LeakRate<20) {
                                return '轻微漏损'
                            } else{
                                return '严重漏损'
                            }
                        }
                    " width="20%">是否漏損</th>
                </tr>
                </thead>
            </table>
            <div id="parition-table-tool" style="padding:2px 5px;">
                <label style="margin-left: 10px;">开始时间:</label>
                <input class="easyui-datebox" id="date_start" style="width:15%">
                <label style="margin-left: 10px;">结束时间:</label>
                <input class="easyui-datebox" id="date_end" style="width:15%">
                <a href="#" id="btn_search_parition" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="报警列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="press-alarm-record-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#press-alarm-record-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'devCode',width:100,align:'center'">设备编号</th>
                    <th data-options="field:'devTypeName',width:100,align:'center'">设备类型</th>
                    <th data-options="field:'pos',width:120,align:'center'">安装位置</th>
                    <th data-options="field:'itemValue',width:80,align:'center'">报警值</th>
                    <th data-options="field:'thresh',width:80,align:'center'">报警下限</th>
                    <th data-options="field:'alarmTime',width:100,align:'center'">报警时间</th>
                    <th data-options="field:'message',width:100,align:'center'">报警消息</th>
                    <th data-options="field:'messageStatus',width:60,align:'center'">处理状态</th>
                </tr>
                </thead>
            </table>
            <div id="press-alarm-record-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备类型:</label>
                <input class="easyui-textbox" value="压力监测仪" style="width:10%" readonly>
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="press-alarm-record-query-devCode" style="width:15%" data-options="prompt:'请输入..'">
                <label style="vertical-align: middle;margin-left: 10px;">处理状态：</label>
                <input id="press-alarm-record-query-messageStatus" class="easyui-combobox" style="width:10%" data-options="
                            valueField:'id',
                            textField:'messageStatusName'">
                <label style="margin-left: 20px;">开始日期：</label>
                <input id="press-alarm-record-query-startdate" class="easyui-datebox" style="width:110px">
                <label style="margin-left: 10px;">结束日期：</label>
                <input id="press-alarm-record-query-enddate" class="easyui-datebox" style="width:110px">
                <a href="#" class="easyui-linkbutton" id="press-alarm-record-query" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="超声波流量监测仪" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="flow-device-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#flow-device-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true">ID</th>
                    <th data-options="field:'devCode',width:100,align:'center'">设备编号</th>
                    <th data-options="field:'devTypeName',width:100,align:'center'">设备类型</th>
                    <th data-options="field:'pos',width:140,align:'center'">安装位置</th>
                    <th data-options="field:'totalFlow',width:100,align:'center'">累计流量(m3)</th>
                    <th data-options="field:'insFlow',width:100,align:'center'">瞬时流量(m3/h)</th>
                    <th data-options="field:'cell',width:100,align:'center'">电池电量</th>
                    <th data-options="field:'lastTime',width:100,align:'center'">最后上传时间</th>
                    <th data-options="field:'devStatus',width:100,align:'center'">设备状态</th>
                </tr>
                </thead>
            </table>
            <div id="flow-device-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="flow-device-query-devCode" style="width:15%" data-options="prompt:'请输入设备编号..'">
                <a href="#" class="easyui-linkbutton" id="flow-device-query" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="压力监测仪" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="press-device-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#press-device-list-toolbar',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true">ID</th>
                    <th data-options="field:'devCode',width:100,align:'center'">设备编号</th>
                    <th data-options="field:'devTypeName',width:100,align:'center'">设备类型</th>
                    <th data-options="field:'pos',width:140,align:'center'">安装位置</th>
                    <th data-options="field:'press',width:100,align:'center'">压力(MPa)</th>
                    <th data-options="field:'cell',width:100,align:'center'">电池电量</th>
                    <th data-options="field:'lastTime',width:100,align:'center'">最后上传时间</th>
                    <th data-options="field:'devStatus',width:100,align:'center'">设备状态</th>
                </tr>
                </thead>
            </table>
            <div id="press-device-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">设备编号:</label>
                <input class="easyui-textbox" id="press-device-query-devCode" style="width:15%" data-options="prompt:'请输入设备编号..'">
                <a href="#" class="easyui-linkbutton" id="press-device-query" iconCls="icon-search">查询</a>
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
                    <a href="#" id="leakage-curve-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
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

<script src="${ctx}/script/content/leakage.js"></script>
<script>
    parent.IndexJS.expendSouthPanel(260);

    document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
        if (document.readyState == "complete") { // 当页面加载完成之后执行
            leakage.init();
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
    function initTab(){
        parent.gisTools.setStartAndEndDate(
                $("#device-curve-query-startdate"),
                $("#device-curve-query-enddate"), 7
        );
        $("#supply-water-leakage-tabs").tabs("select", 4);
        leakage.initCharts();
    }
</script>