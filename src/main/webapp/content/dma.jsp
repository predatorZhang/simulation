<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DMA模型管理</title>
    <link href="${ctx}/css/frm.css" rel="stylesheet" type="text/css">
    <!-- 引用easyUI样式 -->
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">
</head>

<body class="easyui-layout">

<input id="ctx" type="hidden" value="${ctx}">
<input id="region_parent_dma_id" type="hidden">
<input id="device_parent_pos" type="hidden">
<input id="water_parent_dma_id" type="hidden">
<div data-options="region:'west',title:'区域',split:true" style="width:300px;">
    <%--文件选择器--%>
    <input id="fileSelector" type="file" style="display: none">
    <table id="parition-structure-tree" class="easyui-treegrid"
           data-options="fit:true,
               fitColumns:true,
               method:'post',
               idField:'id',
               treeField:'elementName'">
        <thead>
        <tr>
            <th data-options="field:'elementName', width:180">分区结构</th>
        </tr>
        </thead>
    </table>
</div>
<div data-options="region:'center',title:'维护'" style="background:#eee;">
<div id="maintain-tab" class="easyui-tabs" fit="true" border="true">
<div title="子分区信息列表" data-options="closable:false">
    <div id="dlg-add-dma" class="easyui-dialog" style="width: 750px;"
         data-options="title:'新增DMA分区',closable:true,closed:true,modal:true,buttons:'#dlg-add-dma-tool'">
        <form id="fm-add-dma" class="fm" style="padding: 0px">
            <input id="dma-BDataParent_DMA_ID" name="BDataParent_DMA" type="hidden">
            <table class="fitem" style="padding: 0px">
                <tr>
                    <td><label>分区编码</label></td>
                    <td><input class="easyui-validatebox"  name="no" required="true"></td>
                    <td><label>分区名称</label></td>
                    <td><input class="easyui-validatebox"  name="name" required="true" id="name_add"></td>
                    <td><label>用户数量</label></td>
                    <td><input class="easyui-validatebox"  name="userCount" data-options="required:true,validType:'number'"></td>
                </tr>
                <tr>
                    <td><label>夜间用水量(m³)</label></td>
                    <td><input class="easyui-validatebox"  name="normalWater" data-options="required:true,validType:'intOrFloat'"></td>
                    <td><label>管道总长度(㎞)</label></td>
                    <td><input class="easyui-validatebox"  name="pipeLeng" data-options="required:true,validType:'intOrFloat'"></td>
                    <td><label>户表后管道总长度(㎞)</label></td>
                    <td><input class="easyui-validatebox"  name="userPipeLeng" data-options="required:true,validType:'intOrFloat'"></td>
                </tr>
                <tr>
                    <td><label>管道连接总数</label></td>
                    <td><input class="easyui-validatebox"  name="pipeLinks" data-options="required:true,validType:'number'"></td>
                    <td><label>ICF</label></td>
                    <td><input class="easyui-validatebox"  name="icf" data-options="required:true,validType:'intOrFloat'"></td>
                    <td><label>漏损控制目标</label></td>
                    <td><input class="easyui-validatebox"  name="leakControlRate" data-options="required:true,validType:'intOrFloat'"></td>
                </tr>
                <tr>
                    <td><label>日售水量</label></td>
                    <td><input class="easyui-validatebox"  name="saleWater" data-options="required:true,validType:'intOrFloat'"></td>
                    <td><a href="#" class="easyui-linkbutton" onclick="javascript:DMA.selectRegion()">请选择区域</a></td>
                    <td colspan="3"><input id="region" name="region" class="easyui-validatebox" style="width:100%" required="true" readonly></td>
                </tr>
            </table>
        </form>
    </div>
    <div id="dlg-add-dma-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:DMA.newDmaRegion()">确定</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="javascript:DMA.resetNewDmaRegion()">重置</a>
    </div>
    <div id="dlg-dma-water" class="easyui-dialog" data-options="title:'新增DMA分区售水量维护',closable:true,closed:true,modal:true"
         style="overflow: hidden; height:200px;width: 500px;">
        <div id="dlg-add-water" class="easyui-dialog" data-options="title:'添加售水量',closable:true,closed:true,modal:true,buttons:'#dlg-add-water-tool'" style="width: 300px;">
            <form id="fm-add-water" class="fm">
                <input id="water-dma-id" name="dmaID" type="hidden" class="easyui-validatebox" data-options="required:true">
                <table class="fitem">
                    <tr>
                        <td><label>开始日期</label></td>
                        <td><input class="easyui-datebox" type="text" name="beginDate" data-options="required:true"></td>
                    </tr>
                    <tr>
                        <td><label>结束日期</label></td>
                        <td><input class="easyui-datebox" type="text"  name="endDate" required="true"></td>
                    </tr>
                    <tr>
                        <td><label>售水量(m³)</label></td>
                        <td><input class="easyui-validatebox"  name="water" data-options="required:true,validType:'intOrFloat'"></td>
                    </tr>
                    <tr>
                        <td><label>无收益水量(m³)</label></td>
                        <td><input class="easyui-validatebox"  name="noValueWater" data-options="required:true,validType:'intOrFloat'"></td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="dlg-add-water-tool">
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:DMA.addWater()">确定</a>
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="javascript:DMA.resetAddWater()">重置</a>
        </div>
        <table id="water_tbl"
               data-options="
                                singleSelect:true,
                                toolbar:'#water-tool',
                                pagination:true,
                                fit: true,
                                pageSize:5,
                                pageList:[5,10]">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true">分区ID</th>
                <th data-options="field:'startDate',width:100">开始日期</th>
                <th data-options="field:'endDate',width:100">结束日期</th>
                <th data-options="field:'saleWater',width:100">售水量</th>
                <th data-options="field:'noValueWater',width:100">无收益水量</th>
            </tr>
            </thead>
        </table>
        <div id="water-tool">
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"
               onclick="javascript:DMA.openDlg('#dlg-add-water')">添加</a>
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"
               onclick="javascript:DMA.removeWater()">删除</a>
        </div>
    </div>
    <table id="region_tbl"
           data-options="fitColumns:true,
                        singleSelect:true,
                        toolbar:'#dma-tool',
                        pagination:true,
                        fit:true,
                        pageSize:5,
                        pageList:[5,10],
                        method:'post'"
            >
        <thead>
        <tr>
            <th data-options="field:'id',hidden:true">分区ID</th>
            <th data-options="field:'name'" width="10%">分区名称</th>
            <th data-options="field:'userCount'" width="10%">用户数量(户)</th>
            <th data-options="field:'normalWater'" width="17%">正常夜间用水量(m3/h)</th>
            <th data-options="field:'pipeLeng'" width="12%">管道总长度(m)</th>
            <th data-options="field:'userPipeLeng'" width="17%">户表后管道总长度(m)</th>
            <th data-options="field:'pipeLinks'" width="14%">管道连接总数(个)</th>
            <th data-options="field:'icf'" width="6%">ICF</th>
            <th data-options="field:'leakControlRate'" width="14%">漏损控制目标值</th>
        </tr>
        </thead>
    </table>
    <div id="dma-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"
           onclick="javascript:DMA.openDlg('#dlg-add-dma')">添加分区</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"
           onclick="javascript:DMA.removeDmaInfo()">删除分区</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true"
           onclick="javascript:DMA.openWaterDlg()">分区售水量维护</a>
    </div>
