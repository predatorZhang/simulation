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
<div id="event-manage-tabs" class="easyui-tabs" style="height: 100%" fit="true">
    <div title="设备事件列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
        <table id="device-event-list" class="easyui-datagrid"
               data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#device-event-tb',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
            <thead>
            <tr>
                <th data-options="field:'id'" hidden="hidden"></th>
                <th data-options="field:'deviceCode',width:40">设备编号</th>
                <th data-options="field:'recordDate',width:50">发生时间</th>
                <th data-options="field:'roadName',width:50">事件位置</th>
                <th data-options="field:'message',width:40">事件类型</th>
                <th data-options="field:'messageStatus',width:30">处理状态</th>
                <th data-options="field:'operate',width:30">操作</th>
            </tr>
            </thead>
        </table>
        <div id="device-event-tb" style="padding:3px;">
            <label style="vertical-align: middle;">事件类型:</label>
            <select class="easyui-combobox"
                    panelHeight="auto"
                    data-options="editable: false"
                    id="device_event_type" style="width:10%">
                <option value="" selected>全部</option>
                <option value="液位超限">液位超限</option>
                <option value="管线泄漏">管线泄漏</option>
                <option value="压力超限">压力超限</option>
                <option value="浓度超限">浓度超限</option>
                <option value="井盖开启">井盖开启</option>
            </select>
            <label style="vertical-align: middle;">事件状态:</label>
            <%-- TODO:上面的事件类型和事件状态都要改为动态请求--%>
            <select class="easyui-combobox"
                    data-options="editable: false"
                    id="device_event_status"
                    panelHeight="auto" style="width:10%">
                <option value="2" selected>待派发</option>
                <option value="3">待签收</option>
                <option value="4">待处理</option>
                <option value="5">待备案</option>
                <option value="6">完成</option>
            </select>
            <label style="margin-left: 20px;">开始日期:</label>
            <input id="device_event_begin" class="easyui-datebox" style="width:110px">
            <label  style="margin-left: 10px;">结束日期: </label>
            <input id="device_event_end" class="easyui-datebox" style="width:110px">
            <a id="device_event_query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    </div>
    <div title="人工上报事件列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="person-event-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#person-event-tb',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'id'" hidden="hidden"></th>
                    <th data-options="field:'message',width:40">事件类型</th>
                    <th data-options="field:'recordDate',width:50">发生时间</th>
                    <th data-options="field:'location',width:50">事件位置</th>
                    <th data-options="field:'longitude',width:30">经度</th>
                    <th data-options="field:'latitude',width:30">纬度</th>
                    <th data-options="field:'messageStatus',width:30">处理状态</th>
                    <th data-options="field:'operate',width:30">操作</th>
                </tr>
                </thead>
            </table>
            <div id="person-event-tb" style="padding:3px;">
                <label style="vertical-align: middle;">事件状态:</label>
                <%-- TODO:上面的事件类型和事件状态都要改为动态请求--%>
                <select id="person_event_status"
                        data-options="editable: false"
                        class="easyui-combobox"
                        panelHeight="auto" style="width:10%">
                    <%--<option value="">全部</option>--%>
                    <option value="2">待派发</option>
                    <option value="3">待签收</option>
                    <option value="4">待处理</option>
                    <option value="5">待备案</option>
                    <option value="6">完成</option>
                </select>
                <label style="margin-left: 20px;">开始日期:</label>
                <input id="person_event_begin" class="easyui-datebox" style="width:110px">
                <label style="margin-left: 10px;">结束日期: </label>
                <input id="person_event_end" class="easyui-datebox" style="width:110px">
                <a id="person_event_query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
    <div title="PAD上报事件列表" style="padding:3px">
        <div data-options="region:'center'" style="width:100%;height:100%;">
            <table id="pad-event-list" class="easyui-datagrid"
                   data-options="
                   rownumbers:true,
                   singleSelect:true,
                   method:'post',
                   toolbar:'#pad-event-tb',
                   pagination:true,
                   fitColumns:true,
                   pageSize:5,
                   pageList:[5,10],
                   fit:true">
                <thead>
                <tr>
                    <th data-options="field:'dbId'" hidden="hidden"></th>
                    <th data-options="field:'patrolName',width:40">巡检员</th>
                    <th data-options="field:'eventTime',width:50">发生时间</th>
                    <th data-options="field:'latitude',width:50">纬度</th>
                    <th data-options="field:'longitude',width:50">经度</th>
                    <th data-options="field:'descripe',width:100,formatter:formatCellTooltip">事件描述</th>
                    <th data-options="field:'status',width:30">处理状态</th>
                    <th data-options="field:'operate',width:30">操作</th>
                </tr>
                </thead>
            </table>
            <div id="pad-event-tb" style="padding:3px;">
                <label style="vertical-align: middle;">事件状态:</label>
                <%-- TODO:上面的事件类型和事件状态都要改为动态请求--%>
                <select id="pad_event_status"
                        class="easyui-combobox"
                        data-options="editable: false"
                        panelHeight="auto" style="width:10%">
                    <%--<option value="">全部</option>--%>
                    <option value="2">待派发</option>
                    <option value="3">待签收</option>
                    <option value="4">待处理</option>
                    <option value="5">待备案</option>
                    <option value="6">完成</option>
                </select>
                <label style="margin-left: 20px;">开始日期:</label>
                <input id="pad_event_begin" class="easyui-datebox" style="width:110px">
                <label style="margin-left: 10px;">结束日期: </label>
                <input id="pad_event_end" class="easyui-datebox" style="width:110px">
                <a id="pad_event_query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            </div>
        </div>
    </div>
