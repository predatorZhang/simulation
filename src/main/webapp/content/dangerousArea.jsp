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
    <table id="danger-area-dg" class="easyui-datagrid" data-options="rownumbers:true,singleSelect:true,toolbar:'#danger-area-tb',pagination:true,fitColumns:true,fit:true,title:'危险区域管理'">
        <thead>
        <tr>
            <th data-options="field:'dbId',width:80" hidden="true">ID</th>
            <th data-options="field:'areaName',width:30">名称</th>
            <th data-options="field:'areaGrade',width:20">风险等级</th>
            <th data-options="field:'location',width:50" hidden="true">位置</th>
            <%--<th data-options="field:'fileName',width:50" hidden="true">模型路径</th>--%>
            <th data-options="field:'description',width:80,formatter:formatCellTooltip">描述</th>
            <%--<th data-options="field:'editBtn',width:50">编辑</th>--%>
            <%--<th data-options="field:'deleteBtn',width:50">删除</th>--%>
        </tr>
        </thead>
    </table>
        <div id="danger-area-tb" style="padding:3px;">
            <label style="vertical-align: middle;">危险区域名称:</label>
            <input id="query-area-name" class="easyui-textbox" name="name" style="width:15%" data-options="prompt:'请输入..'">
            <label style="vertical-align: middle;">危险区域等级:</label>
            <select id="query-area-level" class="easyui-combobox" panelHeight="auto" style="width:10%" editable="false" >
                <option value="">全部</option>
                <option value="一级">一级</option>
                <option value="二级">二级</option>
                <option value="三级">三级</option>
            </select>
            <label style="vertical-align: middle;">危险区域描述:</label>
            <input id="query-area-desc" class="easyui-textbox" name="name" style="width:15%" data-options="prompt:'请输入..'">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="DangerArea.query()">查询</a>
        </div>
</div>

  <div id="area-edit-dialog" class="easyui-dialog" title="危险区域编辑" style="width:400px;height:250px;padding-left: 20px;display: none;"
       data-options="
                closed: true,
                closable: true,
				buttons: [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						DangerArea.saveEdit();
					}
				},{
					text:'取消',
					handler:function(){
						$('#area-edit-dialog').dialog('close');
						$('#edit-area-info').form('clear');
					}
				}]
			">
      <form id="edit-area-info" enctype="multipart/form-data" method="post">
          <input id="edit-area-dbId" name="dbId" hidden="hidden" label="dbId">
          <div style="margin-top:15px">
              <input id="edit-area-name" name="areaName" class="easyui-textbox" label="危险区域名称" style="width:90%" >
          </div>
          <div style="margin-top:15px">
              <select id="edit-area-level" name="areaGrade" class="easyui-combobox" label="危险区域等级" style="width:90%" editable="false" >
                  <option value="一级">一级</option>
                  <option value="二级">二级</option>
                  <option value="三级">三级</option>
              </select>
          </div>

          <div style="margin-top:15px">
              <input id="edit-area-desc" name="description" class="easyui-textbox" label="危险区域描述" style="width:90%" >
          </div>
      </form>
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
<script type="text/javascript" src="${ctx}/script/jquery/jquery.form.js"></script>
<script  type="text/javascript" src="${ctx}/script/content/dangerousArea.js"></script>
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
        }
    };
    DangerArea.init();
    function formatCellTooltip(value){
        return "<span title='" + value + "'>" + value + "</span>";
    }
    function refreshDg(){
        $('#danger-area-dg').datagrid("reload");
    }
</script>