</div>
<div title="监测点列表" data-options="closable:false">
    <div id="dlg-add-pos" class="easyui-dialog" style="width: 780px;"
         data-options="title:'新增监测点',closable:true,closed:true,modal:true,buttons:'#dlg-add-pos-tool'">
        <form id="fm-add-pos" class="fm" style="padding: 0px">
            <input id="pos-new-BDataParent_DMA_ID" name="dmaID" type="hidden">
            <table class="fitem" style="padding: 0px">
                <tr>
                    <td><label>监测点名称</label></td>
                    <td><input class="easyui-validatebox" id="positionName_add" name="name" required="true"></td>
                    <td><label>经度</label></td>
                    <td><input class="easyui-validatebox" id="longitude"  name="longitude" required="true" readonly></td>
                    <td><label>纬度</label></td>
                    <td><input class="easyui-validatebox" id="latitude"  name="latitude" required="true" readonly></td>
                    <td><label>流向</label></td>
                    <td>
                        <select class="easyui-combobox" name="direction" style="width:140px;">
                            <option value="1">流入</option>
                            <option value="-1">流出</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>监测点类型</label></td>
                    <td><input class="easyui-validatebox"  name="BDataPosType" required="true"></td>
                   <%-- <td><label>排序码</label></td>
                    <td><input class="easyui-validatebox"  name="sortCode" style="width:140px;" required="true"></td>--%>
                    <td><label>是否启用</label></td>
                    <td>
                        <select class="easyui-combobox" name="isUse" style="width:140px;">
                            <option value="true">是</option>
                            <option value="false">否</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>备注</label></td>
                    <td colspan="7"><input name="comment" style="width: 100%; height: 50px;"></td>
                </tr>
            </table>
        </form>
    </div>
    <div id="dlg-add-pos-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-location',plain:true" onclick="javascript:DMA.SelectXYPosition()">定位</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:DMA.newPosition()">确定</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="javascript:DMA.restNewPosition()">重置</a>
    </div>
    <div id="dlg-app-pos" class="easyui-dialog" data-options="title:'添加监测点',closable:true,closed:true,modal:true,buttons:'#dlg-app-pos-tool'" style="width: 300px;">
        <form id="fm-app-pos" class="fm">
            <input id="pos-app-BDataParent_DMA_ID" name="dmaID" type="hidden">
            <table class="fitem" style="padding: 0px">
                <tr>
                    <td><label>监测点</label></td>
                    <td>
                        <input id="combox-position" class="easyui-combobox" name="ID" data-options="valueField:'id',textField:'name',required:true" style="width: 170px;">
                    </td>
                </tr>
                <tr>
                    <td><label>流向</label></td>
                    <td>
                        <select class="easyui-combobox" name="direction" style="width: 170px;" data-options="required:true">
                            <option value="1">流入</option>
                            <option value="-1">流出</option>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="dlg-app-pos-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:DMA.appendPosition()">确定</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="javascript:DMA.resetAppendPosition()">重置</a>
    </div>
    <table id="pos_tbl" class="easyui-datagrid"
           data-options="fitColumns:true,
                       fitColumns:true,
                        singleSelect:true,
                        toolbar:'#pos-tool',
                        pagination:true,
                        fit:true,
                        pageSize:5,
                        pageList:[5,10]">
        <thead>
        <tr>
            <th data-options="field:'id',hidden:true">监测点ID</th>
            <th data-options="field:'name'" width="15%">监测点名称</th>
            <th data-options="field:'longitude'" width="15%">经度</th>
            <th data-options="field:'latitude'" width="15%">纬度</th>
            <th data-options="field:'comment'" width="55%">备注</th>
        </tr>
        </thead>
    </table>
    <div id="pos-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"
           onclick="javascript:DMA.openDlg('#dlg-add-pos')">新建监测点</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"
           onclick="javascript:DMA.openDlg('#dlg-app-pos')">添加监测点</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"
           onclick="javascript:DMA.removePosition()">移除监测点</a>
    </div>
