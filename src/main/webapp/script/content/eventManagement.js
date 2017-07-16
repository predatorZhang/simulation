/**
 * Created by lenovo on 2017/4/27.
 */
var EventManager = function(){
    var ALARM_RECORD_EVENT = 1;
    var ALARM_EVENT = 2;
    var PAD_EVENT = 3;
    //派发
    function distributeDialog(eventId,eventSrc){
        $("#send_event_id").val(eventId);
        $("#send_event_src").val(eventSrc);
        $('#send').dialog('open');
    }
    //备案
    function backupDialog(eventId,eventSrc){
        $("#backup_event_id").val(eventId);
        $("#backup_event_src").val(eventSrc);
        $('#backup').dialog('open');
    }
    //设备marker展示
    function show(row) {
        var deviceCode = row.deviceCode;
        //地球标注显示设备maker
        var feature = parent.DeviceService.getFeatureInLayer(deviceCode,"传感设备图层");
        if(feature){
            parent.Gis.getGlobalControl().Globe.JumpToFeature(feature,20);
            var devCodes = new Array();
            devCodes.push(deviceCode);
            parent.DeviceService.showLabels(devCodes, true);
        }
    }
    function showAlarmEvent(row){
        var lat = row.latitude;// 39.91284903251729;
        var long = row.longitude;// 116.26273326156267;
        var iconPath = parent.config.getLocalhost()+$("#context").val()+"/images/earth/red_icon1.png";
        //已有的marker直接显示
        var main_layer = parent.Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(parent.Gis.getLayerLiquid());
        var description = "事件类型："+row.message + '\n发生时间：' + row.recordDate + '\n事件位置：' + row.location ;
        parent.Gis.addMarkerFeatureInLayer(main_layer, lat, long, iconPath, description, "marker", description);
    }
    function showPadEvent(row){
        var lat = row.latitude;// 39.91284903251729;
        var long = row.longitude;// 116.26273326156267;
        var iconPath = parent.config.getLocalhost()+$("#context").val()+"/images/earth/red_icon1.png";
        //已有的marker直接显示
        var main_layer = parent.Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(parent.Gis.getLayerLiquid());
        var description = "巡检员："+row.patrolName + '\n发生时间：' + row.eventTime + '\n事件描述：' + row.descripe ;
        parent.Gis.addMarkerFeatureInLayer(main_layer, lat, long, iconPath, description, "marker", description);
    }

    function initAlarmRecordList(){
        var startDate = $("#device_event_begin");
        var endDate = $("#device_event_end");
        parent.gisTools.setStartAndEndDate(startDate, endDate, 7);
        $("#device-event-list").datagrid({
            url: $('#context').val() + '/event/getAlarmRecordList.do',
            queryParams : {
                messageValue: $("#device_event_type").val(),
                messageStatus: $("#device_event_status").combobox("getText"),
                beginDate: startDate.datebox("getText"),
                endDate: endDate.datebox("getText")
            },
            onClickCell: function(rowIndex,field,val){
                $("#device-event-list").datagrid('selectRow',rowIndex);
                var row = $("#device-event-list").datagrid('getSelected');
                if(field=="operate"){
                    operate(val,row.id,ALARM_RECORD_EVENT);
                }else{
                    //地图上展示报警事件信息
                    show(row);
                }
            }
        });
        $("#device_event_query").click(function() {
            flashDeviceTable();
        });
    }
    function initAlarmEventList(){
        var startDate = $("#person_event_begin");
        var endDate = $("#person_event_end");
        parent.gisTools.setStartAndEndDate(startDate, endDate, 7);
        $("#person-event-list").datagrid({
            url: $('#context').val() + '/event/getAlarmEventList.do',
            queryParams : {
                messageStatus: $("#person_event_status").combobox("getText"),
                beginDate: startDate.datebox("getText"),
                endDate: endDate.datebox("getText")
            },
            onClickCell: function(rowIndex,field,val){
                $("#person-event-list").datagrid('selectRow',rowIndex);
                var row = $("#person-event-list").datagrid('getSelected');
                if(field=="operate"){
                    operate(val,row.id,ALARM_EVENT);
                }else{
                    //地图上展示人工上报事件信息
                    showAlarmEvent(row);
                }
            }
        });
        $("#person_event_query").click(function() {
            flashPersonTable();
        });
    }
    function initPadEventList(){
        var startDate = $("#pad_event_begin");
        var endDate = $("#pad_event_end");
        parent.gisTools.setStartAndEndDate(startDate, endDate, 7);
        $("#pad-event-list").datagrid({
            url: $('#context').val() + '/event/getPadEventList.do',
            queryParams : {
                messageStatus: $("#pad_event_status").combobox("getText"),
                beginDate: startDate.datebox("getText"),
                endDate: endDate.datebox("getText")
            },
            onClickCell: function(rowIndex,field,val){
                $("#pad-event-list").datagrid('selectRow',rowIndex);
                var row = $("#pad-event-list").datagrid('getSelected');
                if(field=="operate"){
                    operate(val,row.dbId,PAD_EVENT);
                }else{
                    //地图上展示Pad上报事件信息
                    showPadEvent(row);
                }
            }
        });
        $("#pad_event_query").click(function() {
            flashPadTable();
        });
    }

    function initPatrolList(){
        $('#send_person').combobox({
            valueField : 'dbId',
            textField : 'userName',
            panelHeight : 'auto',
            url : $('#context').val()+'/patrol/fetch-all.do'
        });
    }
    //具体操作的判断路由
    function operate(fieldVal,eventId,eventSrc){
        if(fieldVal.indexOf("派发")!=-1){
            distributeDialog(eventId,eventSrc);
        }else if(fieldVal.indexOf("修改备案")!=-1){
            //请求之前的备案信息，并设置默认值为之前的值
            initBackupDialog(eventId,eventSrc);
            backupDialog(eventId,eventSrc);
        }else if(fieldVal.indexOf("备案")!=-1){
            backupDialog(eventId,eventSrc);
        }
    }

    function initBackupDialog(eventId,eventSrc){
        $.ajax({
            type:"GET",
            url:$('#context').val()+'/event/getBackUpInfo.do',
            data:{"eventId":eventId,"eventSrc":eventSrc},
            success:function(result){
                if(result){
                    $("#backup_info").combobox("setValue",result.data.backupInfo);
                    $("#backup_reason").textbox("setValue",result.data.backupReason);
                    $("#backup_measure").combobox("setValue",result.data.backupMeasure);
                    $("#upload_file").filebox("setValue",result.data.backupOriginalPath);
                }
            },
            error:function(){

            }
        });
    }

    /**
     * 刷新Pad事件列表
     */
    function flashDeviceTable() {
        $('#device-event-list').datagrid("reload", {
            messageValue: $("#device_event_type").val(),
            messageStatus: $("#device_event_status").combobox("getText"),
            beginDate: $("#device_event_begin").datebox("getText"),
            endDate: $("#device_event_end").datebox("getText")
        });
    }

    /**
     * 刷新人工上报事件列表
     */
    function flashPersonTable() {
        $('#person-event-list').datagrid("reload",{
            messageStatus: $("#person_event_status").combobox("getText"),
            beginDate: $("#person_event_begin").datebox("getText"),
            endDate: $("#person_event_end").datebox("getText")
        });
    }

    /**
     * 刷新Pad事件列表
     */
    function flashPadTable() {
        $('#pad-event-list').datagrid("reload", {
            messageStatus: $("#pad_event_status").combobox("getText"),
            beginDate: $("#pad_event_begin").datebox("getText"),
            endDate: $("#pad_event_end").datebox("getText")
        });
    }

    /**
     * 刷新所有事件列表
     */
    function flashTables() {
        flashDeviceTable();
        flashPersonTable();
        flashPadTable();
    }

    return {
        init:function(){
            initAlarmRecordList();
            initAlarmEventList();
            initPadEventList();
            //巡检员列表
            initPatrolList();
        },
        distribute:function(){
            if(!$("#send_person").val()){
                alert("请选择派发的巡检员！");
                return;
            }
            $.ajax({
                type:"POST",
                url:$('#context').val()+'/event/distribute.do',
                data:{"signUid":$("#send_person").val(),
                    "eventId":$("#send_event_id").val(),
                    "description":$("#send_result").val(),
                    "eventSrc":$("#send_event_src").val()
                },
                success:function(result){
                    if(result.success){
                        alert("派发成功！");
                        $('#send').dialog('close');
                        $('#send-info').form('clear');
                    }else{
                        alert("派发失败！");
                    }
                    flashTables();
                },
                error:function(){
                    alert("派发失败！");
                }
            });
        },
        backup:function(){
            $('#backup-info').ajaxSubmit({
                type: 'post',
                url: $('#context').val() + "/event/backup.do",
                dataType:'json',
                success:function(responseText, statusText, xhr, $form){
                    if(responseText.success) {
                        alert("备案成功！");
                        $('#backup').dialog('close');
                        $('#backup-info').form('clear');
                    }else{
                        alert("备案失败！");
                    }
                    flashTables();
                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    alert("备案失败！");
                    console.log(XmlHttpRequest);
                    console.log(textStatus);
                    console.log(errorThrown);
                }
            });
        }

    }
}();
