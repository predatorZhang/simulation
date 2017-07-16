/**
 * Created by liyulong on 2015/12/15.
 */
var gisTools = function() {
    var closeValveFrm = null;
    return {
        //修改所有调用后可删除：
        getFeatureLableByDevCode: function (devCode) {
            var layerLiquid = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
            var features = layerLiquid.getAllFeatures();
            for (var i = 0; i < features.Count; i++) {
                var feature = features.item(i);
                if (feature.Name.substring(5) == devCode) {
                    return feature;
                }
            }
            return null;
        },

        getFeatureDetails: function (feature) {
            var table = "<div style='width: 380px;padding:0;margin:0;'>";
            table += "<table style='border: solid 1px #C1DAD7;margin: 0 auto; font-size:9pt;width:100%;' border='0' cellspacing='0' cellpadding='0' align='center'>";
            table += "<tr style='background:#0066CC; color:#fff;font-weight:bold;'>";
            table += "<td style='padding:5px 10px; text-align:center;border-bottom:1pt solid #C1DAD7;width:62px;'>属性</td>";
            table += "<td style='padding:5px 10px; text-align:center;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>值</td>"
            table += "</tr>";
            table += "</table>";
            table += "<div style='width: 380px;overflow-y:scroll;height:220px;padding:0;margin:0;'>";
            table += "<table style='border: solid 1px #C1DAD7;margin: 0 auto; font-size:9pt;width:100%;' border='0' cellspacing='0' cellpadding='0' align='center'>";
            for (var i = 0; i < feature.GetFieldCount(); i++) {
                var defn = feature.GetFieldDefn(i);
                var fieldName = defn.Name;
                var fieldValue = feature.GetFieldValue(i);
                if (fieldValue == null || typeof (fieldValue) == 'undefined' || fieldValue == '') {
                    fieldValue = "无";
                }
                if (i % 2 == 1) {
                    table += "<tr style='background-color:#F5FAFA;'>";
                    table += "<td style='padding:5px 10px; text-align:right;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;width:62px;'>" + fieldName + "</td>";
                    table += "<td style='padding:5px 10px; text-align:left;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>" + fieldValue + "</td>";
                    table += "</tr>";
                } else {
                    table += "<tr>";
                    table += "<td style='padding:5px 10px; text-align:right;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;width:62px;'>" + fieldName + "</td>";
                    table += "<td style='padding:5px 10px; text-align:left;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>" + fieldValue + "</td>";
                    table += "</tr>";
                }
            }
            table += "</table>";
            table += "</div>";
            table += "</div>";
            return table;
        },

        getDevDetails: function (feature) {
            var table = "<div style='width: 380px;padding:0;margin:0;overflow-y:hidden;'>";
            table += "<table style='border: solid 1px #C1DAD7;margin: 0 auto; font-size:9pt;width:100%;' border='0' cellspacing='0' cellpadding='0' align='center'>";
            table += "<tr style='background:#0066CC; color:#fff;font-weight:bold;'>";
            table += "<td style='padding:5px 10px; text-align:center;border-bottom:1pt solid #C1DAD7;width:62px;'>属性</td>";
            table += "<td style='padding:5px 10px; text-align:center;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>值</td>"
            table += "</tr>";
            table += "</table>";
            table += "<div style='width: 380px;overflow-y:auto;height:220px;padding:0;margin:0;'>";
            table += "<table style='border: solid 1px #C1DAD7;margin: 0 auto; font-size:9pt;width:100%;' border='0' cellspacing='0' cellpadding='0' align='center'>";
            for (var i = 0; i < feature.GetFieldCount(); i++) {
                var defn = feature.GetFieldDefn(i);
                var fieldName = noneGisTools.getDevFieldShowName(defn.Name);
                var fieldValue = feature.GetFieldValue(i);

                if (fieldValue == null || typeof (fieldValue) == 'undefined' || fieldValue == '') {
                    fieldValue = "无";
                } else if (typeof fieldValue == 'number' && fieldName != '编码') {
                    fieldValue = fieldValue.toFixed(2);
                }

                if (i % 2 == 1) {
                    table += "<tr style='background-color:#F5FAFA;'>";
                    table += "<td style='padding:5px 10px; text-align:right;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;width:62px;'>" + fieldName + "</td>";
                    table += "<td style='padding:5px 10px; text-align:left;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>" + fieldValue + "</td>";
                    table += "</tr>";
                } else {
                    table += "<tr>";
                    table += "<td style='padding:5px 10px; text-align:right;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;width:62px;'>" + fieldName + "</td>";
                    table += "<td style='padding:5px 10px; text-align:left;border-right:1pt solid #C1DAD7;border-bottom:1pt solid #C1DAD7;'>" + fieldValue + "</td>";
                    table += "</tr>";
                }
            }
            table += "</table>";
            table += "</div>";
            table += "</div>";
            return table;
        },

        getWellCodeByDevCode: function (devCode) {
            var layerDevice = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("传感设备图层");
            var featuresDevice = layerDevice.GetAllFeatures();

            for (var i = 0; i < featuresDevice.Count; i++) {
                var featureDevice = featuresDevice.Item(i);
                var defnDevice = featureDevice.GetFieldValue("DEVICEID");

                if (defnDevice == devCode) {
                    return featureDevice.getFieldValue("ATTACHID");
                }
            }
            return "";
        },

        getWellLayerNameByDevCode: function (devCode) {
            var layerDevice = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("传感设备图层");
            var featuresDevice = layerDevice.GetAllFeatures();

            for (var i = 0; i < featuresDevice.Count; i++) {
                var featureDevice = featuresDevice.Item(i);
                var defnDevice = featureDevice.GetFieldValue("DEVICEID");

                if (defnDevice == devCode) {
                    return featureDevice.getFieldValue("ATTACHLAYER");
                }
            }
            return "";
        },

        removeWaterLevelByWellCode: function (wellCode) {
            if (null == wellCode && "" == wellCode) {
                return;
            }
            var memoryLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getCacheLayer());
            var features = memoryLayer.getFeatureByName("yewei," + wellCode, true);
            if (features.count > 0) {
                features.item(0).delete();
            }
        },

        removeWaterLevelByMarker: function (feature) {
            var wellCode = gisTools.getWellCodeByDevCode(feature.Name.substring(DeviceService.getMarkerPrefix().length));
            gisTools.removeWaterLevelByWellCode(wellCode);
        },

        getLayerLabel: function () {
            return "MemoryLabel";
        },

        //删除liquid图层里边某个marker关联的label
        deleteOneLabelByDevCode: function (devCode) {
            var layerLiquid = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
            var features = layerLiquid.getAllFeatures();
            for (var i = 0; i < features.Count; i++) {
                var feature = features.item(i);
                if (feature.Name.substring(5) == devCode) {
                    feature.delete();
                    Gis.getGlobalControl().refresh();
                    return feature.Label.Text;
                }
            }
        },

        getDevFeatureByDevCode: function (devCode) {
            var layerDevice = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("传感设备图层");
            var featuresDevice = layerDevice.GetAllFeatures();

            for (var i = 0; i < featuresDevice.Count; i++) {
                var featureDevice = featuresDevice.Item(i);
                var defnDevice = featureDevice.GetFieldValue("DEVICEID");

                if (defnDevice == devCode) {
                    return featureDevice;
                }
            }
            return featureDevice;
        },

        changePipeLayerColor: function (layerName, color) {
            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            if (layer != null) {
                var features = layer.getAllFeatures();
                for (var j = 0; j < features.Count; j++) {
                    var feature = features.Item(j);
                    var line = feature.Geometry;
                    line.Style.LineColor = color;
                }
            }
        },

        recoverRainPipeColor: function () {
            var color = Gis.getGlobalControl().CreateColorRGBA();
            color.SetValue(250, 128, 0, 255);
            gisTools.changePipeLayerColor("雨水管线", color);
        },

        recoverGasPipeColor: function () {
            var color = Gis.getGlobalControl().CreateColorRGBA();
            color.SetValue(215, 0, 64, 255);
            gisTools.changePipeLayerColor("天然气管线", color);
        },

        showAllLayers: function () {
            /*for (var nu = 0; nu < Gis.getGlobalControl().Globe.Layers.Count; nu++) {
             var pLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByID(nu);
             if ((pLayer != null))
             pLayer.Visible = true;
             }*/
            //$("#layer_manager").tree('check', $("#layer_manager").tree('getRoot').target);
        },

        //隐藏marker关联的label
        hideLabel: function () {
            var liquidLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
            var features = liquidLayer.GetAllFeatures();
            for (var k = 0; k < features.Count; k++) {
                var feature = features.Item(k);
                if ((null != feature) && (null != feature.Label)) {
                    feature.Label.Visible = false;
                }
            }
            Gis.getGlobalControl().Refresh();
        },


        hideAllLayers: function () {
            for (var i = 0; i < Gis.getGlobalControl().Globe.Layers.Count; i++) {
                var pLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByID(i);
                if (pLayer != null && "180fd" != pLayer.Caption() && pLayer.Caption().indexOf("天地图") == -1) {//隐藏图层时过滤地面模型和天地图
                    pLayer.Visible = false;
                }
            }
        },


        showLayerByPipe: function (pipeName) {
            var layerNameList = new Array();
            switch (pipeName) {
                case "给水" :
                    layerNameList.push("给水阀门");
                    layerNameList.push("给水管线");
                    layerNameList.push("给水管线附属物");
                    layerNameList.push("给水管线特征管点");
                    break;
                case "雨水" :
                    layerNameList.push("雨水管线");
                    layerNameList.push("雨水管线附属物");
                    layerNameList.push("雨水管线特征管点");
                    break;
                case "污水" :
                    layerNameList.push("污水管线");
                    layerNameList.push("污水管线附属物");
                    layerNameList.push("污水管线特征管点");
                    break;
                case "天然气" :
                    layerNameList.push("天然气阀门");
                    layerNameList.push("天然气管线");
                    layerNameList.push("天然气管线附属物");
                    layerNameList.push("天然气管线特征管点");
                    break;
                case "热力" :
                    layerNameList.push("电通管线");
                    layerNameList.push("电通管线附属物");
                    layerNameList.push("电通管线特征管点");
                    break;
                case "供电" :
                    layerNameList.push("供电管线");
                    layerNameList.push("供电管线附属物");
                    layerNameList.push("供电管线特征管点");
                    break;
                default :
                    break;
            }
            Gis.showSomePipeLayer(layerNameList);
        },

        getFiberMarkerFeats: function (devCode, isNormal) {
            var markFeats = parent.DeviceService.getFeaturesInLayer(devCode, DeviceService.getMainLayer());
            var returnFeats = [];
            var isNormalStr = isNormal ? 'true' : 'false';
            for (var i = 0; i < markFeats.Count; i++) {
                if (markFeats.item(i).Description.split(',')[0] == isNormalStr) {
                    returnFeats.push(markFeats.item(i));
                }
            }
            return returnFeats;
        },

        /**
         * 在feature上面标注文字：
         * 注：feature目前只支持文字，
         *
         * @author liyulong
         * @time 2015.12.17
         *
         * @param feat
         * @param desc
         */
        addLabel: function (feat, desc, fontColor, fontSize) {
            var feature = Gis.getGlobalControl().CreateFeature();
            if (feat.Geometry.Type == 305) {
                //处理点模型
                var dynamicMarker = Gis.getGlobalControl().CreateGeoDynamicMarker();
                dynamicMarker.Position = feat.Geometry.Position;
                feature.Geometry = dynamicMarker;
            } else if (feat.Geometry.Type == 302 || feat.Geometry.Type == 401) {
                //处理线模型
                feature.Geometry = feat.Geometry;
            } else {
                //处理面模型
            }
            var label = Gis.getGlobalControl().CreateLabel();
            label.Text = desc;
            if (null != fontColor && null != fontSize) {
                label.Style = Gis.getGlobalControl().CreateLabelStyle();
                label.Style.TextStyle = Gis.getGlobalControl().CreateTextStyle();

                label.Style.TextStyle.ForeColor = fontColor;
                label.Style.TextStyle.FontSize = fontSize;
            }
            feature.Label = label;
            Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(feature);
            return feature;
        },

        //在管线的一端加label
        addLabelOnLineOneSide: function (feature, desc) {
            //不是线feature就不执行
            if (feature.Geometry.Type == 302) {
                var point = feature.Geometry.item(0).item(0);
                var feature0 = Gis.getGlobalControl().CreateFeature();
                var dynamicMarker = Gis.getGlobalControl().CreateGeoDynamicMarker();
                dynamicMarker.Position = point;
                feature0.Geometry = dynamicMarker;
                var label = Gis.getGlobalControl().CreateLabel();

                //透明度调节代码，现在不好使
                //var labelStyle  = Gis.getGlobalControl().CreateLabelStyle();
                //var colorBegin = Gis.getGlobalControl().CreateColorRGBA();
                //colorBegin.SetValue(255,255,255,0);
                //var colorMid = Gis.getGlobalControl().CreateColorRGBA();
                //colorMid.SetValue(255,255,255,0);
                //var colorEnd = Gis.getGlobalControl().CreateColorRGBA();
                //colorEnd.SetValue(255,255,255,0);
                //labelStyle.BackBeginColor = colorBegin;
                //labelStyle.BackEndColor = colorEnd;
                //labelStyle.BackMidColor = colorMid;
                //label.Style = labelStyle;

                label.Text = desc;
                feature0.Label = label;
                Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(feature0);
                return feature0;
            }
            return null;
        },

        getMarkerByDevCode: function (devCode) {
            if (null == devCode && "" == devCode) {
                return null;
            }

            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
            var features = layer.getAllFeatures();

            for (var i = 0; i < features.Count; i++) {
                var feature = features.Item(i);
                var defn = feature.Name.substring(6);
                if (defn == devCode) {
                    return feature;
                }
            }

            return null;
        },

        /**
         * predator:根据雨水管井的编号绘制方井
         * @param wellCode:String
         * @param liquid:String
         */
        addWaterLevelByWellCode: function (wellCode, liquid) {

            var layerDevice = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("传感设备图层");
            var layerRainAppen = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("雨水管线附属物");
            var featuresDevice = layerDevice.GetAllFeatures();
            var featuresRainAppen = layerRainAppen.GetAllFeatures();
            var featureRainPipeAppen = null;
            for (var i = 0; i < featuresDevice.Count; i++) {
                var featureDevice = featuresDevice.Item(i);
                var defn = featureDevice.GetFieldValue("DEVICEID");
                if (defn == wellCode) {
                    for (var j = 0; j < featuresRainAppen.Count; j++) {
                        var featureRainAppen = featuresRainAppen.Item(j);
                        var defn2 = featureRainAppen.GetFieldValue("编号");
                        if (defn2 == featureDevice.GetFieldValue("ATTACHID")) {
                            featureRainPipeAppen = featureRainAppen;
                            break;
                        }
                    }
                    break;
                }
            }
            Gis.showDepthInFangJin(0.75, 0.75, liquid / 1.0, featureRainPipeAppen);
        },

        //显示最新效果
        addWaterLevelByMarker: function (markFeat, percent) {
            var devCode = markFeat.Name.substring(DeviceService.getMarkerPrefix().length);
            var devFeat = gisTools.getFeatureInLayer(devCode, "deviceid", "传感设备图层");
            var wellFeatRain = gisTools.getFeatureInLayer(devFeat.GetFieldValue("ATTACHID"), "编号", "雨水管线附属物");
            var wellFeatSewage = gisTools.getFeatureInLayer(devFeat.GetFieldValue("ATTACHID"), "编号", "污水管线附属物");
            if (new Number(percent) > 1) {
                percent = 1.0;
            }
            if (null != wellFeatRain) { //雨水
                Gis.showDepthInFangJin(0.75, 0.75, percent, wellFeatRain);
            } else if (null != wellFeatSewage) { //污水
                Gis.showCircleWellLiquidLevel(wellFeatSewage, percent, 1.0);
            }
        },

        //在polygon中获取某一图层中的管线,feature为设备marker
        getFeaturesInPolygonAndOneLayer: function (feature, alarmFeature) {
            var layerName = gisTools.getWellLayerNameByDevCode(feature.Name.substring(DeviceService.getMarkerPrefix().length));
            var layerSupplyPipe = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName.replace("附属物", ""));
            return layerSupplyPipe.FindFeaturesInPolygon(alarmFeature.Geometry, false);
        },

        //设置起始结束日期，结束日期为今天，开始日期为今天减去dateNum天
        setStartAndEndDate: function (dateBoxStart, dateBoxEnd, dateNum) {
            var currentDate = new Date();
            var startS = currentDate.getTime() - 1000 * 3600 * 24 * dateNum;
            var start = new Date(startS);
            dateBoxStart.datebox("setValue", Gis.myformatter(start));
            dateBoxEnd.datebox("setValue", Gis.myformatter(currentDate));
        },

        //检查开始日期是否比结束日期早
        checkStartEarlyerEnd: function (dateBoxStart, dateBoxEnd) {
            if(dateBoxStart.val() != null && dateBoxStart.val().length > 0
                && dateBoxEnd.val() != null && dateBoxEnd.val().length > 0
                && dateBoxStart.val() > dateBoxEnd.val()) {
                return false;
            } else {
                return true;
            }
        },

        bulkSaveDev: function (filePath) {
            //创建操作EXCEL应用程序的实例
            try {
                var excelObject = new ActiveXObject("Excel.application");
                //打开指定路径的excel文件
                var excelFile = excelObject.Workbooks.open(filePath);
                //操作第一个sheet(从一开始，而非零)
                excelFile.worksheets(1).select();
                var excelSheet = excelFile.ActiveSheet;
                //文件总行数
                var totalRows = excelSheet.usedrange.rows.count;
            } catch (e) {
            }

            try {
                //获取属性index
                var devCodeIndex = gisTools.getPropertyIndex(excelSheet, "编号");
                var streetIndex = gisTools.getPropertyIndex(excelSheet, "所属道路");
                var heightIndex = gisTools.getPropertyIndex(excelSheet, "到井盖距离");
                var wellLayerNameIdIndex = gisTools.getPropertyIndex(excelSheet, "附属物名称");
                var wellCodeIndex = gisTools.getPropertyIndex(excelSheet, "附属物编号");
                var deviceTypeIdShapeIndex = gisTools.getPropertyIndex(excelSheet, "传感器编码");
                var longitudeIndex = gisTools.getPropertyIndex(excelSheet, "X坐标");
                var latitudeIndex = gisTools.getPropertyIndex(excelSheet, "Y坐标");

                //第一行是标题，从2开始
                for (var i = 2; i <= totalRows; i++) {
                    /* excel属性依次为
                     * 1.设备编号，2.所属道路，3.到井盖距离，4.所在附属物图层名称，
                     * 5.所在附属物编号，6.传感器编码，7.经度，8.纬度
                     */

                    // 1.读取excel中的属性信息
                    var devCode = gisTools.getNotNullValue(excelSheet.Cells(i, devCodeIndex).value);
                    var street = gisTools.getNotNullValue(excelSheet.Cells(i, streetIndex).value);
                    var height = gisTools.getNotNullValue(excelSheet.Cells(i, heightIndex).value);
                    var wellLayerNameId = gisTools.getNotNullValue(excelSheet.Cells(i, wellLayerNameIdIndex).value);
                    var wellCode = gisTools.getNotNullValue(excelSheet.Cells(i, wellCodeIndex).value);
                    var deviceTypeIdShape = gisTools.getNotNullValue(excelSheet.Cells(i, deviceTypeIdShapeIndex).value);
                    var longitude = gisTools.getNotNullValue(excelSheet.Cells(i, longitudeIndex).value);
                    var latitude = gisTools.getNotNullValue(excelSheet.Cells(i, latitudeIndex).value);
                    var wellLayerName = gisTools.getWellLayerNameById(wellLayerNameId);
                    var acceptPersonId = 50;
                    var deviceTypeId = 0;
                    var deviceTypeName = "";
                    var devName = "";
                    var modelPath = "";
                    if ("400" == deviceTypeIdShape) {
                        //智能燃气监测终端
                        deviceTypeId = 201;
                        deviceTypeName = "燃气智能监测终端";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("500" == deviceTypeIdShape) {
                        //液位监测仪
                        deviceTypeId = 150;
                        deviceTypeName = "液位监测仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("600" == deviceTypeIdShape) {
                        //渗漏预警仪
                        deviceTypeId = 200;
                        deviceTypeName = "渗漏预警仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("700" == deviceTypeIdShape) {
                        //有害气体监测仪
                        deviceTypeId = 2424;
                        deviceTypeName = "有害气体监测仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("800" == deviceTypeIdShape) {
                        //井盖防盗报警器
                        deviceTypeId = 2426;
                        deviceTypeName = "井盖防盗报警器";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("900" == deviceTypeIdShape) {
                        //多功能漏损监测仪
                        deviceTypeId = 202;
                        deviceTypeName = "多功能漏损监测仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("910" == deviceTypeIdShape) {
                        //远传水表
                        deviceTypeId = 2427;
                        deviceTypeName = "远传水表";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("2003" == deviceTypeIdShape) {
                        //温度压力监测仪
                        deviceTypeId = 2434;
                        deviceTypeName = "温度压力监测仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("2004" == deviceTypeIdShape) {
                        //雨量计，shape编号未确定
                        deviceTypeId = 2431;
                        deviceTypeName = "雨量计";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("2005" == deviceTypeIdShape) {
                        //入户燃气报警器，shaoe编号未确定
                        deviceTypeId = 2433;
                        deviceTypeName = "入户燃气报警器";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("2006" == deviceTypeIdShape) {
                        //远传电表，shape编号未确定
                        deviceTypeId = 2428;
                        deviceTypeName = "远传电表";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else if ("2007" == deviceTypeIdShape) {
                        //水质监测仪，shaoe编号未确定
                        deviceTypeId = 2425;
                        deviceTypeName = "水质监测仪";
                        devName = deviceTypeName + devCode;
                        modelPath += "传感器" + "\\" + deviceTypeName + ".gcm";
                    }
                    else {
                        alert("新类型编号" + deviceTypeIdShape);
                        return;
                    }

                    // 2.存储新设备到传感设备图层
                    var device = new Object();
                    device.deviceTypeName = deviceTypeName;
                    device.devCode = devCode;
                    device.wellCode = wellCode;
                    device.wellLayerName = wellLayerName;
                    device.street = street;
                    device.modelPath = modelPath;
                    device.longitude = longitude;
                    device.latitude = latitude;
                    device.modelLocation = modelPath;
                    device.devName = devName;
                    gisTools.saveDevice(device);

                    // 3.存储新设备到数组,循环结束后调用后台函数存到alarm_device表
                    var date = new Date();
                    var today = date.getFullYear() + "-" + (date.getMonth() + 1).toString() + "-" + date.getDate();
                    $.ajax({
                        type: "POST",
                        async: false,
                        url: $("#context").val() + "/alarm/device!bulkSave.do",
                        data: {
                            devCode: devCode,
                            devName: devName,
                            roadName: street,
                            gaocheng: height,
                            setupDate: today,
                            latitude: latitude,
                            longtitude: longitude,
                            attachLayer: wellLayerName,
                            attachFeature: wellCode,
                            ownerId: acceptPersonId,
                            deviceTypeId: deviceTypeId
                        },
                        success: function (result) {
                            var result = eval("(" + result + ")");
                            if (!result.success) {
                                alert("alarm_device" + result.msg);
                            }
                        }
                    });
                }
                alert("入库完成");
            } catch (e) {
            }

            //退出操作excel的实例对象
            excelObject.Application.Quit();
            //手动调用垃圾收集器
            CollectGarbage();
        },

        getWellLayerNameById: function (wellLayerNameId) {
            var wellLayerNameList =
                [
                    ["GD", "供电管线附属物"],
                    ["GD", "供电管线附属物"],
                    ["LD", "路灯管线附属物"],
                    ["XH", "交通信号管线附属物"],
                    ["DX", "电信管线附属物"],
                    ["DS", "有线电视管线附属物"],
                    ["WT", "网通管线附属物"],
                    ["YD", "移动管线附属物"],
                    ["LT", "联通管线附属物"],
                    ["TT", "铁通管线附属物"],
                    ["GT", "共通管线附属物"],
                    ["JK", "监控管线附属物"],
                    ["JS", "给水管线附属物"],
                    ["WS", "污水管线附属物"],
                    ["YS", "雨水管线附属物"],
                    ["HS", "雨污合流管线附属物"],
                    ["ZS", "中水管线附属物"],
                    ["RQ", "燃气管线附属物"],
                    ["TR", "天然气管线附属物"],
                    ["RL", "热力管线附属物"],
                    ["GY", "工业管线附属物"],
                    ["ZH", "综合管沟管线附属物"],
                    ["BM", "不明管线附属物"],
                    ["LJ", "垃圾管线附属物"],
                    ["KT", "空调管线附属物"],
                    ["XF", "消防管线附属物"]
                ];
            for (var i = 0; i < wellLayerNameList.length; i++) {
                if (wellLayerNameList[i][0] == wellLayerNameId) {
                    return wellLayerNameList[i][1];
                }
            }
            return null;
        },

        //存储新设备到传感设备图层
        saveDevice: function (device) {

            if (device.deviceTypeName.indexOf("光纤") != -1) {
                return;
            }

            var fields = new Array();
            fields.push("DEVICETYPE");
            fields.push("DEVICEID");
            fields.push("ATTACHID");
            fields.push("ATTACHLAYER");
            fields.push("ROAD");
            fields.push("MODELPATH");

            var values = new Array();
            values.push(device.deviceTypeName);
            values.push(device.devCode);
            values.push(device.wellCode);
            values.push(device.wellLayerName);
            values.push(device.street);
            values.push(device.modelPath);

            var position = Gis.getGlobalControl().CreatePoint3d();
            position.SetValue(device.longitude, device.latitude, 0);

            gisTools.addModelToLayer(device.modelLocation, device.devName, "传感设备图层", position, device.devCode, fields, values);
        },

        getNotNullValue: function (inValue) {
            var outValue = "";
            if (typeof inValue == "string") {
                inValue = inValue.replace(/[\r|\n|\t|\s]/g, "");
            }
            if (null != inValue) {
                outValue = inValue;
            }
            return outValue;
        },

        getPropertyIndex: function (excelSheet, propertyName) {
            var index = 1;
            while (true) {
                var value = excelSheet.Cells(1, index).value;
                if (!value) {
                    return -1;
                } else if (value == propertyName) {
                    return index;
                }
                index++;
            }
        },

        /*
         modelPath:模型在服务器端的路径
         descn:feature的描述
         layerName：图层的名称
         pos:必须为var position=globalControl.CreatePoint3d();
         featureName:要素的名称
         */
        addModelToLayer: function (modelPath, descn, layerName, pos, featureId, fields, values) {

            try {

                var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);

                if (layer.GetFeatureByName(featureId, true).Count != 0) {
                    alert("设备" + values[1] + "已经存在");
                    return;
                }

                var feature = layer.Dataset.CreateFeature();
                var model = Gis.getGlobalControl().CreateGeoModel();
                model.FilePath = modelPath;
                model.Position = pos;
                model.AltitudeMode = 0;
                feature.Geometry = model;
                feature.Name = featureId;

                for (var i = 0; i < fields.length; i++) {
                    feature.SetFieldValue(fields[i], values[i]);
                }

                layer.AddFeature(feature);
                layer.save();
            }
            catch (e) {
                alert("添加模型错误");
            }

        },

        /*
         modelPath:模型在服务器端的路径
         descn:feature的描述
         layerName：图层的名称
         pos:必须为var position=globalControl.CreatePoint3d();
         featureName:要素的名称
         */
        getFeatureInLayer: function (fieldValue, fieldName, layerName) {
            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(layerName);
            if (layer == null) {
                return null;
            }
            var features = layer.GetAllFeatures();
            for (var i = 0; i < features.Count; i++) {
                var feature = features.Item(i);
                var defn = feature.GetFieldValue(fieldName);
                if (defn == fieldValue) {
                    return feature;
                }
            }
            return null;
        },

        addSysLog: function (businessName, operationType, content) {
            jQuery.ajax({
                type: "POST",
                dataType: 'json',
                data: {
                    businessName: businessName,
                    operationType: operationType,
                    content: content
                },
                url: $('#context').val() + "/SysLog/saveSysLog.do"
            });
        },

        updateAlarmMarker: function (dev) {
            var devices = new Array();
            //删除旧marker
            var oldMarkFeat = DeviceService.getFeatureInLayer(
                DeviceService.getMarkerPrefix() + dev.devCode, DeviceService.getMainLayer());
            if (null != oldMarkFeat) {
                oldMarkFeat.delete();
            }

            var desc = DeviceService._getLabelInfo(dev);
            var feature = DeviceService.getFeatureInLayer(dev.devCode, "传感设备图层");
            var curValue = dev.normal ? dev.currentValue : dev.itemValue;
            if (feature != null) {
                var device = new DeviceBean(feature, desc, dev.normal, curValue);
                devices.push(device);
            }
            DeviceService.show(devices);
            var devCodes = new Array();
            devCodes.push(dev.devCode);
            parent.DeviceService.showLabels(devCodes, true);
        },

        getOneAlarmFiberMarkerFeat: function (devCode, dis) {
            var markFeats = parent.DeviceService.getFeaturesInLayer(devCode, DeviceService.getMainLayer());

            for (var i = 0; i < markFeats.Count; i++) {
                var descList = markFeats.item(i).Description.split(',');
                if (descList[0] == "false"
                    && DeviceService.toFixed(descList[2].split('^')[0]) == DeviceService.toFixed(dis)) {
                    return markFeats.item(i);
                }
            }
            return null;
        },

        getDisInFiber: function (fiberFeat, x, y) {
            var line = fiberFeat.Geometry;
            var totalLength = line.GetSpaceLength(false, 6378137);
            var shortestPointDis = totalLength;
            var dis = 0;

            for (var i = 0; i < totalLength; i++) {
                var segment = line.GetSegment(0, i);//第二个参数是distance
                var point = segment.Item(segment.PartCount - 1).Item(segment.Item(segment.PartCount - 1).Count - 1);
                //var point = segment.Item(0).Item(0);
                var pointX = point.X.toFixed(7);
                var pointY = point.Y.toFixed(7);

                var pointDis = Math.sqrt(Math.pow((pointX - x), 2) + Math.pow((pointY - y), 2));
                if (pointDis < shortestPointDis) {
                    dis = i;
                    shortestPointDis = pointDis;
                }
            }
            return dis;
        },

        screenToScene: function (x, y) {
            var screenPoint = Gis.getGlobalControl().CreatePoint2d();
            screenPoint.X = x;
            screenPoint.Y = y;
            var scenePoint = Gis.getGlobalControl().Globe.ScreenToScene(screenPoint);//SceneToScreen
            x = scenePoint.X;
            y = scenePoint.Y;

            return [x, y];
        },

        showSitePipe: function (site, ds) {
            for (var i = 0; i < ds.Count; i++) {
                var layerName = ds.Item(i).Caption();
                if (layerName.indexOf('SLS_') != 0 && gisTools.isToShow(layerName, site) &&
                    (layerName.indexOf("给水") > -1 || layerName.indexOf("供电") > -1 || layerName.indexOf("管沟") > -1 || layerName.indexOf("空调") > -1 ||
                    layerName.indexOf("路灯") > -1 || layerName.indexOf("燃气") > -1 || layerName.indexOf("污水") > -1 || layerName.indexOf("雨水") > -1 ||
                    layerName.indexOf("传感设备图层") > -1)) {
                    var layer = Gis.getGlobalControl().Globe.Layers.Add2(ds.Item(i));
                }
            }
            var layers = Gis.getGlobalControl().Globe.Layers;
            for (var i = 0; i < layers.Count; i++) {
                var layerName = layers.Item(i).Caption();
                if (layerName.indexOf("管线") == layerName.length - 2) {
                    Gis.pipelineLiuXiang(layerName, false);
                }
            }
            Gis.getGlobalControl().refresh();
        },

        isToShow: function (layerName, site) {
            return layerName == '传感设备图层'
                || (site.indexOf('东工业区') > -1 && layerName.indexOf('SLS_') != 0 && layerName.indexOf('五街坊_') != 0)
                || (site.indexOf('五街坊') > -1 && layerName.indexOf('五街坊_') == 0)
                || (site.indexOf('304场') > -1 && layerName.indexOf('SLS_') == 0);
        },

        updateOneMarker: function () {

        },

        /**
         * 关闭阀门分析
         *
         * @author zhangfan
         */
        closeValve: function (f) {
            if (null == f) {
                return null;
            }
            var line = f.Geometry;
            if (line.Type == 302) {
                //处理管线逻辑
                var layer = f.layer;
                var dataset = layer.Dataset;
                var netWorkDataset = dataset.DataSource.Item(dataset.Name + "Network");
                if (netWorkDataset == null) {
                    return null;
                }

                var valveResult = netWorkDataset.CloseValveAnalysis(f.ID, false, true);
                if (valveResult == null) {
                    return null;
                }
                var arrayFeatureIDs = valveResult.ResValveIDs;
                //var valveName = layer.Name.replace("管线","阀门");
                //阀门图层名称为 管线图层名称+阀门
                var valveName = layer.Name + "阀门";
                var valveLayer = Gis.getGlobalControl().Globe.Layers.Item(valveName);
                //if (arrayFeatureIDs.Count < 1 || arrayFeatureIDs[0]==null) {
                if (arrayFeatureIDs.Count < 1) {
                    return null;
                }
                if (arrayFeatureIDs != null && arrayFeatureIDs.Count > 0 && valveLayer != null) {
                    return [arrayFeatureIDs, valveLayer];
                }
            }
            return null;
        },

        showCloseValve: function (feature) {
            var result = gisTools.closeValve(feature);
            if (null == result) {
                alert("没有找到要关闭的阀门");
                return;
            }
            var wellCodeList = result[0];
            var valveLayer = result[1];

            /* $("#dg_closeValve").datagrid("reload",
             gisTools.getCloseValveList(wellCodeList, valveLayer));
             $.layer({
             type : 1,
             title : '需要关闭的阀门列表',
             shade : [ '', '', false ],
             offset : [ '150px', '210px' ],
             area : [ '260px', '260px' ],
             page : {
             dom : '#closeValveDiv'
             }
             });*/


            $("#dg_closeValve").datagrid({
                fit: true,
                columns: [[{
                    field: 'wellCode',
                    width: 230,
                    styler: function (value, row, index) {
                        return 'width:230;';
                    },
                    align: 'center'
                }]],
                pagination: false,
                rownumbers: true,
                singleSelect: true,
                data: gisTools.getCloseValveList(wellCodeList, valveLayer),
                onLoadSuccess: function () {

                    $(".datagrid-view").height(100);
                    $(".datagrid-view1").width(30);
                    $(".datagrid-view1 .datagrid-header").width(30);
                    $(".datagrid-view1 .datagrid-header").height(24);
                    $(".datagrid-view1 .datagrid-body").width(30);
                    $(".datagrid-view1 .datagrid-body").height(198);
                    $(".datagrid-view2 .datagrid-header").height(24);

                    if(closeValveFrm!=null) {
                        layer.close(closeValveFrm);
                    }
                    closeValveFrm= $.layer({
                        type: 1,
                        title: '需要关闭的阀门列表',
                        shade: ['', '', false],
                        offset: ['150px', '210px'],
                        area: ['260px', '260px'],
                        page: {
                            dom: '#closeValveDiv'
                        }
                    });
                },
                onClickRow: function (rowIndex, rowData) {
                    var wellCode = rowData.wellCode;
                    var feat = gisTools.getFeatureInLayer(wellCode, "编号", valveLayer.Name);
                    gisTools.addLabel(feat, "关闭此上游阀门");
                    Gis.getGlobalControl().Globe.JumpToFeature(feat, 20);
                }
            });
        },

        getCloseValveList: function (wellCodeList, valveLayer) {
            var json = '{"total":' + wellCodeList.Count + ',' +
                '"rows":[';
            for (var i = 0; i < wellCodeList.Count; i++) {
                var feat = valveLayer.GetFeatureByID(wellCodeList.Item(i));
                json += '{"wellCode":"' + feat.getFieldValue("编号") + '"}';
                if (i < wellCodeList.Count - 1) {
                    json += ',';
                }
            }
            json += ']}';
            json = JSON.parse(json);
            return json;
        },

        /* *******************************************************************************************
         * 代码块：清除所有地球关联各种操作的事件
         */
        //以下方法用来清除所有地球关联FireFeatureMouseClick的事件
        clearFeatureMouseClickEvent: function () {
            Gis.detachEvent("FireFeatureMouseClick", Gis.showballoon);
            Gis.detachEvent("FireFeatureMouseClick", Gis.showRQverticals);
            Gis.detachEvent("FireFeatureMouseClick", Gis.PipeBreakAnalysis);
        },
        //以下方法用来清除所有地球关联MouseUp的事件
        clearMouseUpEvent: function () {
            Gis.detachEvent("MouseUp", Gis.getGasLeakage0);
            Gis.detachEvent("MouseUp", Gis.addDangerousPoint);
            Gis.detachEvent("MouseUp", Gis.addDangerousArea);
//            Gis.detachEvent("MouseUp", Gis.addDevice);
//            Gis.detachEvent("MouseUp", Gis.closeValveAnalysis);
            Gis.detachEvent("MouseUp", Gis.showOneRainLevel);
            Gis.detachEvent("MouseUp", Gis.showRQverticals);
//            Gis.detachEvent("MouseUp", reLiBaoGuan.BufferAnalysis);
            Gis.detachEvent("MouseUp", Gis.PipeBreakAnalysis);

        },
        //以下方法用来清除所有地球关联FireTrackPolygonEnd的事件
        clearFireTrackPolygonEndEvent: function () {
            Gis.detachEvent("FireTrackPolygonEnd", Gis.showCutLine);
        }
        /*
         * 代码块：清除所有地球关联各种操作的事件结束
         ***************************************************************************************** */
    }
} ();