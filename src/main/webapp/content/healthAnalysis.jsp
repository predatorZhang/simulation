<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctx}/script/easyui/themes/default/easyui.css" />
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">

    <style type="text/css">
        body,div,form{
            margin: 0;
            padding: 0;
        }

        table,td,tr {
            font-size: 12px;
        }

        input {
            width: 140px;
        }
    </style>
</head>
<body>
<div data-options="region:'center'" style="width:100%;height:100%;">
    <table id="health-dg" class="easyui-datagrid" data-options="rownumbers:true,singleSelect:true,toolbar:'#health-tb',pagination:true,fitColumns:true,fit:true,title:'健康度分析'">
        <thead>
        <tr>
            <th data-options="field:'pipeId',width:50">管线编号</th>
            <th data-options="field:'pipeType',width:50">管线类型</th>
            <th data-options="field:'detectTime',width:40">评估时间</th>
            <th data-options="field:'healthRank',width:30">健康状态</th>
            <th data-options="field:'result',width:100,formatter:formatCellTooltip">健康度描述</th>
            <th data-options="field:'suggestion',width:100,formatter:formatCellTooltip">管理建议</th>
            <%--<th data-options="field:'positionBtn',width:20">操作</th>--%>
        </tr>
        </thead>
    </table>
        <div id="health-tb" style="padding:3px;">
            <label style="vertical-align: middle;">管线编号:</label>
            <input id="query-pipeId" class="easyui-textbox" name="name" style="width:15%" data-options="prompt:'请输入..'">
            <label style="vertical-align: middle;">健康度等级:</label>
            <select id="query-health-status" class="easyui-combobox" panelHeight="auto" style="width:10%">
                <option value="">全部</option>
                <option value="严重疾病">严重疾病</option>
                <option value="疾病">疾病</option>
                <option value="亚健康">亚健康</option>
                <option value="健康">健康</option>
            </select>
            <label style="vertical-align: middle;">健康度描述:</label>
            <input id="query-health-desc" class="easyui-textbox" name="name" style="width:15%" data-options="prompt:'请输入..'">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="HealthAnalysis.query()">查询</a>
        </div>
    </div>

<input type="text" class="hidden" id="context" value="${ctx}"/>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;
         width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>

<script  type="text/javascript" src="${ctx}/script/content/healthAnalysis.js"></script>
<script>
    parent.IndexJS.expendSouthPanel(260);
    function closes() {
        $("#Loading").fadeOut("normal", function () {
            $(this).remove();
        });
    }

    document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
        if (document.readyState == "complete") { // 当页面加载完成之后执行
            closes();
            parent.IndexJS.expendSouthPanel(260);//230
        }
    };
    HealthAnalysis.init();
    function formatCellTooltip(value){
        return "<span title='" + value + "'>" + value + "</span>";
    }
</script>