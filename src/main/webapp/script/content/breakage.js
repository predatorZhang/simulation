var breakage = function() {

    var myGlobalCtrl = window.parent.Gis.getGlobalControl();
    var defaultNullValue = "-";
    var noiseDateURL = "/noise/getData.do";
    var noiseDateMeans = "噪声幅度值";

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
                        {type: 'average', name: '平均值'},
                        [{
                            symbol: 'none',
                            x: '90%',
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
     * 初始化noise-device-list datagrid
     * 噪声设备列表tab初始化
     */
    function initNoiseDeviceListDatagrid() {
        $("#noise-device-list").datagrid({
            url: $('#context').val() + '/breakage/getNoiseDeviceList.do',
            onLoadSuccess: function (result) {
                if(result.success===true
                    && result.rows
                    && result.rows.length > 0) {
                    prepareChartsQueryParam(result.rows[0].devCode, noiseDateURL, noiseDateMeans);
                }
            },
            onClickRow: function (rowIndex, rowData) {
                prepareChartsQueryParam(rowData.devCode, noiseDateURL, noiseDateMeans);
                clickDevice(rowData.devCode);
            }
        });
        $("#noise-device-query").click(function() {
            $('#noise-device-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    devCode: $("#noise-device-query-devCode").val()
                }
            });
        });
    }

    /**
     * 初始化noise-alarm-record-list datagrid
     * 噪声报警记录tab初始化
     */
    function initNoiseAlarmRecordListDatagrid() {
        parent.gisTools.setStartAndEndDate(
            $("#noise-alarm-record-query-startdate"),
            $("#noise-alarm-record-query-enddate"), 7
        );
        $("#noise-alarm-record-list").datagrid({
            url: $('#context').val() + '/breakage/getNoiseAlarmRecordList.do',
            queryParams : {
                beginDate: $("#noise-alarm-record-query-startdate").datebox("getText"),
                endDate: $("#noise-alarm-record-query-enddate").datebox("getText"),
                messageStatus: '全部'
            },
            onClickRow: function (rowIndex, rowData) {
                prepareChartsQueryParam(rowData.devCode, noiseDateURL, noiseDateMeans);
                clickDevice(rowData.devCode);
            }
        });
        $('#noise-alarm-record-query-messageStatus').combobox({
            url: $("#context").val() + '/alarm/getMessageStatusList.do',
            onLoadSuccess: function (data) {
                $('#noise-alarm-record-query-messageStatus').combobox(
                    'setValue', '全部'
                );
            }
        });
        $("#noise-alarm-record-query").click(function() {
            $('#noise-alarm-record-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    devCode: $("#noise-alarm-record-query-devCode").val(),
                    messageStatus: $('#noise-alarm-record-query-messageStatus').combobox("getText"),
                    beginDate: $("#noise-alarm-record-query-startdate").datebox("getText"),
                    endDate: $("#noise-alarm-record-query-enddate").datebox("getText")
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
        $("#breakage-curve-query").click(breakage.initCharts);
    }

    /**
     * 准备设备图表查询所需要的参数
     * @param devCode
     * @param rsURL
     */
    function prepareChartsQueryParam(devCode, rsURL, seriesMeans) {
        $("#device-curve-query-devCode").textbox("setText", devCode);
        $("#device-curve-query-rsURL").val(rsURL);
        $("#device-curve-series-means").val(seriesMeans);
    }

    return {
        init: function () {
            initNoiseAlarmRecordListDatagrid();
            initNoiseDeviceListDatagrid();
            initCurveTab();

            $("#supply-water-breakage-tabs").tabs({
                onSelect: function (title, index) {
                    if ("设备曲线" == title) {
                        breakage.initCharts();
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
                jsonpCallback:"success_jsoncallback",
                success: function (result) {
                    if (result.success && result.data) {
                        var list = result.data;
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