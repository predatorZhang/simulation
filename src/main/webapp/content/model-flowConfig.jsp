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
<div data-options="region:'center'" style="width: 100%;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'south',title:'完整流程'" style="height:190px">
            <div style="text-align:center;width: 100%;height: 100%;">
                <img src="${ctx}/images/workflow/tempflow.png" style="margin-top:8px;"/>
            </div>
        </div>
        <div data-options="region:'center'">
            <table id="flow-table" class="easyui-datagrid"
                   style="width:100%;height:100%;"
                   data-options="
                        rownumbers: true,
                        singleSelect: false,
                        animate: true,
                        collapsible: true,
                        fitColumns: true,
                        selectOnCheck: false,
                        toolbar:'#flow-table-tool',
                        method:'get',
                        rowStyler: function(index,row){
                            if (row.active){
                                return 'background-color:#6293BB;color:#fff;font-weight:bold;';
                            }
                        }">
                <thead>
                <tr>
                    <th data-options="field:'active',checkbox:true"></th>
                    <th data-options="field:'flowName',width:60">流程名称</th>
                    <th data-options="field:'descn',width:180,align:'left'">备注</th>
                    <th data-options="field:'operations',width:180">包含操作</th>
                </tr>
                </thead>
            </table>
            <div id="flow-table-tool" style="padding:2px 5px;">
                <a href="#" id="takeEffect"
                   class="easyui-linkbutton" iconCls="icon-ok">生效</a>
            </div>
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

<script>
    $(function(){
        $("#flow-table").datagrid({
            url: $("#ctx").val() + '/flow/flow-list.do'
        });
        $("#takeEffect").click(fire);
    });

    function fire() {
        var ids = new Array();
        /**
         * 由于easyui本身getchecked方法的问题
         * 以下代码越过easyui的api
         */
        var rows = $("#flow-table").datagrid('getRows');
        $("input[name='active']").each(function (i) {
            if($(this).is(':checked')){
                ids.push(rows[i].id);
            }
        });
        if ($.inArray(${defaultFlow.id}, ids) < 0) {
            alert("[${defaultFlow.flowName}]必须有效，请重新选择");
            return;
        }
        $.ajax({
            type: "POST",
            url: $("#ctx").val() + "/flow/fire-flow.do",
            data: {flows:ids},
            dataType: "json",
            success: function(result) {
                if (!result.success) {
                    $.messager.alert('结果', result.msg);
                }
                parent.gisTools.addSysLog(
                        "处置流程管理", "modify",
                                "激活流程:[" + ids.toString()
                                + "],结果:" + result.msg);
                $("#flow-table").datagrid("reload");
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
</script>