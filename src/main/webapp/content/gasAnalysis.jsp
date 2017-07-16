<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="/taglibs.jsp" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/script/easyui/themes/default/easyui.css"/>
    <style type="text/css">
        body {
            padding: 0;
            margin: 0;
        }
        table,td,tr {
            font-size: 12px;
        }

    </style>
</head>

<body style="text-align: left;background-color: #FFFFFF;height:250px;overflow-x:hidden;overflow-y:hidden;">
<input type="hidden" id="context" value="${ctx}"/>
<div>

    <form id="form-gas-analysis" enctype="multipart/form-data" method="post"  class="fm">
        <table class="fitem" style="width: 100%;">
            <tr><td><input type="text" hidden="hidden" name="dbId" id="dangerArea-id" value="${model.dbId}"/></td></tr>
            <tr>
                <td><label>燃气流速(m/s)</label></td>
                <td><input id="gas_speed" name="areaName" class="easyui-textbox" number></td>
                <td><label>泄漏孔径(m<sup>2</sup>)</label></td>
                <td><input id="hole_area" name="areaName" class="easyui-textbox" number></td>
            </tr>
            <tr>
                <td><label>风速(m/s)</label></td>
                <td><input id="wind_speed" name="location" class="easyui-textbox"></td>
                <td><label>燃气密度(mg/m<sup>3</sup>)</label></td>
                <td><input  id="gas_density"  class="easyui-textbox"></td>
            </tr>
            <tr>
                <td><label>泄漏时长(s)</label></td>
                <td><input  id="time_span"  class="easyui-textbox"></td>
                <td></td>
                <td></td>
            </tr>
        </table>
        <div style="text-align:center;padding:5px 0">
            <a class="easyui-linkbutton" id="cancel" style="width:80px">取消</a>
            <a class="easyui-linkbutton" id="ok" style="width:80px">提交</a>
        </div>
    </form>

</div>

</body>

</html>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/script/jquery/jquery.form.js"></script>

<script>

   $("#ok").click(function(){
       parent.Gis.getGlobalControl().Globe.MemoryLayer.RemoveAllFeature();
       parent.Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(parent.Gis.getCacheLayer()).RemoveAllFeature();
        var emitterFeature = parent.Gis.getGlobalControl().Globe.SelectedObject;
       var v = $("#gas_speed").textbox("getValue");
       var d = $("#hole_area").textbox("getValue");
       var v0 = $("#wind_speed").textbox("getValue");
       var p = $("#gas_density").textbox("getValue");
       var T = $("#time_span").textbox("getValue");

       if(isNaN(v)||isNaN(d)||isNaN(v0)||isNaN(p)||isNaN(T)){
           alert("请输入数字！");
           return;
       }
       var diameter = Math.pow((3/8)*v*d*d*(v0/p)*T,1/3);
       parent.Gis.getGasLeakage(emitterFeature, 20, diameter,"no");
       parent.Gis.AddWaterByLine(emitterFeature);
       parent.layer.close(parent.Gis.getLayerGas());
   });
    $("#cancel").click(function(){
        $('#form-gas-analysis').form("clear");
        parent.Gis.clearCalculate();
        parent.layer.close(parent.Gis.getLayerGas());
    });
</script>


