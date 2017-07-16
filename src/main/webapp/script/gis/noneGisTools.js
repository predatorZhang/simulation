/**
 * Created by liyulong on 2015/12/15.
 */
var noneGisTools = function() {
    return {
        sendMsg : function(phoneNum, msg) {
            jQuery.ajax({
                type: "POST",
                dataType: 'json',
                data: {
                    itemName : phoneNum,
                    message : msg
                },
                url: $('#context').val() + "/alarm/alarm-record!sendMsg.do",
                success: function (result) {
                    if (result.success) {
                        alert("发送成功");
                    } else {
                        alert("发送失败");
                    }
                }
            });
        },

        showDetails : function(feature, evt, featureTooltip) {
            if (feature.GetFieldCount() == 0) {
                return;
            }
            var description = gisTools.getFeatureDetails(feature);
            featureTooltip.ShowBalloon1(evt.X, evt.Y, description);
        },

        showDevDetails : function(feature, evt, featureTooltip) {
            if (feature.GetFieldCount() == 0) {
                return;
            }
            var description = gisTools.getDevDetails(feature);
            featureTooltip.ShowBalloon1(evt.X, evt.Y, description);

            if(feature.getFieldValue('DEVICETYPE').indexOf('光纤') > -1) {
                var devCode = feature.getFieldValue('DEVICEID');

                var markFeats = parent.gisTools.getFiberMarkerFeats(devCode, true);
                for(var i=0; i<markFeats.length; i++) {
                    //if(markFeats[i].Label.Text.indexOf("起点") > -1) {
                    //    Gis.getGlobalControl().Globe.JumpToFeature(markFeats[i], 40);
                    //}
                    markFeats[i].Visible = true;
                    markFeats[i].Label.Visible = true;
                }
            }
        },

        getDevFieldShowName : function(name) {
            switch (name) {
                case "DEVICETYPE" :
                    return "设备类型";
                    break;
                case "DEVICEID" :
                    return "设备编号";
                    break;
                case "ATTACHID" :
                    return "所在管井";
                    break;
                case "ATTACHLAYER" :
                    return "管井类型";
                    break;
                case "ROAD" :
                    return "所属道路";
                    break;
                case "MODELPATH" :
                    return "模型路径";
                    break;
                default :
                    return name;
            }
        },

        getHalfWidth : function(width, length, rotateAngel, type) {
            var angel2 = noneGisTools.getAngel2(width, length, rotateAngel, type);
            return Math.sqrt(Math.pow(width, 2) + Math.pow(length, 2)) * Math.sin(angel2);
        },

        getHalfLength : function(width, length, rotateAngel, type) {
            var angel2 = noneGisTools.getAngel2(width, length, rotateAngel, type);
            return Math.sqrt(Math.pow(width, 2) + Math.pow(length, 2)) * Math.cos(angel2);
        },

        getAngel2 : function (width, length, rotateAngel, type) {
            var angel1 = Math.atan(width/length);
            var angel2 = 0;
            switch (type) {
                case 1:
                    angel2= angel1 + rotateAngel / 180 * Math.PI;
                    break;
                case 3:
                    angel2= angel1 - rotateAngel / 180 * Math.PI;
                    break;
            }
            return angel2;
        },
        drawDevDataCurve : function(feature) {
            var codeWithPrefix = feature.Name;
            var description = feature.Description;
            var devCode = codeWithPrefix.substring(DeviceService.getMarkerPrefix().length);
            var devType = description.split(',')[1];

            var clickedItem = $("#clickEvent").val();
            if (clickedItem && codeWithPrefix.indexOf(DeviceService.getMarkerPrefix()) > -1 ) {//当前点击的那些item,附加查询iframe的事件，并下方展示出相应历史曲线信息
                switch (clickedItem) {
                    case "water_pipe_line_tbl.1":
                        if (devType == "超声波流量监测仪" || devType == "压力监测仪") {
                            DeviceService.activeChartTab(1, devType, devCode, devType + devCode);
                        }
                        break;
                    case "water_pipe_line_tbl.2":
                        if (devType == "噪声监测仪") {
                            DeviceService.activeChartTab(1, devType, devCode, devType + devCode);
                        }
                        break;
                    case "rainPipeLineFunTbl.1":
                        if (devType == "全量程液位监测仪") {//在点击这个的时候，如果不是全量程液位监测仪则不展示
                            DeviceService.activeChartTab(1, devType, devCode, devCode);
                        }
                        break;
                    case "rainPipeLineFunTbl.3":
                        if (devType == "雨量计") {
                            DeviceService.activeChartTab(1, devType, devCode, devCode);
                        }
                        break;
                    case "gas_pipe_line_tbl.11":
                        if (devType == "可燃气体泄漏监测仪") {
                            DeviceService.activeChartTab(1, devType, devCode, devCode);
                        }
                        break;
                }
            }
        }
    }
} ();