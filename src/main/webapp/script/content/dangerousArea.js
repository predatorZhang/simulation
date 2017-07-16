/**
 * Created by lenovo on 2017/3/28.
 */
var DangerArea = function() {

    function riskEdit() {
        var row = $('#danger-area-dg').datagrid('getSelected');
        if (!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        $('#area-edit-dialog').dialog('open');
        $("#edit-area-name").textbox("setValue",row.areaName);
        $("#edit-area-dbId").val(row.dbId);
        //TODO List:默认值，暂未完善
        $("#edit-area-level").combobox("setValue", row.areaGrade);
        $("#edit-area-desc").textbox("setValue",row.description);
    }
    function remove() {
        var row = $('#danger-area-dg').datagrid('getSelected');
        if (!row) {
            alert("没有选择数据，请先选择！")
            return;
        }
        if (confirm("确定要删除？")) {
            $.ajax({
                type: "POST",
                url: $('#context').val() + "/dangerArea/dangerArea-delete.do",
                dataType: "json",
                data: "dbId=" + row.dbId,
                success: function (data) {
                    var jData = eval(data);
                    if (jData.success == true) {
                        $("#danger-area-dg").datagrid("reload");
                        parent.Gis.removeFeatureById(row.areaName,parent.Gis.getDangerAreaLayerName());
                        parent.Gis.getGlobalControl().Globe.MemoryLayer.RemoveAllFeature();
                        parent.gisTools.addSysLog(
                            "删除危险区域", "delete",
                                "危险区域:"+row.areaName + ",删除成功!"
                        );
                    }
                    else {
                        alert("删除失败");
                        parent.gisTools.addSysLog(
                            "删除危险区域", "delete",
                                "危险区域:"+row.areaName + ",删除失败!"
                        );
                    }
                },
                error: function (request) {
                    alert("删除失败");
                    parent.gisTools.addSysLog(
                        "删除危险区域", "delete",
                            "危险区域:"+row.areaName + ",删除失败!"
                    );
                }
            });
        }
    }
    function getDangerAreaByName(featureName)
    {
        var dangerAreaLayerName = parent.Gis.getDangerAreaLayerName();
        var globalControl = parent.Gis.getGlobalControl();
        var layer=globalControl.Globe.Layers.GetLayerByCaption(dangerAreaLayerName);
        if (layer == null) {
            alert("无该图层" + dangerAreaLayerName);
            return;
        }
        var features =layer.GetFeatureByName(featureName,true);
        for(var j = 0;j<features.Count;j++)
        {
            var feature = features.item(j);
            return feature;
        }
    }
    return {
        init: function () {
            $(function () {
                var pager = $('#danger-area-dg').datagrid({
                    url: $("#context").val() + '/dangerArea/dangerArea-list.do',
                    onLoadSuccess: function (data) {
                        for (var i = 0; i < data.rows.length; i++) {
                            var areaName = data.rows[i].areaName;
                            var areaFeature = getDangerAreaByName(areaName);
                            var areaGrade = data.rows[i].areaGrade;
                            var message = "危险区域名称：" + areaName + "\n"
                                + "危险区域等级：" + areaGrade;
                            if (areaFeature != null) {
                                parent.Gis.createLabel(areaFeature, message, parent.Gis.getDangerAreaColor(areaGrade), "危险区域");
                            }
                        }
                    },
                    onClickRow: function (rowIndex, rowData) {
                        parent.Gis.setDangerAreaVisiable(true);
                        parent.Gis.getGlobalControl().Refresh();
                        var areaName = rowData.areaName;
                        var feature = null;
                        if (areaName) feature = getDangerAreaByName(areaName);
                        if (feature) {
                            parent.Gis.getGlobalControl().Globe.JumpToFeature(feature, 2000);
                        } else {
                            alert("无名为：" + areaName + "的区域!");
                        }
                    }
                }).datagrid('getPager');
                pager.pagination({
                    buttons: [
                        {
                            iconCls: 'icon-edit',
                            text: '编辑',
                            handler: function () {
                                riskEdit();
                            }
                        },
                        {
                            iconCls: 'icon-remove',
                            text: '删除',
                            handler: function () {
                                remove();
                            }
                        }
                    ]
                });
            })
        },//init
        query: function () {
            $('#danger-area-dg').datagrid("reload",{
                    name: $("#query-area-name").textbox("getValue"),
                    level: $("#query-area-level").combobox("getValue"),
                    desc: $("#query-area-desc").textbox("getValue")
            });
        },
        saveEdit:function() {
            var name = $('#edit-area-name').filebox("getValue");
            $('#edit-area-info').ajaxSubmit({
                type: 'post',
                url: $('#context').val() + "/dangerArea/dangerArea-save.do?",
                dataType: 'json',
                beforeSubmit: function () {
                    if (!name) {
                        alert("请输入危险区域名称");
                        return false;
                    }
                    return $('#edit-area-info').form('validate');
                },
                success: function (responseText) {
                    if (responseText.success == true) {
                        alert("修改成功！");
                        var row = $('#danger-area-dg').datagrid('getSelected');
                        var orignalName = row.areaName;
                        var fields = ["name","rank","description"];
                        var desc = $("#edit-area-desc").textbox("getValue");
                        var level = $("#edit-area-level").combobox("getValue");
                        var values = [name,level,desc];
                        parent.Gis.updateFeatureById(orignalName,name,parent.Gis.getDangerAreaLayerName(),fields,values,parent.Gis.getDangerAreaColor(level));
                        parent.Gis.getGlobalControl().Globe.MemoryLayer.RemoveAllFeature();
                        parent.gisTools.addSysLog(
                            "修改危险区域", "edit",
                                "危险区域:"+$('#edit-area-name').filebox("getValue") + ",修改成功!"
                        );
                        $('#area-edit-dialog').dialog('close');
                        $('#edit-area-info').form('clear');
                        $('#danger-area-dg').datagrid("reload");
                    }else{
                        alert("修改失败！");
                        parent.gisTools.addSysLog(
                            "修改危险区域", "edit",
                                "危险区域:"+$('#edit-area-name').filebox("getValue") + ",修改失败!"
                        );
                    }
                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    alert("请求异常！");
                    console.log(XmlHttpRequest);
                    console.log(textStatus);
                    console.log(errorThrown);
                }
            });
        }

    }
}();