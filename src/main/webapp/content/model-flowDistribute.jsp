<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <title>流程配置</title>
    <!-- 引用easyUI样式 -->
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout">
<input id="ctx" type="hidden" value="${ctx}">
<div data-options="region:'center'" style="width:100%;height:100%;">
    <table id="distribute-table" class="easyui-datagrid"
           data-options="
           rownumbers:true,
           singleSelect:true,
           method:'get',
           toolbar:'#distribute-table-tool',
           pagination:true,
           fitColumns:true,
           fit:true">
        <thead>
        <tr>
            <th data-options="field:'eventTypeDesc',width:110">事件类型</th>
            <th data-options="field:'flowName',width:180">流程名称</th>
            <th data-options="field:'createTime',width:150">创建时间</th>
            <th data-options="field:'updateTime',width:150">更新时间</th>
        </tr>
        </thead>
    </table>
    <div id="distribute-table-tool" style="padding:2px 5px;">
        <label style="vertical-align: middle;">事件类型:</label>
        <input id="event-type-combox" name="name"
               class="easyui-combobox" style="width: 120px;"
               data-options="
                    valueField:'index',
                    textField:'typeName',
                    editable: false,
                    url:'${ctx}/event/queryAllEventType.do'"/>
        <a href="#" id="distribute-query"
           class="easyui-linkbutton" iconCls="icon-search">查询</a>
    </div>
</div>

<div id="infowindow" class="easyui-window"
     data-options="
            modal:true,
            closed:true,
            iconCls:'icon-save',
            resizable:false,
            minimizable:false,
            maximizable:false"
     style="width:500px;height:220px;padding:0px;">
    <div class="easyui-panel" style="width:100%;height:100%;padding:30px 60px;">
        <form id="addFlowDistribute" method="post">
            <input type="hidden" id="distributeID" name="distributeID"/>
            <div style="margin-bottom:20px">
                <input id="event-type-left-combox"
                       label="事件类型:" name="typeID"
                       class="easyui-combobox" style="width:100%"
                       data-options="
                            valueField:'index',
                            textField:'typeName',
                            editable: false,
                            required:true,
                            url:'${ctx}/event/queryEventType.do'"/>
            </div>
            <div style="margin-bottom:20px">
                <input id="flow-combox"
                       label="流程名称:" name="flowID"
                       class="easyui-combobox" style="width:100%"
                       data-options="
                            valueField:'id',
                            textField:'flowName',
                            editable: false,
                            required:true,
                            url:'${ctx}/flow/queryAllFlow.do'"/>
            </div>
        </form>
        <div style="text-align:center;padding:5px 0">
            <a href="javascript:void(0)" id="add-distribution-button" class="easyui-linkbutton" style="width:80px">增加</a>
        </div>
    </div>
</div>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
</body>
</html>

<!-- core js jquery 1.11 -->
<script src='${ctx}/script/easyui/jquery.min.js'></script>
<script src='${ctx}/script/easyui/jquery.easyui.min.js'></script>
<script src='${ctx}/script/easyui/locale/easyui-lang-zh_CN.js'></script>

<script src='${ctx}/script/content/model-flowDistribute.js'></script>

<script>

    $(function(){
        FlowDistribute.init();
    })

    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
</script>