</div>
<div title="设备列表" data-options="closable:false">
    <div id="dlg-add-dev" class="easyui-dialog"
         data-options="title:'添加设备',
                        closable:true,
                        closed:true,
                        modal:true,
                        buttons:'#dlg-add-dev-tool'" style="width: 520px;">
        <form id="fm-add-dev" class="fm" style="padding: 0px">
            <input id="dev-position-id" name="positionId" type="hidden" class="easyui-validatebox" required="true">
            <table class="fitem" style="padding: 0px">
                <tr>
                    <td><label>设备编号</label></td>
                    <td>
                        <input id="dev-combox"  name="devId" style="width: 150px;" required="true" data-options="
                            valueField:'id',
                            textField:'devCode',
                            url:'${ctx}/WaterPipelineDeviceScan/queryDeviceV2.do'">
                    </td>
                    <td><label>传感器类型</label></td>
                    <td>
                        <input id="eqt_type_name" style="width: 150px;" disabled="disabled" readonly="readonly"/>
                        <input id="eqt_type" name="sensorType" type="hidden"/>
                    </td>
                </tr>
                <tr>
                    <td><label>材质</label></td>
                    <td>
                        <select id="pipeMaterial" name="pipeMaterial" required="true" class="easyui-combobox" style="width: 150px;">
                            <option value="球墨铸铁">球墨铸铁</option>
                            <option value="铸铁">铸铁</option>
                            <option value="钢/镀锌">钢/镀锌</option>
                            <option value="铜">铜</option>
                            <option value="钢筋混凝土/水泥">钢筋混凝土/水泥</option>
                            <option value="铅">铅</option>
                            <option value="铅银/铜合金">铅银/铜合金</option>
                            <option value="玻璃钢">玻璃钢</option>
                            <option value="陶瓷">陶瓷</option>
                            <option value="PVC">PVC</option>
                            <option value="PE">PE</option>
                        </select>
                    </td>
                    <td><label>管径(㎜)</label></td>
                    <td>
                        <input id="eqt_pipe_size" name="pipeSize" type="text" class="easyui-validatebox" data-options="required:true,validType:'intOrFloat'" style="width: 150px;">
                    </td>
                </tr>
                <tr>
                    <td><label id="lb_eqt_start_total">起始累计流量(m³)</label></td>
                    <td colspan="3">
                        <input id="eqt_start_total_value" name="startTotalValue" type="text" style="width: 150px;" class="easyui-validatebox" data-options="required:true,validType:'intOrFloat'">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="dlg-add-dev-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="javascript:DMA.addDevice4Position()">确定</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="javascript:DMA.resetDevice4Position()">重置</a>
    </div>
    <table id="eqt_tbl" class="easyui-datagrid"
           data-options="fitColumns:true,
                       fitColumns:true,
                        singleSelect:true,
                        toolbar:'#eqt-tool',
                        pagination:true,
                        fit:true,
                        pageSize:5,
                        pageList:[5,10]"
            >
        <thead>
        <tr>
            <th data-options="field:'id',hidden:true">设备ID</th>
            <th data-options="field:'devCode',hidden:true">设备名称</th>
            <th data-options="field:'devName'" width="15%">设备名称</th>
            <th data-options="field:'typeName'" width="15%">设备类型</th>
            <th data-options="field:'monitorType'" width="15%">监测类型</th>
            <th data-options="field:'longtitude'" width="15%">经度</th>
            <th data-options="field:'latitude'" width="15%">纬度</th>
            <th data-options="field:'factory'" width="25%">所属道路</th>
        </tr>
        </thead>
    </table>
    <div id="eqt-tool">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="javascript:DMA.openDlg('#dlg-add-dev')">添加设备</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:DMA.removeDevice()">移除设备</a>
        <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:DMA.addAllYeweiji()">添加所有液位计</a>--%>
        <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="javascript:DMA.addAllShenlou()">添加所有噪声记录仪</a>--%>
    </div>
</div>
</div>
</div>
<div id='Loading' style="position: absolute; z-index: 1000; top: 0px; left: 0px;
         width: 100%; height: 100%; background: #ffffff; text-align: center;">
</div>
</body>
</html>
<!-- core js jquery 1.11 -->
<script src='${ctx}/script/easyui/jquery.min.js'></script>
<script src='${ctx}/script/easyui/jquery.easyui.min.js'></script>
<script src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="${ctx}/script/content/dma.js"></script>
<script type="text/javascript">
    parent.IndexJS.expendSouthPanel(280);
    $.extend($.fn.validatebox.defaults.rules, {
        intOrFloat : {
            validator : function(value) {
                return /^\d+(\.\d+)?$/i.test(value);
            },
            message : '只能是数字.'
        },
        number: {
            validator : function(value) {
                return /^[1-9]+\d*$/i.test(value);
            },
            message : '只能是非零正整数.'
        }
    });

    $(function () {
        DMA.init();
    });

    document.onreadystatechange = function () {
        if (document.readyState == "complete") {
            closes();
        }
    }

    function closes() {
        $("#Loading").fadeOut("normal", function () {
            $(this).remove();
        });
    }
</script>
