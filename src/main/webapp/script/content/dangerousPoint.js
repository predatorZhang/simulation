/**
 * Created by lenovo on 2017/3/28.
 */
var DangerPoint = function(){

    function view(){
        parent.Gis.setDangerSourceVisiable(true);
        var row = $('#danger-point-dg').datagrid('getSelected');
        if(!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        var sourceName = row.sourceName;
        var globalControl = parent.Gis.getGlobalControl();
        var locationFeature = getDangerSourceByName(sourceName);
        if(locationFeature) globalControl.Globe.JumpToFeature(locationFeature,10);
        else alert("无名为："+sourceName+"的危险源！");
    }
    function errorModel(){//事故模型查看
        var row = $('#danger-point-dg').datagrid('getSelected');
        if(!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        var name = row.sourceName;
        var errorMode = row.errorMode;
        var locationFeature = getDangerSourceByName(name);
        if(locationFeature){
            var globalControl = parent.Gis.getGlobalControl();
            globalControl.Globe.MemoryLayer.RemoveAllFeature();
            if(errorMode == "给水爆管")  parent.Gis.penquan(locationFeature);
            else if(errorMode == "给水渗漏")parent.Gis.getSupplyWaterLeakage(locationFeature, 1, "no");
            else if(errorMode == "燃气爆管")  parent.Gis.huomiao(locationFeature);
            else if(errorMode == "雨水溢流")  parent.Gis.getLiquidLeakage(locationFeature);
            else  parent.Gis.AddWaterLine(locationFeature);
            globalControl.Globe.JumpToFeature(locationFeature,10);
        }
    }
    function  riskEdit(){
        var row = $('#danger-point-dg').datagrid('getSelected');
        if(!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        $('#point-edit-dialog').dialog('open');
        $("#edit-point-dbId").val(row.dbId);
        //TODO List:默认值，暂未完善
        $("#edit-point-name").textbox("setValue",row.sourceName);
        $("#edit-point-level").combobox("setValue",row.sourceGrade);
        $("#edit-point-model").combobox("setValue",row.errorMode);
        $("#edit-point-desc").textbox("setValue",row.description);
    }
    function remove(){
        var row = $('#danger-point-dg').datagrid('getSelected');
        if(!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        if(confirm("确定要删除？")) {
            $.ajax( {
                type: "POST",
                url: $('#context').val()+"/dangerSource/dangerSource-delete.do",
                dataType:"json",
                data: "dbId="+row.dbId,
                success: function(data) {
                    var jData = eval(data);
                    if(jData.success==true) {
//                        alert("删除成功");
                        $("#danger-point-dg").datagrid("reload");
                        parent.Gis.removeFeatureById(row.sourceName,parent.Gis.getDangerPointLayerName());
                        parent.Gis.getGlobalControl().Globe.MemoryLayer.RemoveAllFeature();
                        parent.gisTools.addSysLog(
                            "删除危险源", "delete",
                                "危险源:"+row.sourceName + ",删除成功!"
                        );
                    }
                    else{
                        alert("删除失败");
                        parent.gisTools.addSysLog(
                            "删除危险源", "delete",
                                "危险源:"+row.sourceName + ",删除失败!"
                        );
                    }
                },
                error:function(request){
                    alert("删除失败");
                    parent.gisTools.addSysLog(
                        "删除危险源", "delete",
                            "危险源:"+row.sourceName + ",删除失败!"
                    );
                }
            });
        }
    }
    function getDangerSourceByName(featureName)
    {
        var globalControl = parent.Gis.getGlobalControl();
        var layer=globalControl.Globe.Layers.GetLayerByCaption(parent.Gis.getDangerPointLayerName());
        var features =layer.GetFeatureByName(featureName,true);
        for(var j = 0;j<features.count();j++)
        {
            var feature = features.item(j);
            return feature;
        }
    }
    return {
        init:function() {
            $(function () {
                $('#danger-point-dg').datagrid({
                    url: $("#context").val() + '/dangerSource/dangerSource-list.do',
                    onClickRow: function (rowIndex,rowData) {
                        view();
                    }
                });
            });
            $(function(){
                var pager = $('#danger-point-dg').datagrid().datagrid('getPager');
                pager.pagination({
                    buttons:[{
                        iconCls:'icon-edit',
                        text:'编辑',
                        handler:function(){
                            riskEdit();
                        }
                    },{
                        iconCls:'icon-remove',
                        text:'删除',
                        handler:function(){
                            remove();
                        }
                    },{
                        iconCls:'icon-print',
                        text:'潜在事故模拟',
                        handler:function(){
                            errorModel();
                        }
                    }]
                });
            });

        },//init

        query:function(){
            $('#danger-point-dg').datagrid("reload",{
                    name: $("#query-point-name").textbox("getValue"),
                    level: $("#query-point-level").combobox("getValue"),
                    desc: $("#query-point-desc").textbox("getValue"),
                    model:$("#query-point-model").combobox("getValue")
            });
        },
        saveEdit:function() {
            var name = $('#edit-point-name').filebox("getValue");
            $('#edit-point-info').ajaxSubmit({
                type: 'post',
                url: $('#context').val() + "/dangerSource/dangerSource-save.do?",
                dataType: 'json',
                beforeSubmit: function () {
                    if (!name) {
                        alert("请输入危险源名称");
                        return false;
                    }
                    return $('#edit-point-info').form('validate');
                },
                success: function (responseText) {
                    if (responseText.success == true) {
                       alert("修改成功！");
                        var row = $('#danger-point-dg').datagrid('getSelected');
                        var orignalName = row.sourceName;
                        var fields = ["name","rank","descirption"];
                        var level = $("#edit-point-level").combobox("getValue");
                        var desc = $("#edit-point-desc").textbox("getValue");
                        var values = [name,level,desc];
                        parent.Gis.updateFeatureById(orignalName,name,parent.Gis.getDangerPointLayerName(),fields,values,parent.Gis.getDangerPointModelPathByGrade(level));
                        parent.gisTools.addSysLog(
                            "修改危险源", "edit",
                                "危险源:"+$('#edit-point-name').filebox("getValue") + ",修改成功!"
                        );
                        $('#point-edit-dialog').dialog('close');
                        $('#edit-point-info').form('clear');
                        $('#danger-point-dg').datagrid("reload");
                    }else{
                        alert("修改失败！");
                    }
                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    alert("error");
                    parent.gisTools.addSysLog(
                        "修改危险源", "edit",
                            "危险源:"+$('#edit-point-name').filebox("getValue") + ",修改失败!"
                    );
                    console.log(XmlHttpRequest);
                    console.log(textStatus);
                    console.log(errorThrown);
                }
            });
        }
    }
}();