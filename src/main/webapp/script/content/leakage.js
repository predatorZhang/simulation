var leakage = function() {

    var myGlobalCtrl = window.parent.Gis.getGlobalControl();
    var gisMemorylayerDmaPartitionFeaturePrefix = "DMA_";
    var defaultNullValue = "-";
    var pressDateURL = "/press/getData.do";
    var flowDateURL = "/flow/getData.do";
    var pressDateMeans = "压力(MPa)";
    var flowDateMeans = "瞬时流量(m3/h)";

    /** 设备曲线曲线图 */
    var defaultLineOption = {
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '5%',
            containLabel: true
        },
        xAxis:  {
            type: 'category',
            boundaryGap: false
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value}'
            }
        },
        series: [
            {
                type:'line',
                markLine: {
                    data: [
                        [{
                            symbol: 'none',
                            x: '95%',
                            yAxis: 'max'
                        }, {
                            symbol: 'circle',
                            label: {
                                normal: {
                                    position: 'start',
                                    formatter: '最大值'
                                }
                            },
                            type: 'max',
                            name: '最高点'
                        }]
                    ]
                }
            }
        ]
    };

    /**
     * 加载分区列表成功后触发事件
     * @param result
     * @constructor
     */
    function loadPartitionListSuccess(result) {
        var layer = myGlobalCtrl.Globe.MemoryLayer;
        var data = result.rows;
        for (var i = 0; i < data.length; i++) {
            var dmaName = data[i].dmaInfoName;
            var dmaId = data[i].dmaId;
            var dmaFeatId = gisMemorylayerDmaPartitionFeaturePrefix + dmaId;
            var features = layer.GetFeatureByName(dmaFeatId, true);
            for(var j = 0;j < features.count();j++) {
                var feature = features.item(i);
                feature.Delete();
            }
            myGlobalCtrl.Refresh();
            var leakRate = data[i].LeakRate.toFixed(1);
            var reportDate = data[i].ReportDate;
            var points = eval(data[i].points);
            var pointStr = [];
            for (var j = 0; j < points.length; j++) {
                var point = points[j];
                pointStr.push({x: point.x, y: point.y});
            }
            if (pointStr.length > 2) {
                var newFillColor =
                    myGlobalCtrl.CreateColorRGBA();
                //根据漏损率填充不同颜色的区域，填充颜色，对应红、蓝、黑、透明度
                if (leakRate >= 20) {
                    newFillColor.SetValue(100, 0, 0, 50);
                } else if (leakRate < 10) {
                    newFillColor.SetValue(0, 0, 255, 50);
                } else {
                    newFillColor.SetValue(0, 0, 0, 50);
                }
                var desc = "分区名称:" + dmaName + "\n" +
                    "分区漏损率:" + leakRate + "\n" +
                    "分析时间：" + reportDate;
                parent.Gis.drawPolygonWithFontColor(
                    pointStr, desc, newFillColor, dmaFeatId
                );
            }
        }
        //定位视角
        myGlobalCtrl.Globe.JumpToCameraState(
            parent.Gis.getInitCameraState()
        )
    }

    /**
     * 点击分区列表数据触发事件
     * @param rowIndex
     * @param rowData
     */
    function clickParition(rowIndex, rowData) {
        var features =
            myGlobalCtrl.Globe.MemoryLayer.GetAllFeatures();
        var point = null;
        var targetName = gisMemorylayerDmaPartitionFeaturePrefix+rowData.dmaId;
        for (var i = 0; i < features.Count; i++) {
            var feature = features.Item(i);
            if(feature.Name == targetName) {
                feature.Visible = true;
                point = feature.Geometry.GeoBottomCenterPoint;
            } else {
                feature.Visible = false;
            }
        }
        if (point) {
            var cameraState = myGlobalCtrl.Globe.CameraState;
            cameraState.Longitude = point.x;
            cameraState.Latitude =  point.y;
            myGlobalCtrl.Globe.JumpToCameraState(cameraState);
        } else {
            $.messager.alert("结果", "模型已不存在，请重新加载");
        }
    }

    /**
     * 初始化partition-list datagrid
     * 分区列表tab初始化
     */
    function initPartitionDatagrid() {
        var start = $("#date_start"), end = $("#date_end");
        parent.gisTools.setStartAndEndDate(start, end, 7);
        $("#parition-list").datagrid({
            url: $('#context').val() +
                '/WaterPipelineAnalysis/getCurrentLeakage.do',
            queryParams: {
                beginDate: start.datebox("getValue"),
                endDate: end.datebox("getValue")
            },
            onLoadSuccess: function (result) {
                if(result.success===true) {
                    loadPartitionListSuccess(result);
                }
            },
            onClickRow: function (rowIndex, rowData) {
                clickParition(rowIndex, rowData);
            }
        });
        $("#btn_search_parition").click(function() {
            $("#parition-list").datagrid("reload", {
                beginDate: start.datebox("getValue"),
                endDate: end.datebox("getValue")
            })
        });
    }

    /**
     * 触发点击设备事件
     * @param devCode  device code
     */
    function clickDevice(devCode) {
        var feature = parent.DeviceService.getFeatureInLayer(devCode, "传感设备图层");
        if(feature != null) {
            myGlobalCtrl.Globe.JumpToFeature(feature, 20);
            var devCodes = new Array();
            devCodes.push(devCode);
            parent.DeviceService.showLabels(devCodes, true);
        }
    }

    /**
     * 初始化press-alarm-record-list datagrid
     * 压力报警记录tab初始化
     */
    function initAlarmRecordListDatagrid() {
        var startDate = $("#press-alarm-record-query-startdate");
        var endDate = $("#press-alarm-record-query-enddate");
        var messageStatus = $('#press-alarm-record-query-messageStatus');
        parent.gisTools.setStartAndEndDate(startDate, endDate, 7);
        $("#press-alarm-record-list").datagrid({
            url: $('#context').val() + '/leakage/getPressAlarmRecordList.do',
            queryParams : {
                beginDate: startDate.datebox("getText"),
                endDate: endDate.datebox("getText"),
                messageStatus: '全部'
            },
            onClickRow: function (rowIndex, rowData) {
                prepareChartsQueryParam(rowData.devCode, pressDateURL, pressDateMeans);
                clickDevice(rowData.devCode);
            }
        });
        messageStatus.combobox({
            url: $("#context").val() + '/alarm/getMessageStatusList.do',
            onLoadSuccess: function (data) {
                messageStatus.combobox(
                    'setValue', '全部'
                );
            }
        });
        $("#press-alarm-record-query").click(function() {
            $('#press-alarm-record-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    devCode: $("#press-alarm-record-query-devCode").val(),
                    messageStatus: messageStatus.combobox("getText"),
                    beginDate: startDate.datebox("getText"),
                    endDate: endDate.datebox("getText")
                }
            });
        });
    }

    /**
     * 初始化flow-device-list datagrid
     * 流量设备列表初始化
     */
    function initFLowDeviceListDatagrid() {
        $("#flow-device-list").datagrid({
            url: $('#context').val() + "/leakage/getFlowDeviceList.do",
            onLoadSuccess: function (result) {
                if(result.success===true
                    && result.rows
                    && result.rows.length > 0) {
                    prepareChartsQueryParam(result.rows[0].devCode, flowDateURL, flowDateMeans);
                }
            },
            onClickRow: function (rowIndex, rowData) {
                prepareChartsQueryParam(rowData.devCode, flowDateURL, flowDateMeans);
                clickDevice(rowData.devCode);
            }
        });
        $("#flow-device-query").click(function() {
            $('#flow-device-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    devCode: $("#flow-device-query-devCode").val()
                }
            });
        });
    }

    /**
     * 初始化press-device-list datagrid
     * 压力设备列表初始化
     */
    function initPressDeviceListDatagrid() {
        $("#press-device-list").datagrid({
            url: $('#context').val() + "/leakage/getPressDeviceList.do",
            onLoadSuccess: function (result) {
                if(result.success===true
                    && result.rows
                    && result.rows.length > 0) {
                    prepareChartsQueryParam(result.rows[0].devCode, pressDateURL, pressDateMeans);
                }
            },
            onClickRow: function (rowIndex, rowData) {
                prepareChartsQueryParam(rowData.devCode, pressDateURL, pressDateMeans);
                clickDevice(rowData.devCode);
            }
        });
        $("#press-device-query").click(function() {
            $('#press-device-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    devCode: $("#press-device-query-devCode").val()
                }
            });
        });
    }

    /**
     * 初始化设备曲线tab页内容
     */
    function initCurveTab() {
        parent.gisTools.setStartAndEndDate(
            $("#device-curve-query-startdate"),
            $("#device-curve-query-enddate"), 7
        );
        $("#leakage-curve-query").click(leakage.initCharts);
    }

    /**
     * 准备设备图表查询所需要的参数
     * @param devCode
     * @param rsURL
     * @param seriesMeans
     */
    function prepareChartsQueryParam(devCode, rsURL, seriesMeans) {
        $("#device-curve-query-devCode").textbox("setText", devCode);
        $("#device-curve-query-rsURL").val(rsURL);
        $("#device-curve-series-means").val(seriesMeans);
    }

    return {
        init: function () {
            initPartitionDatagrid();
            initAlarmRecordListDatagrid();
            initFLowDeviceListDatagrid();
            initPressDeviceListDatagrid();
            initCurveTab();

            $("#supply-water-leakage-tabs").tabs({
                onSelect: function (title, index) {
                    if (4 == index) {
                        leakage.initCharts();
                    }
                }
            });

        },

        initCharts: function () {
            var devCode = $("#device-curve-query-devCode").textbox("getText");
            if (!devCode) {
                alert("请选择设备");
                return;
            }
            $.ajax({
                type: "get",
                url: $("#rsURL").val() + $("#device-curve-query-rsURL").val(),
                crossDomain:true,
                data: {
                    devcode: devCode,
                    beginDate: $("#device-curve-query-startdate").datebox("getValue"),
                    endDate: $("#device-curve-query-enddate").datebox("getValue")
                },
                dataType: 'jsonp',
                jsonp: "jsoncallback",
//                jsonpCallback:"success_jsoncallback",
                success: function (result) {
                    if (result.success) {
                        var list = result.flowList ?
                            result.flowList : result.pressList;
                        var timeList = [];
                        var denseList = [];
                        for(var i=0; i<list.length; i++) {
                            timeList.push(new Date(list[i][0]).toLocaleString());
                            denseList.push(list[i][1]);
                        }
                        defaultLineOption.xAxis.data = timeList;
                        defaultLineOption.series[0].data = denseList;
                        defaultLineOption.series[0].name =
                            $("#device-curve-series-means").val();
                        var charts = echarts3.init(
                            document.getElementById("device-curve-echarts")
                        );
                        charts.setOption(defaultLineOption);
                    } else {
                        console.warn("没有相应数据");
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        },

        getDefaultValue: function(o) {
            if (o) {
                return o;
            }
            return defaultNullValue;
        }
    }
}();