</div>


  <div id="send" class="easyui-dialog" title="流转派发" style="width:400px;height:200px;padding-left: 20px;display: none;"
       data-options="
                closed: true,
                closable: true,
				buttons: [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						EventManager.distribute();
					}
				},{
					text:'取消',
					handler:function(){
						$('#send').dialog('close');
						$('#send-info').form('clear');
					}
				}]
			">
      <form id="send-info" method="post">
          <div style="margin-top:15px">
              <input id="send_person"
                     class="easyui-combobox"
                     data-options="editable: false"
                     label="选择巡检员" style="width:90%">
          </div>
          <input id="send_event_id" hidden="hidden">
          <input id="send_event_src" hidden="hidden">
          <div style="margin-top:15px">
              <input id="send_result" class="easyui-textbox" label="分析结果" style="width:90%" >
          </div>
      </form>
  </div>

  <div id="backup" class="easyui-dialog" title="备案" style="width:400px;height:250px;padding-left: 20px;display: none;"
       data-options="
                closed: true,
                closable: true,
				buttons: [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
					   EventManager.backup();
					}
				},{
					text:'取消',
					handler:function(){
						$('#backup').dialog('close');
						$('#backup-info').form('clear');
					}
				}]
			">
      <form id="backup-info" enctype="multipart/form-data" method="post">
          <div style="margin-top:15px">
              <select id="backup_info"
                      name="backupInfo"
                      class="easyui-combobox"
                      data-options="editable: false"
                      label="备案信息" style="width:90%">
                  <option value="管线爆管">管线爆管</option>
                  <option value="管井损坏">管井损坏</option>
                  <option value="井盖丢失">井盖丢失</option>
              </select>
          </div>
          <div style="margin-top:15px">
              <input id="backup_reason" name="backupReason" class="easyui-textbox" label="事件原因" style="width:90%" >
          </div>
          <div style="margin-top:15px">
              <select id="backup_measure"
                      name="backupMeasure"
                      data-options="editable: false"
                      class="easyui-combobox"
                      label="采取措施" style="width:90%">
                  <option value="断水抢修">断水抢修</option>
                  <option value="关阀维护">关阀维护</option>
                  <option value="重新购买">重新购买</option>
              </select>
          </div>
          <%--<div style="margin-top:15px">--%>
              <%--<input id="result" class="easyui-textbox" label="造成后果" style="width:90%" >--%>
          <%--</div>--%>
          <div style="margin-top:15px">
              <input id="upload_file"
                     class="easyui-filebox"
                     buttonText="选择图片"
                     name="uploadFile"
                     label="图片" style="width:90%">
          </div>
          <input id="backup_event_id" name="eventId" hidden="hidden">
          <input id="backup_event_src" name="eventSrc" hidden="hidden">
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
<script type="text/javascript" src="${ctx}/script/content/eventManagement.js"></script>
<script>

    parent.IndexJS.expendSouthPanel(260);
    $(function () {
        EventManager.init();
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
    };
    ///easyui datagrid鼠标悬停提示
    function formatCellTooltip(value){
        return "<span title='" + value + "'>" + value + "</span>";
    }

</script>