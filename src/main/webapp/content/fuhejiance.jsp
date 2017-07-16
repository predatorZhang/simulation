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
    <div title="过载节点信息" style="padding:3px">
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
                    <th data-options="field:'code',width:80,align:'center'" hidden="hidden">code</th>
                    <th data-options="field:'roadName',width:80,align:'center'">所属街道</th>
                    <th data-options="field:'relateWell',width:100,align:'center'">关联管井</th>
                    <th data-options="field:'texture',width:80,align:'center'">管线材质</th>
                    <th data-options="field:'wellDepth',width:140,align:'center'">井深(mm)</th>
                    <th data-options="field:'liquidDepth',width:60,align:'center'">液位(mm)</th>
                    <th data-options="field:'proba',width:100,align:'center'">过载可能性</th>
                </tr>
                </thead>
            </table>
            <div id="liquid-alarm-record-list-toolbar" style="height:auto">
                <label style="vertical-align: middle;">所属街道:</label>
                <input class="easyui-textbox" id="road" name="roadName" style="width:10%" >
                <a href="#" class="easyui-linkbutton" id="liquid-alarm-record-query" iconCls="icon-search">查询</a>
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