var overflow = function() {

    var myGlobalCtrl = window.parent.Gis.getGlobalControl();

    /**
     * 触发点击设备事件
     * @param pipeCode  雨水管线编号 code
     */
    function clickPipe(pipeCode) {
        var feature = parent.DeviceService.getFeatureInLayer(pipeCode, "雨水管线");
        if(feature != null) {
            myGlobalCtrl.Globe.JumpToFeature(feature, 20);
            var devCodes = new Array();
            devCodes.push(pipeCode);
            parent.DeviceService.showLabels(devCodes, true);
        }
    }

    /**
     * 初始化liquid-alarm-record-list datagrid
     * 全量程液位监测仪报警记录tab初始化
     */
    function initLiquidAlarmRecordListDatagrid() {
        $("#liquid-alarm-record-list").datagrid({
            url: $('#context').val() + '/overflow/getLiquidAlarmRecordList.do',
            onClickRow: function (rowIndex, rowData) {
                clickPipe(rowData.code);
            }
        });
        $("#liquid-alarm-record-query").click(function() {
            $('#liquid-alarm-record-list').datagrid({
                pageNumber : 1,
                queryParams : {
                    roadName:$("#road").val()
                }
            });
        });
    }

    return {
        init: function () {
            initLiquidAlarmRecordListDatagrid();
        }
    }
}();