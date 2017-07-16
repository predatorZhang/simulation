<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <title>日志管理</title>
    <!-- 引用easyUI样式 -->
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="width:100%;height:100%;">
    <table id="log_list" class="easyui-datagrid" data-options="
        rownumbers:true,
        singleSelect:true,
        method:'get',
        toolbar:'#log-table-tool',
        pagination:true,
        fitColumns:true,
        fit:true,
        title:'系统操作日志'">
        <thead>
        <tr>
            <th data-options="field:'businessName'" width="15%">业务名称</th>
            <th data-options="field:'content'" width="60%">操作内容</th>
            <th data-options="field:'createUser'" width="10%">操作人</th>
            <th data-options="field:'createTime'" width="15%">操作时间</th>
        </tr>
        </thead>
    </table>
    <div id="log-table-tool" style="padding:2px 5px;">
        <label style="vertical-align: middle;">操作人:</label>
        <input class="easyui-textbox" id="operator_name" name="name" style="width:15%" data-options="prompt:'请输入操作人'">
        <label style="margin-left: 20px;">开始时间:</label>
        <input class="easyui-datebox" id="date_start" style="width:15%">
        <label style="margin-left: 10px;">结束时间:</label>
        <input class="easyui-datebox" id="date_end" style="width:15%">
        <a href="#" id="btn_search_log" class="easyui-linkbutton" iconCls="icon-search">查询</a>
    </div>
</div>
<input type="text" class="hidden" id="context" value="${ctx}"/>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
</body>
</html>

<!-- core js jquery 1.11 -->
<script src='${ctx}/script/easyui/jquery.min.js'></script>
<script src='${ctx}/script/easyui/jquery.easyui.min.js'></script>
<script src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>

<script src="${ctx}/script/content/logManage.js"></script>
<script>
    $(function () {
        LogManage.init();
    });
    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            var start = $("#date_start"), end = $("#date_end");
            parent.gisTools.setStartAndEndDate(start, end, 7);
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
</script>