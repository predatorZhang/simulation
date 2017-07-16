var DeviceService = function () {

    var marker_prefix = "marker_device_";
    var layerName = Gis.getLayerLiquid();
    var toFixed = function(value){//保留3位小数
        return parseFloat(value).toFixed(3);
    };
    return {
        /**
         * 地图上显示指定设备标注
         * @param devices：Array（DeviceBean）
         */
        show: function (devices) {
            //TODO LIST:地球标注显示设备maker
            for (var i = 0; i < devices.length; i++) {
                var device = devices[i];

                var devCode = device.devCode;
                var lat = device.lat;
                var long = device.long;
                var iconPath = device.icon;
                var devType = device.devType;
                var attachLayer = device.attachLayer;
                var markerId = marker_prefix + devCode;

                //已有的marker直接显示
                var main_layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
                var markerFeats = main_layer.GetFeatureByName(markerId, true);
                if (markerFeats.Count != 0) {
                    var markerFeat = markerFeats.item(0);
                    markerFeat.visible = true;
                    continue;
                }
                var description = device.status + ',' + devType + ',' + attachLayer;
                parent.Gis.addDevMarkerFeatureInLayer(main_layer, lat, long, iconPath, markerId, description);
            }
            Gis.getGlobalControl().refresh();
        },

        /**
         * 根据管线类型返回设备类型列表
         */
        _getDeviceTypeListByPipeType: function (pipeType) {
            var list = new Array();
            switch (pipeType) {
                case "给水管线":
                    list.push("超声波流量监测仪");
                    list.push("压力监测仪");
                    break;
                case "雨水管线":
                    list.push("液位监测仪");
                    list.push("雨量计");
                    break;
                case "污水管线":
                    list.push("液位监测仪");
                    list.push("有害气体监测仪");
                    break;
                case "天然气管线":
                    list.push("燃气智能监测终端");
                    list.push("燃气泄漏形变光纤");
                    list.push("燃气开挖监测光纤");
                    break;
                case "热力管线":
                    list.push("热力泄漏形变光纤");
                    list.push("热力开挖监测光纤");
                    break;
                default:
                    break;
            }
            return list;
        },

        /**
         * 指定管线类型，显示对应的传感器
         */
        showMarkerByPipeType: function (pipeType, layer) {
            var main_layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            var features = main_layer.GetAllFeatures();
            main_layer.visible = true;

            for (var j = 0; j < features.Count; j++) {
                var feature = features.Item(j);
                var desc = feature.Description;
                var list = desc.split(',');
                var devType = list[1];
                var status = list[0];
                var attachLayer = list[3];
                feature.Visible = false;
                var devTypeList = DeviceService._getDeviceTypeListByPipeType(pipeType);

                for (var i = 0; i < devTypeList.length; i++) {
                    var type = devTypeList[i];
                    if (devType == type && ("" == layer || attachLayer == layer)) {
                        var icon = DeviceService._getIconByDevType(devType, status, attachLayer);
                        var originalMarker = feature.Geometry;
                        var mstyle = Gis.getGlobalControl().CreateMarkerStyle3D();
                        mstyle.IconPath = icon;
                        originalMarker.Style = mstyle;
                        feature.Geometry = originalMarker;
                        feature.Visible = true;
                        break;
                    }
                }
            }
        },

        showDevByPipeType: function (pipeType, layer) {
            var devLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("传感设备图层");
            var features = devLayer.GetAllFeatures();
            devLayer.visible = true;

            for (var j = 0; j < features.Count; j++) {
                var feature = features.Item(j);
                var devType = feature.getFieldValue("DEVICETYPE");
                var attachLayer = feature.getFieldValue("ATTACHLAYER");
                feature.Visible = false;
                var devTypeList = DeviceService._getDeviceTypeListByPipeType(pipeType);

                for (var i = 0; i < devTypeList.length; i++) {
                    var type = devTypeList[i];
                    if (devType == type && ("" == layer || attachLayer == layer)) {
                        feature.Visible = true;
                        break;
                    }
                }
            }
        },

        /**
         *
         * @param devCodes:Array 显示指定设备Label信息
         * @param isVisible:bool 指定显示与隐藏
         */
        showLabels: function (devCodes, isVisible) {
            for (var i = 0; i < devCodes.length; i++) {
                var devCode = devCodes[i];
                var main_layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
                var markerFeats = main_layer.GetFeatureByName(marker_prefix + devCode, false);
                if (markerFeats.Count != 0) {
                    var markerFeat = markerFeats.item(0);
                    markerFeat.Visible = true;
                    markerFeat.Label.Visible = isVisible;
                }
            }
        },

        /**
         * 隐藏所有设备图标
         */
        hideAll: function () {
            var main_layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            var features = main_layer.GetAllFeatures();
            for (var i = 0; i < features.Count; i++) {
                features.item(i).Visible = false;
            }
        },

//        /**
//         *
//         * @param device:DeviceBean
//         * @param attachLayerName:String 附属设备图层名称
//         * @param attachFeatureId：String，附属要素ID
//         */
//        add: function (devType, devCode, attachFeatureId, attachLayerName, roadName, model, longtitude, latitude, devName) {
//            if (devType.indexOf("光纤") != -1) {
//                return;
//            }
//
//            //创建设备增加到设备图层
//            var fields = new Array();
//            fields.push("DEVICETYPE");
//            fields.push("DEVICEID");
//            fields.push("ATTACHID");
//            fields.push("ATTACHLAYER");
//            fields.push("ROAD");
//            fields.push("MODELPATH");
//
//            var values = new Array();
//            values.push(devType);
//            values.push(devCode);
//            values.push(attachFeatureId);
//            values.push(attachLayerName);
//            values.push(roadName);
//            values.push(model);
//
//            var position = Gis.getGlobalControl().CreatePoint3d();
//            position.SetValue(longtitude, latitude, -0.5);
//
//            Gis.addModelToLayer(model, devName, "传感设备图层", position, devCode, fields, values);
//
//            //显示添加设备的marker信息
//            var feature = DeviceService.getFeatureInLayer(devCode, "传感设备图层");
//            var desc = "设备编号：" + devCode + "\n" +
//                "设备类型：" + devType;
//            var device = new DeviceBean(feature, desc, true);
//            var devices = new Array();
//            devices.push(device);
//            DeviceService.show(devices);
//        },

//
//        /**
//         * 删除指定设备
//         * @param devCode :String 删除指定设备编号
//         */
//        del: function (devCode) {
//            parent.Gis.removeFeatureById(devCode, "传感设备图层");
//        },

        _getFlowLabelInfo:function(dev, normal) {
            var desc = '';
            if(isNaN(dev.totalFlow)){
                dev.totalFlow =0;
            }
            if(isNaN(dev.insFlow)){
                dev.insFlow =0;
            }
            desc = "设备编号：" + dev.devCode + "\n" +
                "设备类型：" + dev.devTypeName + "\n"+
                "设备位置：" + dev.pos + "\n" +
                "累计流量：" + toFixed(dev.totalFlow)+"(m3)"+"\n"+
                "瞬时流量：" + toFixed(dev.insFlow)+"(m3/h)"+"\n"+
                "上传时间：" + dev.lastTime;
            return desc;
        },

        _getPressLabelInfo:function(dev, normal) {
            var desc = '';
            if(normal) {
                if(isNaN(dev.press)){
                    dev.press =0;
                }
                desc = "设备编号：" + dev.devCode + "\n" +
                    "设备类型：" + dev.devTypeName + "\n"+
                    "设备位置：" + dev.pos + "\n" +
                    "压力值：" + toFixed(dev.press)+"(MPa)"+"\n"+
                    "上传时间：" + dev.lastTime;
            } else {
                if(isNaN(dev.itemValue)){
                    dev.itemValue =0;
                }
                if(dev.thresh==""||isNaN(dev.thresh)){
                    dev.thresh = 0;
                }
                desc = "设备编号：" + dev.devCode + "\n" +
                    "设备类型：" + dev.devTypeName + "\n"+
                    "设备位置：" + dev.pos + "\n" +
                    "压力报警值：" + toFixed(dev.itemValue)+"(MPa)"+"\n"+
                    "压力阈值：" + toFixed(dev.thresh)+"(MPa)"+"\n"+
                    "报警时间：" + dev.alarmTime;
            }
            return desc;
        },
        /**
         * @param:dev:json字符串
         * dev.normal (true:false)
         * dev.devCode
         * dev.roadName
         * dev.typeName:
         * dev.currentValue:当前最新值
         *
         */
        _getLabelInfo :function (dev, normal) {
            var desc = "";
            switch (dev.devTypeName) {
                case "超声波流量监测仪":
                    desc = DeviceService._getFlowLabelInfo(dev, normal);
                    break;
                case "压力监测仪":
                    desc = DeviceService._getPressLabelInfo(dev, normal);
                    break;
                default:
                    desc = DeviceService._getDefaultLabelInfo(dev, normal);
                    break;
            }
            return desc;
        },

        _getDefaultLabelInfo: function (dev) {
            var devCode = dev.devCode;
            var type = dev.typeName;
            var road = dev.roadName;

            var desc = "设备编号：" + devCode + "\n" +
                        "设备类型：" + type + "\n" +
                        "设备位置：" + road;
            return desc;
        },

        _getIconByDevType: function (devType, status, attachLayer) {
            if (typeof(status) == "boolean") {
                status = status ? "true" : "false";
            }
            var icon = "";
            var root = config.getLocalhost() + $('#context').val();
            switch (devType) {
                case "超声波流量监测仪":
                    icon = status=="true"?root+"/images/devMarker/flow.png":root+"/images/devMarker/alarm.png";
                    break;
                case "压力监测仪":
                    icon = status=="true"?root+"/images/devMarker/yl.png":root+"/images/devMarker/alarm.png";
                    break;
                case "噪声监测仪":
                    icon = status == "true" ? root + "/images/devMarker/sl.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "多功能漏损监测仪":
                    icon = status == "true" ? root + "/images/devMarker/dgn.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "爆管预警仪":
                    icon = status == "true" ? root + "/images/devMarker/bgyj.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "液位监测仪":
                    var pngName = "";
                    if (attachLayer.indexOf("雨水") > -1) {
                        pngName = root + "/images/devMarker/ysyw.png";
                    } else if (attachLayer.indexOf("污水") > -1) {
                        pngName = root + "/images/devMarker/wsyw.png";
                    }
                    icon = status == "true" ? pngName : root + "/images/devMarker/alarm.png";
                    break;
                case "雨量计":
                    icon = status == "true" ? root + "/images/devMarker/ylj.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "有害气体监测仪":
                    icon = status == "true" ? root + "/images/devMarker/yhqt.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "燃气智能监测终端":
                    icon = status == "true" ? root + "/images/devMarker/rqzn.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "燃气泄漏形变光纤":
                    icon = status == "true" ? "" : root + "/images/devMarker/alarm.png";
                    break;
                case "燃气开挖监测光纤":
                    icon = status == "true" ? "" : root + "/images/devMarker/alarm.png";
                    break;
                case "热力泄漏形变光纤":
                    icon = status == "true" ? "/images/devMarker/rl.png" : root + "/images/devMarker/alarm.png";
                    break;
                case "热力开挖监测光纤":
                    icon = status == "true" ? "/images/devMarker/rl.png" : root + "/images/devMarker/alarm.png";
                    break;
                default:
                    icon = status == "true" ? root + "/images/devMarker/default.png" : root + "/images/devMarker/alarm.png";
                    break;
            }
            return icon;
        },

        /**
         * 刷新场景中设备最新marker信息，跨域異步請求僅為判定設備是否正常（isNormal字段）
         */
        update:function(){
            $.ajax({
                type: "get",
                url: $("#rsUrl").val() + '/device/getDeviceListForHomePage.do',
                crossDomain:true,
                data: {
                },
                dataType: 'jsonp',
                jsonp: "jsoncallback",
//                jsonpCallback:"success_jsoncallback",
                success: function (data) {
                    var result = eval(data);
                    if (result.success) {
                        var devList = eval(result.data);
                        var devices = new Array();
                        for (var i = 0; i < devList.length; i++) {
                            var dev = devList[i];
                            var feature = DeviceService.getFeatureInLayer(dev.devCode, "传感设备图层");
                            if(feature){
                                var device = new DeviceBean(feature, dev.isNormal);
                                devices.push(device);
                            }
                        }
                        DeviceService.show(devices);
                        ALREADY_UPDATE = true;
                    }
                },
                error: function (request) {
                    ALREADY_UPDATE = true;//出錯也表示其已執行
                    //alert("网络连接错误");
                }
            });
        },

        /**
         *
         * @param devCode:String
         * @returns
         */
        getFeatureInLayer: function (devCode, layerName) {
            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            if (layer == null) {
                alert("无该图层" + layerName);
            }
            if (devCode != null) {
                var features = layer.GetFeatureByName(devCode, false);
                if (features.Count != 0) {
                    return features.Item(0);
                }
            }
            return null;
        },

        getFeaturesInLayer: function (devCode, layerName) {

            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            if (layer == null) {
                alert("无该图层" + layerName);
            }
            if (devCode != null) {
                var features = layer.GetFeatureByName(devCode, false);
                return features;
            }
            return null;
        },

        activeChartTab: function (tabIndex, devType, devCode, devName2) {//tabIndex暂时未用，在各个tab页函数中做的
            var iframeContent = $("#iframeDlg").contents();
            if(null != iframeContent.find("#deviceCode")&& iframeContent.find("#deviceCode").length>0) {
                iframeContent.find("#deviceCode").val(devCode);
            }
            if(null != iframeContent.find("#deviceTypeName")&& iframeContent.find("#deviceTypeName").length>0) {
                iframeContent.find("#deviceTypeName").val(devType);
            }
            if(null != iframeContent.find("#devName2")&& iframeContent.find("#devName2").length>0) {
                iframeContent.find("#devName2").val(devName2);
            }
            if(null != iframeContent.find("#deviceId")&& iframeContent.find("#deviceId").length>0) {
                iframeContent.find("#deviceId").val(devCode);
            }
            if(null != iframeContent.find("#device-curve-query-devCode")&& iframeContent.find("#device-curve-query-devCode").length>0) {
//                iframeContent.find("#device-curve-query-devCode").val(devCode);
                window.frames["iframeDlg"].$("#device-curve-query-devCode").textbox('setValue',devCode);
            }
            if(null != iframeContent.find("#device-curve-query-rsURL")&& iframeContent.find("#device-curve-query-rsURL").length>0) {
                if(devType=='压力监测仪')  iframeContent.find("#device-curve-query-rsURL").val("/press/getData.do");
                else if(devType =='全量程液位监测仪') iframeContent.find("#device-curve-query-rsURL").val( "/flow/getData.do");
            }
            if(null != iframeContent.find("#device-curve-series-means")&& iframeContent.find("#device-curve-series-means").length>0) {
                if(devType=='压力监测仪') iframeContent.find("#device-curve-series-means").val("压力(MPa)");
                else if(devType =='全量程液位监测仪') iframeContent.find("#device-curve-series-means").val( "瞬时流量(m3/h)");
            }

            $("#iframeDlg")[0].contentWindow.initTab(1);
        },
        showEffect:function(feature, evt, featureTooltip) {

            var codeWithPrefix = feature.Name;
            var description = feature.Description;
            var devCode = codeWithPrefix.substring(marker_prefix.length);
            var status = description.split(',')[0];
            var devType = description.split(',')[1];
            var liquidValue = description.split(',')[2];
            var attachLayer = description.split(',')[3];
            if (liquidValue==null||isNaN(liquidValue)) {
                liquidValue = 0;
            }
            //2.显示液位计对应的管井液位效果------------------------------------------------------------------------------
            if (devType.indexOf("全量程液位监测仪") > -1) {
                var devFeat = gisTools.getFeatureInLayer(devCode, "deviceid", "传感设备图层");
                var wellCode = devFeat.getFieldValue("attachid");
                gisTools.removeWaterLevelByMarker(feature);
                Gis.showOneLiquidLevel(devCode, wellCode, attachLayer, liquidValue);
            }

            //3.显示隐藏label-------------------------------------------------------------------------------------------
            if (null != feature && devType != '红外高清网络球机') {
                DeviceService.setLabelText(feature);
                var labelVisible = !feature.Label.Visible;
                feature.Label.Visible = labelVisible;
                if (labelVisible) {
                    Gis.getGlobalControl().Globe.JumpToFeature(feature, 30);
                }
            } else if (null != feature && devType == '红外高清网络球机') {//视频播放
                featureTooltip.ShowLink(evt.x, evt.y,
                        config.getLocalhost() + $('#context').val() + "/content/alarm/videoPlay.jsp")
            }
        },
        setLabelText : function(feature) {
            var devCode = feature.Name.substr(DeviceService.getMarkerPrefix().length);
            var devTypeName = feature.Description.split(',')[1];
            var normal = feature.Description.split(',')[0];
            var url = $("#context").val() + "/leakage/";
            normal = normal == 'true';
            if(devTypeName == "超声波流量监测仪") {
                url += "getFlowDeviceList.do";
            }else if(devTypeName == "压力监测仪") {
                url += normal ? "getPressDeviceList.do" : "getPressAlarmRecordList.do";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: {
                    page : 1,
                    rows : 1,
                    devCode : devCode,
                    devTypeName : devTypeName
                },
                success: function (data) {
                    var  result = eval("(" + data + ")");
                    if (result != null) {
                        var dev = result.rows[0];
                        var desc = DeviceService._getLabelInfo(dev, normal);

                        var fontColor = Gis.getGlobalControl().CreateColorRGBA();
                        if(normal){
                            fontColor.SetValue(0, 0, 255, 255);//蓝色
                        } else {
                            fontColor.SetValue(255, 0, 0, 255);//红色
                        }

                        if(feature.Label.Text == '') {
                            var label = Gis.getGlobalControl().CreateLabel();
                            label.Text = desc;
                            label.Style = Gis.getGlobalControl().CreateLabelStyle();
                            label.Style.TracktionLineWidth = 2;
                            label.Style.SizeFixed = true;
                            label.Style.TracktionLineType = 0;
                            label.Style.TextStyle = Gis.getGlobalControl().CreateTextStyle();
                            label.Style.TextStyle.FontSize = 14;
                            label.Style.TextStyle.ForeColor = fontColor;
                            feature.Label = label;
                        } else {
                            feature.Label.Text = desc;
                        }

                    }
                }
            });
        },

        /**
         * 获取marker前缀
         *
         * @returns {marker_prefix}
         */
        getMarkerPrefix: function () {
            return marker_prefix;
        },

        /**
         * 主要工作图层
         *
         * @returns {layerName}
         */
        getMainLayer: function () {
            return layerName;
        },

        toFixed:function(value) {
            return toFixed(value);
        }
    }
}();

DeviceBean = function (feature,  status) {
    this.devType = feature.getFieldValue("DEVICETYPE");
    this.devCode = feature.getFieldValue("DEVICEID");
    this.roadName = feature.getFieldValue("ROAD");
    this.model = feature.getFieldValue("MODELPATH");
    this.attachLayer = feature.getFieldValue("ATTACHLAYER");
    this.status = status;

    this.lat = 0;
    this.long = 0;

    if (feature.geometry.type == 305) {
        this.lat = feature.Geometry.Position.Y;
        this.long = feature.Geometry.Position.X;
    } else {
        alert("新设备类型：feature.geometry.type=" + feature.geometry.type);
    }
    this.icon = DeviceService._getIconByDevType(this.devType, this.status, this.attachLayer);
};