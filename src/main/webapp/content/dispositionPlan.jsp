<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>处置预案管理</title>
    <!-- 引用easyUI样式 -->
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="width:100%;height:100%;">
    <table id="plan-files" class="easyui-datagrid"
           data-options="
               rownumbers:true,
               singleSelect:true,
               method:'post',
               toolbar:'#plan-table-tool',
               pagination:true,
               fitColumns:true,
               fit:true,
               pageSize:5,
               pageList:[5,10],
               title:'预案管理'">
        <thead>
        </thead>
    </table>
    <div id="plan-table-tool" style="padding:2px 5px;">
        <label style="vertical-align: middle;">预案名称:</label>
        <input id="planName" class="easyui-textbox"
                style="width:15%" data-options="prompt:'请输入名称'">
        <label style="vertical-align: middle;">预案类型:</label>
        <input id="planType" class="easyui-combobox"
               style="width:10%;" data-options="
                    url:'${ctx}/fileController/queryPlanAllFileTypeJson.do',
                    method:'post',
                    editable:false,
                    valueField:'code',
                    textField:'name',
                    panelHeight:'auto',
					">
        <label style="margin-left: 20px;">上传日期 自:</label>
        <input id="date_start" class="easyui-datebox" style="width:110px">
        <label style="margin-left: 10px;">至: </label>
        <input id="date_end" class="easyui-datebox" style="width:110px">
        <a href="javascript:void(0);" id="queryBtn"
           class="easyui-linkbutton" iconCls="icon-search">查询</a>
        <a href="javascript:void(0);" onclick='addFile()'
           class="easyui-linkbutton" iconCls="icon-add">新增</a>
    </div>
</div>
<div id="plan-dialog" class="easyui-dialog" title="预案管理" style="width:400px;height:210px;padding-left: 20px;display: none;"
     data-options="
                closed: true,
                closable: true,
				buttons: [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						fileInfo.submit();
					}
				},{
					text:'取消',
					handler:function(){
						$('#plan-dialog').dialog('close');
						$('#plan-info').form('clear');
					}
				}]
			">
    <form id="plan-info" method="post" enctype="multipart/form-data">
        <div style="margin-top:15px">
            <input id="plan_info_name" class="easyui-textbox"
                   label="预案名称" style="width:90%"
                   name="fileName" required="true">
        </div>
        <div style="margin-top:15px">
            <input id="plan_info_type" class="easyui-combobox"
                   style="width:90%;" name="fileType"
                   data-options="
                    url:'${ctx}/fileController/queryPlanFileTypeJson.do',
                    method:'post',
                    editable:false,
                    valueField:'code',
                    textField:'name',
                    panelHeight:'auto',
                    label: '预案类型',
                    labelPosition: 'left'
					" required="true">
        </div>
        <div style="margin-top:15px">
            <input class="easyui-filebox" id="uploadFile"
                   buttonText="选择文件"
                   name="uploadFile" label="文档"
                   style="width:90%" required="true">
        </div>
    </form>
</div>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
<input id="context" type="hidden" value="${ctx}">
</body>
</html>
<!-- core js jquery 1.11 -->
<script src='${ctx}/script/easyui/jquery.min.js'></script>
<script src="${ctx}/script/jquery/jquery.form.js"></script>
<script src='${ctx}/script/easyui/jquery.easyui.min.js'></script>
<script src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>

<script src="${ctx}/script/content/dispositionPlan.js"></script>
<script>
    parent.IndexJS.expendSouthPanel(260);

    function addFile() {
        $('#plan-dialog').dialog('open');
    }

    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            parent.gisTools.setStartAndEndDate(
                    $("#date_start"), $("#date_end"), 7
            );
            fileInfo.init();
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
</script>