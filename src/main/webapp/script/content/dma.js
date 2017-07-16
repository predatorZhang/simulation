/**
 * Created by 203 on 2015/9/8.
 */
var DMA = function () {

    var rootNo = -1; // “全部”根节点的NO

    var selectRegion = function (e) {

        var longitudeLatitudeCollects = "";

        var polygon = e;

        for (var i = 0; i < polygon.PartCount; i++) {

            var polygonParts = polygon.Item(i);

            for (var j = 0; j < polygonParts.Count - 1; j++) {

                var point = polygonParts.Item(j);

                var x = point.X;
                var y = point.Y;
                var longitudeLatitude = (x + ":" + y);

                longitudeLatitudeCollects += (longitudeLatitude + "-");
            }
            longitudeLatitudeCollects = longitudeLatitudeCollects.substring(0, longitudeLatitudeCollects.lastIndexOf("-"));
        }

        $('#region').val(longitudeLatitudeCollects);

        parent.Gis.detachEvent("FireTrackPolygonEnd", selectRegion);

        parent.IndexJS.expendSouthPanel();
    };

    var SelectXYPosition = function (e, sender, x, y) {

        var globalControl = parent.Gis.getGlobalControl();
        var point2D = globalControl.CreatePoint2D();
        point2D.X = x;
        point2D.Y = y;
        var point3D = globalControl.Globe.ScreenToScene(point2D);

        $('#longitude').val(point3D.X);
        $('#latitude').val(point3D.Y);

        parent.Gis.detachEvent('MouseDown', SelectXYPosition);

        parent.IndexJS.expendSouthPanel();
    };

    return {

        init: function () {

            $("#maintain-tab").tabs('disableTab', '监测点列表');
            $("#maintain-tab").tabs('disableTab', '设备列表');

            $('#dev-combox').combobox({

                onSelect: function (rec) {

                    var devId = rec.id;

                    $.ajax({

                        url: $('#ctx').val()+'/WaterPipelineDeviceScan/querySensorByDevId.do',

                        data: {

                            'devId': devId

                        },

                        type: 'POST',

                        success: function (data) {

                            $("#eqt_type").val(data.sensorTypeId);

                            $("#eqt_type_name").val(data.sensorTypeName);

                            if(data.sensorTypeName.indexOf("流量")<0) {
                                $("#eqt_start_total_value").val(0);
                                $("#eqt_start_total_value").hide();
                                $("#lb_eqt_start_total").hide();
                            }else {
                                $("#eqt_start_total_value").val(0);
                                $("#eqt_start_total_value").show();
                                $("#lb_eqt_start_total").show();
                            }

                        }

                    });

                }

            });

            $("#parition-structure-tree").treegrid({

                url: $("#ctx").val() + '/WaterPipelineRegionScan/getWaterPipelineRegionTreeData.do',

                onLoadSuccess: function (data) {

                    var selectDmaId = $("#region_parent_dma_id").val();

                    if (!selectDmaId) {

                        selectDmaId = rootNo;

                    }

                    $("#parition-structure-tree").treegrid("select", selectDmaId);

                    $('#region_parent_dma_id').val(selectDmaId);

                    $('#region_tbl').datagrid({

                        url: $('#ctx').val() + '/WaterPipelineRegionScan/querySubDMAByID.do',

                        queryParams: {

                            regionId: selectDmaId

                        },

                        onSelect: function (rowIndex, rowData) {

                            $('#water_parent_dma_id').val(rowData.id);

                        }

                    });

                    $('#pos_tbl').datagrid({

                        url: $('#ctx').val() + '/WaterPipelineRegionScan/queryPositionInfoByID.do',

                        queryParams: {

                            regionId: selectDmaId

                        }

                    });

                    $('#eqt_tbl').datagrid({

                        url: $('#ctx').val() + '/WaterPipelineRegionScan/queryEquipmentInfoByID.do',

                        onClickRow: function (rowIndex, rowData) {

                            var markFeat = parent.DeviceService.getFeatureInLayer(rowData.devCode, parent.DeviceService.getMainLayer());
                            if (markFeat) {
                                parent.Gis.getGlobalControl().Globe.JumpToFeature(markFeat, 20);
                                var devCodes = new Array();
                                devCodes.push(rowData.devCode);
                                parent.DeviceService.showLabels(devCodes, true);
                            }

                        }

                    });

                    $('#dma-BDataParent_DMA_ID').val(selectDmaId);

                    $('#pos-new-BDataParent_DMA_ID').val(selectDmaId);

                    $('#pos-app-BDataParent_DMA_ID').val(selectDmaId);

                },

                onClickRow: function (row) {

                    $("#region_parent_dma_id").val(row.id);

                    $("#device_parent_pos").val(row.positionID);

                    if (row.isRegion) {

                        DMA.loadSubRegion(row.regionID);

                        DMA.loadPosition(row.regionID);

                        $('#dma-BDataParent_DMA_ID').val(row.id);

                        $('#pos-new-BDataParent_DMA_ID').val(row.id);

                        $('#pos-app-BDataParent_DMA_ID').val(row.id);

                        $('#combox-position').combobox('reload', $('#ctx').val() + '/WaterPipelineRegionScan/queryPosition.do?regionId=' + row.id);

                        $("#maintain-tab").tabs('disableTab', '设备列表');

                        $("#maintain-tab").tabs('enableTab', '子分区信息列表');

                        if(row.id != rootNo) {
                            $("#maintain-tab").tabs('enableTab', '监测点列表');
                        } else {
                            $("#maintain-tab").tabs('disableTab', '监测点列表');
                        }

                        $("#maintain-tab").tabs('select', '子分区信息列表');

                    }

                    if (row.isPosition) {

                        $("#dev-position-id").val(row.positionID);

                        DMA.loadEquipment(row.positionID);

                        $('#dev-combox').combobox("reload");

                        $("#maintain-tab").tabs('enableTab', '设备列表');

                        $("#maintain-tab").tabs('disableTab', '子分区信息列表');

                        $("#maintain-tab").tabs('disableTab', '监测点列表');

                        $("#maintain-tab").tabs('select', '设备列表');

                    }

                }

            });

            $("#maintain-tab").tabs({

                onSelect: function (title, index) {

                    switch (title) {

                        case '子分区信息列表':

                            $('#region_tbl').datagrid('reload');

                            break;

                        case "监测点列表":

                            $('#pos_tbl').datagrid('reload');

                            break;

                        case "设备列表":

                            $('#eqt_tbl').datagrid('reload');

                            break;

                    }

                }

            });

        },

        loadSubRegion: function (id) {

            $('#region_tbl').datagrid('load', {

                regionId: id

            })

        },

        loadPosition: function (id) {

            $('#pos_tbl').datagrid('load', {

                regionId: id

            })

        },

        loadEquipment: function (id) {

            $('#eqt_tbl').datagrid('load', {

                positionID: id

            })

        },

        openDlg: function (dlgId) {

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $(dlgId).dialog('open').dialog('center');

        },

        openWaterDlg: function () {

            var row = $('#region_tbl').datagrid('getSelected');

            if (row) {

                $('#water-dma-id').val(row.id);

                $('#dlg-dma-water').dialog({

                    onOpen: function () {

                        $('#water_tbl').datagrid({

                            url: $('#ctx').val()+'/DMASaleWater/getSaleWaterList.do',

                            queryParams: {

                                dmaID: row.id

                            }

                        });

                    }

                }).dialog('open').dialog('center');


            } else {

                alert('请选择要维护售水量的DMA分区！');

            }

        },

        closeDlg: function (id) {

            $(id).dialog('close');

        },

        selectRegion: function (id) {

            parent.IndexJS.collapseSouthPanel();

            parent.Gis.attachEvent("FireTrackPolygonEnd", selectRegion);

            parent.Gis.setAction(17);

        },

        SelectXYPosition: function () {

            parent.IndexJS.collapseSouthPanel();

            parent.Gis.attachEvent('MouseDown', SelectXYPosition);

        },

        /**
         * 删除分区
         */
        removeDmaInfo: function () {

            var row = $("#region_tbl").datagrid("getSelected");

            if (row) {

                $.messager.confirm("删除", "您确定删除吗?", function (r) {

                    if (r) {

                        $.post(

                            $('#ctx').val() + "/WaterPipelineRegion/delSubDMA.do",

                            {'dmaID': row.id},

                            function (result) {

                                if (result.success) {

                                    $("#parition-structure-tree").treegrid("reload");

                                }

                                parent.gisTools.addSysLog(
                                    "DMA分区定义及分级",
                                    "delete", "删除分区:'" + row.name
                                        + "',结果:" + result.msg );

                                $.messager.alert('结果', result.msg);

                            }, "json");

                    }

                });

            } else {

                $.messager.alert('结果', "请选择要删除的子分区！");

            }

        },

        /**
         * 删除监测点，共享一个监测点的都一并删除
         */
        removePosition: function () {

            var row = $("#pos_tbl").datagrid("getSelected");

            if (row) {

                $.messager.confirm("删除", "您确定删除吗?", function (r) {

                    if (r) {

                        $.post(
                            $('#ctx').val()+"/WaterPipelinePosition/deletePosition.do",

                            {

                                'posID': row.id

                            },

                            function (result) {

                                var positionName = row.name;

                                if (result.success) {

                                    $("#parition-structure-tree").treegrid("reload");

                                }

                                parent.gisTools.addSysLog(
                                    "DMA分区定义及分级",
                                    "delete", "移除监测点:'" + positionName
                                        + "',结果:" + result.msg);

                                $.messager.alert('结果', result.msg);

                            }, "json");

                    }

                });

            } else {

                $.messager.alert('提示', '请选择要删除的监测点！');

            }

        },

        /**
         * 删除监测点关联的设备
         */
        removeDevice: function () {

            var row = $("#eqt_tbl").datagrid("getSelected");

            var node = $("#parition-structure-tree").treegrid("getSelected");

            if (row) {

                $.messager.confirm("删除", "您确定删除吗?", function (r) {

                    if (r) {

                        $.post(
                                $('#ctx').val()+"/WaterPipelineDevice/deleteDevice.do",

                            {

                                'devID': row.id,

                                'positionID': node.positionID

                            },

                            function (result) {

                                if (result.success) {

                                    $("#eqt_tbl").datagrid("reload");

                                    $('#dev-combox').combobox("reload");

                                }
                                parent.gisTools.addSysLog(
                                    "DMA分区定义及分级",
                                    "delete", "删除设备'" + row.devName
                                        + "',结果:" + result.msg );

                                $.messager.alert('结果', result.msg);

                            }, "json");

                    }

                });

            } else {

                $.messager.alert('提示', '请选择要移除的设备！');

            }

        },

        /**
         * 删除售水信息
         */
        removeWater: function () {

            var row = $("#water_tbl").datagrid("getSelected");

            if (row) {

                $.messager.confirm("删除", "您确定删除吗?", function (r) {

                    if (r) {

                        $.post(
                            $('#ctx').val()+"/DMASaleWater/delete.do",

                            {

                                'saleWaterID': row.id

                            },

                            function (result) {

                                if (result.success) {

                                    $("#water_tbl").datagrid("reload");

                                }
                                var regionName = $('#region_tbl').datagrid('getSelected').name;
                                parent.gisTools.addSysLog(
                                    "DMA分区定义及分级", "delete",
                                        "删除售水量，所属分区：'" +
                                        regionName + "',结果:" + result.msg);
                                
                                $.messager.alert('结果', result.msg);

                            }, "json");

                    }

                });

            } else {

                $.messager.alert('提示', '请选择要删除的售水记录！');

            }

        },

        /**
         * 增加分区
         */
        newDmaRegion: function () {

            if ($('#fm-add-dma').form('validate')) {

                $.ajax( {

                    type: "POST",

                    url: $('#ctx').val()+"/WaterPipelineRegion/addSubDMA.do",

                    data: $('#fm-add-dma').serialize(),

                    dataType: "json",

                    success: function(result) {

                        parent.gisTools.addSysLog(
                            "DMA分区定义及分级", "add", "新增分区，名称：'"
                                + $("#name_add").val() +
                                "',结果:" + result.msg);

                        if (result.success) {

                            $('#dlg-add-dma').dialog('close');

                            DMA.resetNewDmaRegion();

                            $('#region_tbl').datagrid('load');

                            $('#combox-position').combobox('reload');

                            $("#parition-structure-tree").treegrid("reload");

                        }

                        $.messager.alert('结果', result.msg);

                    },

                    error:function(XMLHttpRequest, textStatus, errorThrown){}

                });

            }

        },

        resetNewDmaRegion: function () {

            $('#fm-add-dma').form('clear');

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $('#dma-BDataParent_DMA_ID').val($('#region_parent_dma_id').val());

        },

        /**
         * 新建监测点，new 与 append 同时进行
         */
        newPosition: function () {

            if ($('#fm-add-pos').form('validate')) {

                $.ajax( {

                    type: "POST",

                    url: $('#ctx').val()+"/WaterPipelinePosition/addPosition.do",

                    data: $('#fm-add-pos').serialize(),

                    dataType: "json",

                    success: function(result) {

                        var positionName = $('#positionName_add').val();

                        parent.gisTools.addSysLog(
                            "DMA分区定义及分级", "add",
                                "新建监测点：'" + positionName
                                + "',结果:" + result.msg);

                        if (result.success) {

                            $('#dlg-add-pos').dialog('close');

                            DMA.restNewPosition();

                            $('#pos_tbl').datagrid('load');

                            $("#parition-structure-tree").treegrid("reload");

                        }

                        $.messager.alert('结果', result.msg);

                    },

                    error:function(XMLHttpRequest, textStatus, errorThrown){}

                });

            }

        },

        restNewPosition: function () {

            $('#fm-add-pos').form('clear');

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $('#pos-new-BDataParent_DMA_ID').val($('#region_parent_dma_id').val());

        },

        /**
         * 添加监测点
         * 可以添加未删除的监测点（即，跟另外一个分区共享一个监测点）
         */
        appendPosition: function () {

            if ($('#fm-app-pos').form('validate')) {

                $.ajax( {

                    type: "POST",

                    url: $('#ctx').val()+"/WaterPipelinePosition/addExistsPosition.do",

                    data: $('#fm-app-pos').serialize(),

                    dataType: "json",

                    success: function(result) {

                        var positionName = $('#combox-position').combobox("getText");
                        parent.gisTools.addSysLog(
                            "DMA分区定义及分级", "add",
                                "添加监测点，监测点名称：'" +
                                positionName + "',结果:" + result.msg);

                        if (result.success) {

                            $('#dlg-app-pos').dialog('close');

                            DMA.resetAppendPosition();

                            $('#pos_tbl').datagrid('load');

                            $("#parition-structure-tree").treegrid("reload");

                        }

                        $.messager.alert('结果', result.msg);

                    },

                    error:function(XMLHttpRequest, textStatus, errorThrown){}

                });

            }

        },

        resetAppendPosition: function () {

            $('#fm-app-pos').form('clear');

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $('#pos-app-BDataParent_DMA_ID').val($('#region_parent_dma_id').val());

        },

        /**
         * 增加一条新的售水信息
         */
        addWater: function () {

            if ($('#fm-add-water').form('validate')) {

                $.ajax( {

                    type: "POST",

                    url: $('#ctx').val()+"/DMASaleWater/addDMASaleWater.do",

                    data: $('#fm-add-water').serialize(),

                    dataType: "json",

                    success: function(result) {

                        var regionName = $('#region_tbl').datagrid('getSelected').name;

                        parent.gisTools.addSysLog(
                            "DMA分区定义及分级", "add",
                                "新增售水量，所属分区：'" +
                                regionName + "',结果:" + result.msg);

                        $.messager.alert('结果', result.msg);

                        $('#dlg-add-water').dialog('close');

                        DMA.resetAddWater();

                        $('#water_tbl').datagrid('load');

                    },

                    error:function(XMLHttpRequest, textStatus, errorThrown){}

                });

            }

        },

        resetAddWater: function () {

            $('#fm-add-water').form('clear');

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $('#water-dma-id').val($('#water_parent_dma_id').val());

        },

        /**
         * 为监测点增加设备
         */
        addDevice4Position: function () {

            if ($('#fm-add-dev').form('validate')) {

                $.ajax( {

                    type: "POST",

                    url: $('#ctx').val()+"/WaterPipelineDevice/addDevice.do",

                    data: $('#fm-add-dev').serialize(),

                    dataType: "json",

                    success: function(result) {

                        var devName = $('#dev-combox').combobox("getText");

                        parent.gisTools.addSysLog(
                            "DMA分区定义及分级",
                            "add", "添加设备：'" + devName +
                                "',结果:" + result.msg);

                        if (result.success) {

                            $('#dlg-add-dev').dialog('close');

                            $('#eqt_tbl').datagrid('load');

                            DMA.resetDevice4Position();

                        }

                        $.messager.alert('结果', result.msg);

                    },

                    error:function(XMLHttpRequest, textStatus, errorThrown){}

                });

            }

        },

        resetDevice4Position: function () {

            $('#fm-add-dev').form('clear');

            $(".validatebox-tip").remove();

            $(".validatebox-invalid").removeClass("validatebox-invalid");

            $('#dev-position-id').val($('#device_parent_pos').val());

            $('#dev-combox').combobox("reload");

        }

    }
}();