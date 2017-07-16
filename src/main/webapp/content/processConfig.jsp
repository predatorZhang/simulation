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
<input id="ctx" type="hidden" value="${ctx}"/>
<div data-options="region:'west',title:'处置部门人员管理',collapsible: false" style="width:240px;">
    <ul id="department-person-tree" class="easyui-tree"
        data-options="
                url: '${ctx}/flow-dep-person/get-tree.do',
				method: 'get',
				animate: true,
				onContextMenu: function(e,node){
					e.preventDefault();
					$(this).tree('select',node.target);
					$('#menu-tool').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				},
				onSelect: function(e,node) {
				    PCManager.flashPage(node);
				}
			"></ul>
    <div id="menu-tool" class="easyui-menu" style="width:120px;">
        <div onclick="PCManager.appendDepartment()" data-options="iconCls:'icon-add'">增加部门</div>
        <div onclick="PCManager.appendPerson()" data-options="iconCls:'icon-add'">增加人员</div>
        <div class="menu-sep"></div>
        <div onclick="PCManager.removeit()" data-options="iconCls:'icon-remove'">删除</div>
    </div>

    <div id="dialog-add-department" class="easyui-dialog" style="width:100%;max-width:400px;padding:30px;"
         data-options="
                title:'新增部门',
                closable:true,
                closed:true,
                modal:true,
                buttons:'#dialog-add-department-tool'">
        <form id="form-add-department" class="easyui-form" method="post" data-options="novalidate:false">
            <input type="hidden" id="add-dep-depID" name="parentDepID"/>
            <div style="margin-bottom:20px">
                <input class="easyui-textbox" id="depName" name="depName" style="width:100%" data-options="label:'部门名称:',required:true">
            </div>
            <div style="margin-bottom:20px">
                <input id="add-dep-deplist-combox" name="sysDepID"
                       class="easyui-combobox" style="width: 100%;"
                       data-options="
                            label:'对应全局部门:',
                            valueField:'id',
                            textField:'name',
                            editable: false,
                            required:true,
                            url:'${ctx}/flow-dep-person/get-sys-deps.do'"/>
            </div>
        </form>
    </div>
    <div id="dialog-add-department-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:PCManager.newDepartment()">确定</a>
    </div>

    <div id="dialog-add-person" class="easyui-dialog" style="width:100%;max-width:400px;padding:30px;"
         data-options="
                title:'新增人员',
                closable:true,
                closed:true,
                modal:true,
                buttons:'#dialog-add-person-tool'">
        <form id="form-add-person" class="easyui-form"
              action="${ctx}/flow-dep-person/add-person.do"
              method="post" data-options="novalidate:false">
            <input type="hidden" id="add-person-depID" name="belongToDepNameID"/>
            <div style="margin-bottom:20px">
                <input class="easyui-textbox" id="belongToDepName" style="width:100%" data-options="label:'所属部门:',required:true" readonly>
            </div>
            <div style="margin-bottom:20px">
                <input id="add-person-personlist-combox" name="sysPersonID"
                       class="easyui-combobox" style="width: 100%;"
                       data-options="
                            label:'人员名称:',
                            valueField:'id',
                            textField:'username',
                            groupField:'depName',
                            editable: false,
                            required:true,
                            url:'${ctx}/flow-dep-person/get-sys-persons.do'"/>

            </div>
        </form>
    </div>
    <div id="dialog-add-person-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:PCManager.newPerson()">确定</a>
    </div>
</div>

<div data-options="region:'center'" title="流程规则管理" style="width: 100%;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <table id="rule-table" class="easyui-datagrid"
                   style="width:100%;height:100%;"
                   data-options="
                        rownumbers:true,
                        singleSelect:true,
                        pagination:true,
                        toolbar:'#rule-table-tool',
                        url:'${ctx}/node-limit/node-limie-list.do',
                        method:'post'">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true" width="20%">规则ID</th>
                    <th data-options="field:'nodeName'" width="20%">关联流程任务</th>
                    <th data-options="field:'eventTypeName'" width="20%">事件类别</th>
                    <th data-options="field:'flowPersonName'" width="20%">处理人员</th>
                    <th data-options="field:'flowDepName'" width="20%">处置部门</th>
                </tr>
                </thead>
            </table>
            <div id="rule-table-tool" style="padding:2px 5px;">
                <input id="rule-query-task" class="easyui-combobox"
                       style="width: 20%;"
                       data-options="
                            label:'关联流程任务:',
                            valueField:'id',
                            textField:'nodeName',
                            editable: false,
                            required:true,
                            url:'${ctx}/node-limit/queryNode.do'"/>
                <label style="vertical-align: middle;margin-left: 20px;">事件类型:</label>
                <input id="rule-query-eventtype"
                       class="easyui-combobox"
                       style="width: 15%;"
                       data-options="
                            valueField:'index',
                            textField:'typeName',
                            editable: false,
                            required:true,
                            url:'${ctx}/event/queryAllEventType.do'"/>
                <label style="vertical-align: middle;margin-left: 20px;">处置部门:</label>
                <input id="rule-query-dep" class="easyui-combobox"
                       style="width: 15%;"
                       data-options="
                            valueField:'id',
                            textField:'depName',
                            editable: false,
                            required:true,
                            url:'${ctx}/node-limit/queryAllDep.do'"/>
                <a href="#" id="node-limit-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                <a href="#" id="node-limit-modify" class="easyui-linkbutton" iconCls="icon-edit">更改</a>
            </div>
        </div>
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
     style="width:60%;height:80%;padding:0px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',collapsible: false"
             style="width:50%;height:100%;padding:60px 60px;">
            <form id="modify-rule-form" method="post">
                <div style="margin-bottom:20px">
                    <input id="related-task-modify"
                           label="关联流程任务:" name="nodeID"
                           style="width:100%"/>
                </div>
                <div style="margin-bottom:20px">
                    <input id="event-type-left-modify"
                           label="事件类型:" name="typeID"
                           style="width:100%"/>
                </div>
            </form>
            <div style="text-align:center;padding:5px 0">
                <a href="javascript:void(0)" id="query-rule-button" class="easyui-linkbutton" style="width:80px">查询</a>
                <a href="javascript:void(0)" id="modify-rule-button" class="easyui-linkbutton" style="width:80px">修改</a>
            </div>
        </div>
        <div data-options="region:'center',collapsible:false,fit:true">
            <input type="hidden" id="related-task-choosen"/>
            <input type="hidden" id="event-type-left-choosen"/>
            <ul id="department-person-modify-tree" class="easyui-tree"></ul>
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

<script src='${ctx}/script/content/processConfig.js'></script>

<script>
    $(function(){
        PCManager.init();
    });

    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            $("#Loading").fadeOut("normal", function () {
                $(this).remove();
            });
        }
    }
</script>