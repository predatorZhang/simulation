var Gis = function () {
    /*
     * 参数配置
     */
    var serverIp = config.getServerIp();
    var tomcatIP = config.getTomcatIP();
    var locaServerIp = config.getLocaServerIp();
    var dbIp = config.getDbIp();

    var serverPort = config.getServerPort();
    var tomcatPort = config.getTomcatPort();
    var locaServerPort = config.getLocaServerPort();

    var dbUserName = config.getDbUserName();
    var dbPassword = config.getDbPassword();
    var groundLayerName = config.getGroundLayerName();

    var dbServer = config.getDbServer();
    var deployName = config.getDeployName();

    var defaultRelativeDir = config.getDefaultRelativeDir();

    var localhost = config.getLocalhost();

    //全局变量
    var globalControl = null;
    var actionCache = new Array();//用于存储已经attachEvent
    //显示管线、对象信息用到的：
    var featureTooltip = null;//气泡
    var temp=0;
    var ds = null;


    //添加设备用到的：
    var x;
    var y;
    var feature;
    var layerDevice;
    var layerDangerousArea;
    var layerDangerousPoint;
    var layerGas;
    //雨水管线管井液位添加至此图层
    var cacheLayer = "waterFlow";
    //液位计测得的最新液位值显示的液位加至此图层
    var layerLiquid = "liquid";

    //透明度条Name
    var silderBarName = "silderBarGlobeOpaque";
    //初始视角
    var initCameraState;

    //燃气泄漏效果专用：
    var circleGasAlarm;
    var blinkGasId;
    var featuresGas = new Array();
    var blinkCounterGas = 0;

    //给水管线漏损闪烁专用：
    var circleSupplyAlarm;
    var blinkSupplyId;
    var featuresSupply = new Array();
    var blinkCounterSupply = 0;

    //液位计报警泄漏效果专用：
    var circleLiquidAlarm;
    var blinkLiquidId;
    var featuresLiquid = new Array();
    var blinkCounterLiquid = 0;

    //液位传感器区域统计
    var selectedFeatures = new Array();

    //定时器，采用自旋的方式去设置地面传感器Marker和透明度
    var showSensorMarkerItv;

    //燃气管线纵断面分析所选管线
    var featurePipeRqZongDuanMian;

    //控制圖例圖片
    var btnTuli;

    //光纤label feature
    var fiberLabelFeat;
    var dangerAreaLayerName = "危险区域图层";
    var dangerArea_polygon;
    var dangerSourceLayerName = "危险源图层";
    var dangerSourcePoint;
    var detectActiveX = function () {
        try {
            var comActiveX = new ActiveXObject("LOCASPACEPLUGIN.LocaSpacePluginCtrl.1");
        } catch (e) {
            return false;
        }
        return true;
    };

    var initGlobal = function () {

        var earthDiv = document.getElementById("earthDiv");

        //显示管线、对象信息用到的：
        //xtGasDisplay = new XtGasDisplay();
        //szWSGasDisplay = new SzWSGasDisplay();
        //xtGasDisplay.setSuccessor(szWSGasDisplay);

        if (detectActiveX() == false) {

            earthDiv.innerHTML = "<OBJECT ID=\"MyGlobal\" CLASSID=\"CLSID:0E7A33FF-6238-41A6-A38D-AC3F755F92B6\" WIDTH=\"100%\" HEIGHT=\"100%\"><param name=\"wmode\" value=\"transparent\"></OBJECT>";

        } else {

            try {

                earthDiv.innerHTML = "<OBJECT ID=\"MyGlobal\" CLASSID=\"CLSID:0E7A33FF-6238-41A6-A38D-AC3F755F92B6\" WIDTH=\"100%\" HEIGHT=\"100%\"><param name=\"wmode\" value=\"transparent\"></OBJECT>";

                globalControl = document.getElementById("MyGlobal");
                // globalControl.Globe.LatLonGridVisible = false; // 设置三维控件的经纬度为不显示
                globalControl.Globe.UnderGroundFloor.Visible = false;
                globalControl.Globe.NetCacheUsing = true;
                var color = globalControl.CreateColorRGBA();
                color.SetValue(255,255,255,255);
                globalControl.Globe.UserBackgroundColorValid = true;
                globalControl.Globe.UserBackgroundColor = color;

                featureTooltip = globalControl.CreateBalloonEx();
                featureTooltip.Create(globalControl.Handle);// 创建气泡
                featureTooltip.SetSize(4, 400); //宽度
                featureTooltip.SetSize(5, 300); //高度


                //设置点击事件 by liyulong 2015.9.24
                Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);//
                Gis.attachEvent("FireCameraBeginMove", Gis.hideballoon);// FireCameraBeginMove

                Gis.attachEvent("FireFeatureMouseInto", Gis.mouseinto);
                Gis.attachEvent("FireFeatureMouseOut", Gis.mouseout);

                //globalControl.Globe.Rain.Visible=true;

                globalControl.Globe.OverviewControl.Visible = false;
                globalControl.Globe.LatLonGridVisible = false;//初始化地球时取消经纬线显示；

                //globalControl.Globe.UsingVBO = false;
                globalControl.Globe.NetCacheUsing = true;   //控制是否启用模型网络缓存,默认为true
                globalControl.Globe.ReleaseMemOutOfView = true; // 控制模型移出视口是否释放显存和内存

                //宁波
                var cameraState = globalControl.CreateCameraState();
                cameraState.Longitude = 121.971767;
                cameraState.Latitude = 29.7777777;
                cameraState.Heading = 1;
                cameraState.Tilt = 44.392160966227;
                cameraState.Distance = 2300;
                cameraState.Altitude = 0;
                cameraState.AltitudeMode = 2;

                initCameraState = cameraState;
                globalControl.Globe.JumpToCameraState(initCameraState);

                //globalControl.Globe.Refresh();

            } catch (e) {

                alert(e.description);

            }
        }

    };

    var connectServer = function () {

        try {

            if (globalControl != null) {
                //连接locaserver
                globalControl.Globe.ConnectServer(locaServerIp, locaServerPort, "", "");

                globalControl.Globe.GroundOpaque = 0.5;

                globalControl.Globe.DefaultRelativeDir = defaultRelativeDir;

                //连接ORACLE数据库(地球中显示管线数据)
                ds = globalControl.Globe.DataManager.OpenOracleDataSource(dbServer, "", "", dbUserName, dbPassword);
                if(null != ds) {
                    for ( var i = 0; i < ds.Count; i++) {
                        var layerName = ds.Item(i).Caption();
                        if(layerName.indexOf("危险源图层")!=-1 ||
                            layerName.indexOf("危险区域图层")!=-1) {
                            var layer = globalControl.Globe.Layers.Add2(ds.Item(i));
                            if (layer != null) {
                                layer.ObjectMinVisiblePixelSize = -1;
                                layer.Visible = false;
                            }
                        }
                        //var layer = globalControl.Globe.Layers.Add2(ds.Item(i));
                    }
                }
            }

        } catch (e) {

            alert(e.description);

        }

    };

    /*
     * *******************************************************************************************
     * 代码块：清除所有地球关联各种操作的事件 start
     * *******************************************************************************************
     */
    //以下是新的思路来解决问题
    function pair(eventName, callback) {
        this.eventName = eventName;
        this.callback = callback;
    }
    var checkEvent = function(eventName) {
        if ("FireFeatureMouseClick" == eventName || "MouseDown" == eventName || "FireTrackPolygonEnd" == eventName || "MouseUp" == eventName) {
            return true;
        }
        return false;
    };
    var addEvent = function(eventName, callback) {
        if (checkEvent(eventName)) {
            actionCache.push(new pair(eventName, callback));
        }
    };
    var delEvent = function(eventName, callback) {
        if (checkEvent(eventName)) {
            for (var i = actionCache.length - 1; i >= 0; i --) {
                if (actionCache[i].eventName == eventName && actionCache[i].callback == callback) {
                    actionCache.splice(i,1);
                    return;
                }
            }
        }
    };
    /*
     * *******************************************************************************************
     * 代码块：清除所有地球关联各种操作的事件 end
     * *******************************************************************************************
     */

    return {
        /**
         *切換圖例圖片
         */
        setSymbolImage: function (imageUrl, height, width) {

            btnTuli.SetImage1(localhost + $('#context').val() + imageUrl);
            btnTuli.Height = height;
            btnTuli.Width = width;
        },

        drawPolygonWithFontColor: function (points, text, newFillColor, featName) {

            var newFeature = globalControl.CreateFeature();

            var geoPolygon = globalControl.CreateGeoPolygon3D();

            var polygonPnts = globalControl.CreatePoint3ds();

            for (var i = 0; i < points.length; i++) {

                polygonPnts.Add(points[i].x, points[i].y, 0);//添加点，一个面的组成至少有3个点

            }

            geoPolygon.AddPart(polygonPnts);

            var stylePolygon = globalControl.CreateSimplePolygonStyle3D();

            stylePolygon.OutlineVisible = true;
            stylePolygon.FillColor = newFillColor;
            geoPolygon.Style = stylePolygon;
            geoPolygon.AltitudeMode = 2;
            geoPolygon.moveZ(4);
            newFeature.Geometry = geoPolygon;

            var label = globalControl.CreateLabel();
            label.Text = text;
            label.Style = globalControl.CreateLabelStyle();
            label.Style.TextStyle = globalControl.CreateTextStyle();
            label.Style.TextStyle.FontSize = 14;
            label.Style.TextStyle.ForeColor = newFillColor;

            newFeature.Label = label;
            newFeature.Name = featName;
            globalControl.Globe.MemoryLayer.AddFeature(newFeature);
            globalControl.Globe.JumpToFeature(newFeature, 2000);
        },

        /**
         * 在MemoryLayer增加feature
         *
         * points : [{x:12.4545,y:34.34343},{x:23.434343,y:45454.34343},{x:32.4343,y:34.434232}],points用于存放区域中各个点的坐标。
         * text : 用于存放区域上需要添加的文字描述
         */
        drawPolygon: function (points, text, color, name) {

            var newFeature = globalControl.CreateFeature();

            var geoPolygon = globalControl.CreateGeoPolygon3D();

            var polygonPnts = globalControl.CreatePoint3ds();

            for (var i = 0; i < points.length; i++) {

                polygonPnts.Add(points[i].x, points[i].y, 0);//添加点，一个面的组成至少有3个点

            }

            geoPolygon.AddPart(polygonPnts);

            var stylePolygon = globalControl.CreateSimplePolygonStyle3D();

            stylePolygon.OutlineVisible = true;//面的外边框

            var newFillColor = globalControl.CreateColorRGBA();

            if (color >= 10) {//根据漏损率填充不同颜色的区域

                newFillColor.SetValue(255, 0, 0, 100);//填充颜色，对应红、绿、蓝、透明度

            } else if (color < 10) {

                newFillColor.SetValue(0, 0, 255, 100);

            } else {

                newFillColor.SetValue(255, 100, 38, 100);

            }

            stylePolygon.FillColor = newFillColor;

            geoPolygon.Style = stylePolygon;

            geoPolygon.AltitudeMode = 2;

            geoPolygon.moveZ(2);

            newFeature.Geometry = geoPolygon;

            var label = globalControl.CreateLabel();

            label.Text = text;

            newFeature.Label = label;

            if ("" != name || null != name) {
                newFeature.Name = name;
            }

            globalControl.Globe.MemoryLayer.AddFeature(newFeature);

            globalControl.Globe.JumpToFeature(newFeature, 2000);

        },

        /**
         * 调整透明度
         *
         * @param layerOpaque
         */
        setlayerOpaque: function (layerOpaque) {

            if (globalControl != null) {

                globalControl.Globe.GroundOpaque = layerOpaque;

                var layer = globalControl.Globe.Layers.getLayerByCaption(groundLayerName);

                if (layer != null) {

                    layer.Opaque = layerOpaque * 100;

                }

                var layerBuilding = globalControl.Globe.Layers.getLayerByCaption("jianzhu");
                if (layerBuilding != null) {
                    layerBuilding.Opaque = layerOpaque * 100;
                }
            }

        },

        /**
         * @no-using
         * @param feature
         * @param flag
         */
        addTextFlag: function (feature, flag) {

            if (feature.GetFieldCount() == 0) {

                return;

            }

            var label = globalControl.CreateLabel();

            label.Text = flag;

            feature.Label = label;

            feature.layer.save();

        },

        /**
         * @no-using
         * @param feature
         */
        delTextFlag: function (feature) {

            if (feature.GetFieldCount() == 0) {

                return;

            }

            if (feature.Label) {

                feature.Label.Text = "";

            }

            feature.layer.save();

        },

        getGlobalControl: function () {
            return globalControl;
        },

        getInitCameraState: function () {
            return initCameraState;
        },

        getSelectedFeatures: function () {
            return selectedFeatures;
        },

        getX: function () {
            return x;
        },

        getY: function () {
            return y;
        },

        getFeature: function () {
            return feature;
        },

        getLayerDevice: function () {
            return layerDevice;
        },
        getLayerDangerousArea: function () {
            return layerDangerousArea;
        },
        getLayerDangerousPoint: function () {
            return layerDangerousPoint;
        },
        getLayerGas:function(){
            return layerGas;
        },
        getCacheLayer: function () {
            return cacheLayer;
        },

        getLayerLiquid: function () {
            return layerLiquid;
        },

        getfeaturePipeRqZongDuanMian: function () {
            return featurePipeRqZongDuanMian;
        },

        /**
         * 初始化ActiveX插件，连接服务器
         */
        initEarth: function () {

            if ($('#earthDiv').size() != 0) {

                initGlobal();

                connectServer();

            }

        },

        /**
         * 从数据库中加载所需图层信息
         */
        loadData: function () {
            try {
                if (null != ds) {
                    //var site = $('#personType').val();
                    var site = '东工业区,五街坊';
                    gisTools.showSitePipe(site, ds);
                }
            } catch (e) {
                alert(e);
            }
        },

        /**
         * 绑定事件
         *
         * @param eventName
         * @param callback
         */
        attachEvent: function (eventName, callback) {

            if (globalControl == null) {

                alert("未初始化地球控件");

            }

            addEvent(eventName, callback);

            globalControl.Globe.FeatureMouseOverEnable = true;

            globalControl.attachEvent(eventName, callback);

        },

        /**
         * 解除时间绑定
         *
         * @param eventName
         * @param callback
         */
        detachEvent: function (eventName, callback) {

            if (globalControl == null) {

                alert("未初始化地球控件");

            }

            delEvent(eventName, callback);

            globalControl.detachEvent(eventName, callback);

            switch (eventName) {

                case 'FireTrackPolygonEnd':

                    globalControl.Globe.ClearLastTrackPolygon();

                    globalControl.Globe.Action = 0;

                    break;

            }
        },

        /**
         * 封装Globe.Action设置，对应值功能描述见《LocaSpace javascript API开发手册》2.4.1
         *
         * @param mode
         */
        setAction: function (mode) {

            try {

                if (globalControl != null) {

                    globalControl.Globe.Action = mode;

                    globalControl.refresh();

                }

            } catch (e) {

                alert(e.description);

            }

        },

        /**
         * 清理量算遗留下来的线，对API的封装
         */
        ClearMeasure: function () {

            try {

                if (globalControl != null) {

                    globalControl.Globe.ClearMeasure();

                    globalControl.refresh();

                }

            } catch (e) {

                alert(e.description);

            }

        },

        /**
         * 增加模型至图层（目前主要用于增加传感设备至“传感设备图层”）
         *
         * @param modelPath     模型在服务器端的路径
         * @param descn         feature的描述
         * @param layerName     图层的名称
         * @param pos           必须为var position=globalControl.CreatePoint3d();
         * @param featureId     要素的名称
         * @param fields
         * @param values
         */
        addModelToLayer: function (modelPath, descn, layerName, pos, featureId, fields, values) {

            try {

                var layer = globalControl.Globe.Layers.GetLayerByCaption(layerName);

                if (layer.GetFeatureByName(featureId, true).Count != 0) {

                    alert("该要素已经存在：" + featureId);

                    return;

                }

                var feature = layer.Dataset.CreateFeature();

                var model = globalControl.CreateGeoModel();

                model.FilePath = modelPath;

                model.Position = pos;

                model.AltitudeMode = 0;

                //调整模型入库的大小，三个参数分别为长宽高放大的倍数
                //model.Scale(5,5,5);

                feature.Geometry = model;

                feature.Name = featureId;

                for (var i = 0; i < fields.length; i++) {

                    feature.SetFieldValue(fields[i], values[i]);

                }
                layer.AddFeature(feature);
                globalControl.Globe.JumpToFeature(feature, 50);
                layer.save();
                alert("添加模型成功！");

            } catch (e) {

                alert("添加模型错误");

            }

        },

        /**
         *
         * @param featureId     要数ID，推荐使用数据库主键
         * @param layerName     图层名称
         */
        removeFeatureById: function (featureId, layerName) {

            var layer = globalControl.Globe.Layers.GetLayerByCaption(layerName);

            if (layer == null) {
                alert("无该图层" + layerName);
            }

            if (featureId != null) {
                var features = layer.GetFeatureByName(featureId, false);
                for (var i = 0; i < features.Count; i++) {
                    var feature = features.Item(i);
                    feature.Delete();
                }
                layer.Save();
                globalControl.Refresh();
            }
        },
        updateFeatureById: function (featureId, newFeatureId,layerName,fields,values,opt) {

            var layer = globalControl.Globe.Layers.GetLayerByCaption(layerName);

            if (layer == null) {
                alert("无该图层" + layerName);
            }
            if (featureId != null) {
//下面这段代码不可行
                var features = layer.GetFeatureByName(featureId, false);
                for (var i = 0; i < features.Count; i++) {
                    var feature = features.Item(i);
                    for (var i = 0; i < fields.length; i++) {
                        feature.SetFieldValue(fields[i], values[i]);
                    }
                    feature.Name = newFeatureId;
                    if(layer.Caption == dangerAreaLayerName) feature.Geometry.Style.FillColor = opt;
                    else if(layer.Caption == dangerSourceLayerName) feature.Geometry.FilePath = opt;
                }
//                var features = layer.GetAllFeatures();
//                for(var i = 0;i<features.Count;i++) {
//                    var feature = features.Item(i);
//                    if (feature.GetFieldValue(0) == featureId) {
//                        for (var j = 0; j < fields.length; j++) {
//                            feature.SetFieldValue(fields[j], values[j]);
//                        }
//                        if(layer.Caption == dangerAreaLayerName) feature.Geometry.Style.FillColor = opt;
//                        else if(layer.Caption == dangerSourceLayerName) feature.Geometry.FilePath = opt;
//                    }
//                }
                layer.Save();
                globalControl.Refresh();
            }
        },

        /**
         * 更新传感设备类型的模型路径
         *
         * @param srcPath
         * @param destPath
         * @param devType
         */
        updateModelPath4Device: function (srcPath, destPath, devType) {

            if (srcPath == destPath) {

                return;

            }

            var layer = globalControl.Globe.Layers.GetLayerByCaption("传感设备图层");

            if (layer != null) {

                var features = layer.GetAllFeatures();

                for (var i = 0; i < features.Count; i++) {

                    var feature = features.Item(i);

                    if (devType == feature.GetFieldValue(0)) {

                        var model = feature.Geometry;

                        if (model.FilePath == (globalControl.Globe.DefaultRelativeDir + srcPath)) {

                            model.FilePath = destPath;

                            model.AltitudeMode = 0;

                            feature.Geometry = model;

                            feature.SetFieldValue(0, devType);

                            layer.Save();

                        }

                    }

                }

                globalControl.refresh();

            }

        },

        /**
         * 透明度条监听事件
         *
         * @param sender
         * @param e
         */
        updateGlobeOpaque: function (sender, e) {
            if (sender.Name == silderBarName) {
                var opaque = sender.SliderValue;
                Gis.setlayerOpaque(opaque);
            }
        },

        /**
         * 初始化Gis地球上的按钮组
         */
        initGisTools: function () {

            var globeOpaqueId = setInterval(function () {//设置图层透明度为50%
                var layer = globalControl.Globe.Layers.getLayerByCaption(groundLayerName);
                if (layer != null) {
                    Gis.setlayerOpaque(0.6);
                    clearInterval(globeOpaqueId);
                }
            }, 100);

            //透明度条调整
            var HudSlider = globalControl.CreateHudSliderBar();
            HudSlider.Name = silderBarName;
            var offset = globalControl.CreatePoint2d();
            offset.X = 0;
            offset.Y = 0;
            HudSlider.OffSet = offset;
            HudSlider.MinOpaque = 100;
            HudSlider.MaxOpaque = 255;
            HudSlider.BarLength = 400; //滑动条长度
            HudSlider.SliderValue = 0.5; //滑动条的值
            HudSlider.Align = 2;
            globalControl.Globe.AddHudControl(HudSlider);
            globalControl.attachEvent("FireHudControlMouseMove", Gis.updateGlobeOpaque);//事件绑定
            globalControl.attachEvent("FireHudControlMouseUp", Gis.updateGlobeOpaque);//事件绑定

            //按钮通用变量
            var offset;
            var btnOffset;
            var color = globalControl.CreateColorRGBA();
            color.SetValue(0, 0, 0, 255);
            var textstyle = globalControl.CreateTextStyle();
            textstyle.ForeColor = color;
            textstyle.FontSize = 9;

            //快速定位按钮
            var btnLocation = globalControl.CreateHudButton;
            btnLocation.Name = "swdh";
            btnLocation.SetImage1(localhost + $('#context').val() + "/images/earth/btn_navi1.png");//($('#context').val()+"/s/media/image/bg/1.jpg");
            btnLocation.Text = "快速定位";

            //添加设备按钮
            var btnDevice = globalControl.CreateHudButton;
            btnDevice.Name = "dangerousPoint";
            btnDevice.SetImage1(localhost + $('#context').val() + "/images/earth/btn_device2.png");
            btnDevice.Text = "添加危险源";

            //横切面显示（液位统计按钮）
            var btnCutShow = globalControl.CreateHudButton;
            btnCutShow.Name = "DangerousArea";
            btnCutShow.SetImage1(localhost + $('#context').val() + "/images/earth/btn_rainLevel0.png");
            btnCutShow.Text = "添加危险区";

            //爆管分析按钮
            var btnCloseVal = globalControl.CreateHudButton;
            btnCloseVal.Name = "boomAnalysis";
            btnCloseVal.SetImage1(localhost + $('#context').val() + "/images/earth/btn_closeVal1.png");//($('#context').val()+"/s/media/image/bg/1.jpg");
            btnCloseVal.Text = "爆管分析";

            var btnGas = globalControl.CreateHudButton;
            btnGas.Name = "gasAnalysis";
            btnGas.SetImage1(localhost + $('#context').val() + "/images/earth/btn_navi1.png");//($('#context').val()+"/s/media/image/bg/1.jpg");
            btnGas.Text = "扩散分析";
            //清除分析按钮
            var btnClear = globalControl.CreateHudButton;
            btnClear.Name = "clear";
            btnClear.SetImage1(localhost + $('#context').val() + "/images/earth/btn_clear.png");//($('#context').val()+"/s/media/image/bg/1.jpg");
            btnClear.Text = "清除分析";

            //显示流向按钮
//            var btnRainDir = globalControl.CreateHudButton;
//            btnRainDir.Name = "rainDir";
//            btnRainDir.SetImage1(localhost + $('#context').val() + "/images/earth/btn_rainLevel2.png");
//            btnRainDir.Text = "显示流向";

            //选择对象按钮
            var btnSelect = globalControl.CreateHudButton;
            btnSelect.Name = "xzdx";
            btnSelect.SetImage1(localhost + $('#context').val() + "/images/earth/btn_select.png");//($('#context').val()+"/s/media/image/bg/1.jpg");
            btnSelect.MinOpaque = 0.7;
            btnSelect.MaxOpaque = 0.7;
            btnSelect.Text = "选择对象";
            btnSelect.TextStyle = textstyle;
            btnSelect.textAlign = 7;
            btnOffset = globalControl.CreatePoint2d();
            btnOffset.X = 0;
            btnOffset.Y = -10;
            btnSelect.textOffset = btnOffset;
            btnSelect.Height = 40;
            btnSelect.Width = 40;
            btnSelect.Align = 5;
            offset = globalControl.CreatePoint2d();
            offset.X = 0;//115
            offset.Y = 0;
            btnSelect.Offset = offset;


            //浏览模式按钮
            var btnScanMode = globalControl.CreateHudButton;
            btnScanMode.Name = "ScanMode";
            btnScanMode.SetImage1(localhost + $('#context').val() + "/images/earth/btn_hand.png");
            btnScanMode.MinOpaque = 0.7;
            btnScanMode.MaxOpaque = 0.7;
            btnScanMode.Text = "浏览模式";
            btnScanMode.TextStyle = textstyle;
            btnScanMode.textAlign = 7;
            btnOffset = globalControl.CreatePoint2d();
            btnOffset.X = -1;
            btnOffset.Y = -10;
            btnScanMode.textOffset = btnOffset;
            btnScanMode.Height = 40;
            btnScanMode.Width = 40;
            btnScanMode.Align=5;
            offset = globalControl.CreatePoint2d();
            offset.X = 0;
            offset.Y = 55;
            btnScanMode.Offset = offset;

            //图例
//            btnTuli = globalControl.CreateHudButton;
//            btnTuli.Name = "tuli";
//            btnTuli.SetImage1(localhost + $('#context').val() + "/images/earth/tuli3.png");
//            btnTuli.MinOpaque = 0.7;
//            btnTuli.MaxOpaque = 0.7;
//            btnTuli.Text = "";
//            btnTuli.TextStyle = textstyle;
//            btnTuli.textAlign = 7;
//            btnOffset = globalControl.CreatePoint2d();
//            btnOffset.X = -1;
//            btnOffset.Y = -10;
//            btnTuli.textOffset = btnOffset;
//            btnTuli.Height = 220;
//            btnTuli.Width = 320;
//            btnTuli.Align=6;
//            offset = globalControl.CreatePoint2d();
//            offset.X = 00;
//            offset.Y = 20;
//            btnTuli.Offset = offset;

            //地球上加载按钮
            var btnarray = [btnLocation,btnDevice,btnCutShow,btnCloseVal,btnGas,btnClear];//btnRainDir,
            var interval = 65;
            var xvalue = 5;
            for(var i=0;i<btnarray.length;i++){
                btnarray[i].MinOpaque = 0.7;
                btnarray[i].MaxOpaque = 0.7;
                btnarray[i].TextStyle = textstyle;
                btnarray[i].textAlign = 7;
                btnOffset = globalControl.CreatePoint2d();
                btnOffset.X = 0;
                btnOffset.Y = -10;
                btnarray[i].textOffset = btnOffset;
                btnarray[i].Height = 40;
                btnarray[i].Width = 40;
                offset = globalControl.CreatePoint2d();
                offset.X = xvalue;
                offset.Y = 0;
                btnarray[i].Offset = offset;
                globalControl.Globe.AddHudControl(btnarray[i]);
                xvalue = xvalue + interval;
            }
            globalControl.Globe.AddHudControl(btnSelect);
            globalControl.Globe.AddHudControl(btnScanMode);
//            globalControl.Globe.AddHudControl(btnTuli);

            Gis.attachEvent('FireHudControlMouseDown', function (sender, e) {
                if (!sender) {
                    return;
                }
                Gis.initEvents();
                switch (sender.Name) {
                    //快速定位
                    case 'swdh':
                        globalControl.Globe.JumpToCameraState(initCameraState);
                        Gis.setAction(0);
                        gisTools.clearMouseUpEvent();
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
                        break;

                    //添加设备
                    case 'dangerousPoint':
                        Gis.closeAllFrames();
                        var layer = globalControl.Globe.Layers.getLayerByCaption(dangerSourceLayerName);
                        if(null!=layer) {
                            var features = layer.getAllFeatures();
                            if (features.count()!= 0) {
                                layer.Visible = true;
                            }
                        }
                        alert("请点击选择危险源位置");
                        Gis.setAction(5);
                        gisTools.clearMouseUpEvent();
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("MouseUp", Gis.addDangerousPoint);
                        break;

                    //选择对象
                    case 'xzdx':
                        Gis.closeAllFrames();
                        Gis.setAction(5);
                        //var layer0 = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption("雨水管线");
                        //layer0.saveAs("D:/雨水管线.lgd");
                        break;

                    case 'gasAnalysis':
                        Gis.closeAllFrames();
                        alert("请点击燃气泄漏位置!");
                        Gis.setAction(5);
                        gisTools.clearMouseUpEvent();
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("MouseUp", Gis.getGasLeakage0);
                        break;
                    //清除分析
                    case 'clear':
                        Gis.closeAllFrames();
                        Gis.clearCalculate();
                        DeviceService.hideAll();
                        break;

                    //爆管分析
                    case 'boomAnalysis':
                        var f = globalControl.Globe.SelectedObject;
                        if (!f) {
                            alert("请点击爆管的管线位置!");
                            Gis.setAction(5);
                        }
                        gisTools.clearMouseUpEvent();
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("MouseUp", Gis.PipeBreakAnalysis);
                        break;
                    case 'rainLevel':
                        gisTools.hideAllLayers();
                        DeviceService.showMarkerByPipeType("雨水管线", "雨水管线附属物");
                        DeviceService.showDevByPipeType("雨水管线", "雨水管线附属物");
                        gisTools.showLayerByPipe("雨水");

                        alert("请选择雨水管线或管井");
                        Gis.setAction(5);
                        gisTools.clearMouseUpEvent();
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("MouseUp", Gis.showOneRainLevel);
                        break;

                    case 'DangerousArea':
                        var layer = globalControl.Globe.Layers.getLayerByCaption(dangerAreaLayerName);
                        if(null!=layer) {
                            var features = layer.getAllFeatures();
                            if (features.count()!= 0) {
                                layer.Visible = true;
                            }
                        }
                        alert("请在地球上绘制危险区域");
                        gisTools.clearFeatureMouseClickEvent();
                        Gis.attachEvent("FireTrackPolygonEnd", Gis.addDangerousArea);
                        Gis.setAction(17);
                        break;

                    case 'rainDir':
                        if (null != ds) {
                            var visible = false;
                            if ("显示流向" == btnRainDir.Text) {
                                btnRainDir.Text = "关闭流向";
                                visible = true;
                            } else if ("关闭流向" == btnRainDir.Text) {
                                btnRainDir.Text = "显示流向";
                                visible = false;
                            }

                            for (var i = 0; i < ds.Count; i++) {
                                var layerName = ds.Item(i).Caption();
                                if (layerName.indexOf("管线") == layerName.length - 2) {
                                    Gis.pipelineLiuXiang(layerName, visible);
                                }
                            }
                        }
                        break;
                    case 'flyTo':
                        Gis.flyTo();
                        break;
                }
            });
        },
        flyTo : function() {
            $.layer({
                type : 1,
                title : '管线管井定位',
                offset : [ '250px', '500px' ],
                shade : [ '', '', false ],
                area : [ '380px', '120px' ],
                page : {
                    dom : '#flyToDiv'
                }
            });
        },
        /**
         * 测试用
         *
         * @unfinished
         */
        flyToHell: function () {
            var layer = globalControl.Globe.Layers.GetLayerByCaption("给水管线附属物");
            if (null != layer) {
                var features = Gis.GetAllFeaturesByID(layer.ID);
                for (var i = 0; i < features.Count; i++) {
                    var feature = features.Item(i);
                    var defn = feature.GetFieldValue("编号");
                    if (defn == "GX_JSD_3507_EY_51") {
                        globalControl.Globe.JumpToFeature(feature, 3);
                        break;
                    }
                }
            }
        },

        /**
         * 根据ID 获取该层所有feature
         *
         * @param id
         * @returns {*}
         * @constructor
         */
        GetAllFeaturesByID: function (id) {// 通过图层id获取该图层的所有要素
            if (globalControl.Globe.Layers.Count > 0) {// 判断场景中是否有加载数据
                var pLayer = globalControl.Globe.Layers.GetLayerByID(id); // 根据图层id获取该图层
                if (pLayer == null) return null;
                var allfeatures = pLayer.GetAllFeatures();// 获取该图层的所有要素。
                if (allfeatures.Count > 0) {
                    for (i = 0; i < allfeatures.Count; i++) { // 遍历图层中的所有要素
                        var featureFolder = allfeatures.Item(i);
                        if (featureFolder.Features) {
                            if (featureFolder.Features.Count != 0) {
                                var features = featureFolder.Features;
                                return features;// 返回所有要素
                            }
                        } else {
                            return allfeatures;
                        }
                    }
                } else {
                    return allfeatures;
                }
            }
            return null;
        },

        /**
         * 格式化日期字符串
         *
         * @param date
         * @returns {string}
         */
        myformatter: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
        },

        /**
         * 显示全部管线
         */
        showAllPipeLayer: function () {
            for (var i = 0; i < globalControl.Globe.Layers.Count; i++) {
                var pLayer = globalControl.Globe.Layers.GetLayerByID(i);
                if (pLayer != null && pLayer.Caption().indexOf("管线") > -1) {
                    pLayer.Visible = true;
                }
            }
        },
        /**
         * 显示指定图层
         *
         * @author liyulong
         *
         * @param layerNameList 数组，传递图层名字
         */
        showSomePipeLayer: function (layerNameList) {
            for (var i = 0; i < globalControl.Globe.Layers.Count; i++) {
                var pLayer = globalControl.Globe.Layers.GetLayerByID(i);
                if (pLayer != null && pLayer.Caption().indexOf("管线") > -1) {
                    pLayer.Visible = false;
                }
            }
            for (var j = 0; j < layerNameList.length; j++) {
                var pLayer = globalControl.Globe.Layers.GetLayerByCaption(layerNameList[j]);
                if (pLayer != null) {
                    pLayer.Visible = true;
                }
            }
            globalControl.refresh();
        },

        /**
         * 关闭所有iframe
         */
        closeAllFrames: function () {
            var frames = document.getElementsByTagName("iframe");
            for (var i = 0; i < frames.length; i++) {
                if (frames.item(i).id != "iframeDlg" &&//如果是地球下部的iframe就不进行任何操作
                    frames.item(i).id != "alarmMsgWindow" &&
                    frames.item(i).id != "opaqueFrame" &&
                    frames.item(i).id != "iframeRight") {
                    frames.item(i).style.display = "none";
                }
            }
        },


        /**
         * 燃气泄漏影响区域分析
         * @author lyl
         * @time 2015.9.16
         */
        ranQiXieLou: function () {
            if (globalControl.Globe.SelObjectCount != 1) {
                alert("只能选择一条管线!");
                return;
            }
            var emitterFeature = globalControl.Globe.SelectedObject;

            if (emitterFeature != null && emitterFeature.Geometry != null && emitterFeature.Geometry.Type == 302) {
                //Gis.AddFireSmoke(emitterFeature);
                var line = emitterFeature.Geometry;
                var length = line.GetSpaceLength(true, 6378137);
                var polygon = Gis.getGasLeakage(emitterFeature, length / 2);
                //Gis.closeValveAnalysis();
            }
        },

        /**
         * 飞到光纤报警处
         *
         * @param featurename
         * @param layername
         * @param distance
         */
        flyToFiberAlarmPointByNameOfFeatureAndLayer: function (featurename, layername, distance) {

            var layer = globalControl.Globe.Layers.GetLayerByCaption(layername);

            if (layer != null) {

                var features = layer.GetAllFeatures();

                for (var i = 0; i < features.Count; i++) {

                    var feature = features.Item(i);

                    var defn = feature.GetFieldValue(1);

                    if (defn == featurename) {

                        if (feature.GetFieldValue(0).indexOf('GX') > -1) {

                            var line = feature.Geometry;

                            distance = distance - 0;

                            if (distance >= 1200) {

                                distance = 1100;

                            }

                            if (distance <= 0) {

                                distance = 0.01;

                            }

                            var segment = line.GetSegment(0, distance);

                            if (segment != null) {

                                var point = segment.Item(segment.PartCount - 1).Item(segment.Item(segment.PartCount - 1).Count - 1);

                                var cameraState = globalControl.CreateCameraState();

                                cameraState.Longitude = point.X.toFixed(5);

                                cameraState.Latitude = point.Y.toFixed(5);

                                cameraState.Heading = -3.51574088627348;

                                cameraState.Tilt = 44.392160966227;

                                cameraState.Distance = 100;

                                cameraState.Altitude = 0;

                                cameraState.AltitudeMode = 2;

                                globalControl.Globe.JumpToCameraState(cameraState);

                            }

                        } else {

                            globalControl.Globe.JumpToFeature(feature, 20);

                        }

                        break;

                    }

                }

            }

        },

        /***
         * 燃气烟雾效果显示(管线)
         *
         * @param layerAdd
         * @constructor
         */
        AddWaterByLine: function (layerAdd) {

            var line = layerAdd.Geometry;
            var length = line.GetSpaceLength(true, 6378137); //线的 长度
            var lineLine = line.GetSegment(0, length / 2);
            var point3d = lineLine.Item(lineLine.PartCount - 1).Item(lineLine.Item(lineLine.PartCount - 1).Count - 1); //line.Item(0).Item(0);//lineLine.Item(lineLine.PartCount - 1).Item(lineLine.Item(lineLine.PartCount - 1).Count - 1);

            //  烟火粒子示例,由三个发射器构成
            var geoParticle = globalControl.CreateGeoParticle();
            geoParticle.Position = point3d; //(116.31, 39.84, 0); // 添加到这个经纬度位置
            var emitter = globalControl.CreatePointParticleEmitter();
            emitter.TexturePath = "Resource/ParticleImage/drop3.png";
            geoParticle.TimerInterval = 1;
            emitter.SetSizeFix1(2.0, 2.0);
            emitter.VelFix = 30;
            emitter.VelRnd = 2;
            emitter.GravityAcc = 9.8;
            //水柱发射方向
            emitter.AngleXYFix = 180;
            emitter.AngleXYRnd = 90;

            //水柱分散左右角度
            //emitter.AngleXYRnd = 10;
            //水柱和地面的角度
            emitter.AngleXZFix = 80;
            //水柱上下分散的角度
            emitter.AngleXZRnd = 2;
            emitter.LifeFix = 1.3;
            emitter.LifeRnd = 1.0;
            emitter.RotFix = 0;
            emitter.RotRnd = 0;
            emitter.RotVelFix = 0;
            emitter.RotVelRnd = 0;
            emitter.EmitPerSec = 1500;
            var colorRndStart = globalControl.CreateColorRGBA();
            colorRndStart.SetValue(222, 222, 222, 100);
            var colorRndEnd = globalControl.CreateColorRGBA();
            colorRndEnd.SetValue(222, 222, 222, 50);
            emitter.ColorRndStart = colorRndStart; //Color.FromArgb(100, 222, 222, 222);
            emitter.ColorRndEnd = colorRndEnd; //Color.FromArgb(50, 222, 222, 222);
            emitter.Name = "点发射器1";
            // 将发射器添加到粒子对象中
            geoParticle.AddEmitter(emitter);
            geoParticle.Play();
            /* var feature = myGlobeCtrl.CreateFeature();
             geoParticle.AltitudeMode = 2;
             feature.Geometry = geoParticle;
             //myGlobeCtrl.Globe.FlyToFeature(feature);
             return layerAdd.AddFeature(feature);*/

            var feature = globalControl.CreateFeature();
            feature.Geometry = geoParticle;
            globalControl.Globe.MemoryLayer.AddFeature(feature);
        },

        /***
         * 燃气烟雾效果显示(marker)
         */
        AddWaterLine:function (layerAdd) {
            var geoParticle = globalControl.CreateGeoParticle();
            var point = layerAdd.Geometry.Position;
            if(point!=null){
                geoParticle.PositonX = point.X; //117.370542817108; //117.370477513519;
                geoParticle.PositonY = point.Y; //39.171062773766; //39.1710458878595;
                geoParticle.PositonZ = point.Z;

            }
            var emitter = globalControl.CreatePointParticleEmitter();
            emitter.TexturePath = "Resource/ParticleImage/drop3.png";
            geoParticle.TimerInterval = 1;
            emitter.SetSizeFix1(2.0, 2.0);
            emitter.VelFix = 30;
            emitter.VelRnd = 2;
            emitter.GravityAcc = 9.8;
            //水柱发射方向
            emitter.AngleXYFix = 180;
            emitter.AngleXYRnd = 90;

            //水柱分散左右角度
            //emitter.AngleXYRnd = 10;
            //水柱和地面的角度
            emitter.AngleXZFix = 80;
            //水柱上下分散的角度
            emitter.AngleXZRnd = 2;
            emitter.LifeFix = 1.3;
            emitter.LifeRnd = 1.0;
            emitter.RotFix = 0;
            emitter.RotRnd = 0;
            emitter.RotVelFix = 0;
            emitter.RotVelRnd = 0;
            emitter.EmitPerSec = 1500;
            var colorRndStart = globalControl.CreateColorRGBA();
            colorRndStart.SetValue(222, 222, 222, 100);
            var colorRndEnd = globalControl.CreateColorRGBA();
            colorRndEnd.SetValue(222, 222, 222, 50);
            emitter.ColorRndStart = colorRndStart; //Color.FromArgb(100, 222, 222, 222);
            emitter.ColorRndEnd = colorRndEnd; //Color.FromArgb(50, 222, 222, 222);
            emitter.Name = "点发射器1";
            // 将发射器添加到粒子对象中
            geoParticle.AddEmitter(emitter);
            geoParticle.Play();
            /* var feature = myGlobeCtrl.CreateFeature();
             geoParticle.AltitudeMode = 2;
             feature.Geometry = geoParticle;
             //myGlobeCtrl.Globe.FlyToFeature(feature);
             return layerAdd.AddFeature(feature);*/

            var feature = globalControl.CreateFeature();
            feature.Geometry = geoParticle;
            globalControl.Globe.MemoryLayer.AddFeature(feature);
        },
        /***
         * 冒火
         *
         * @param emitterFeature
         */
        huomiao: function (emitterFeature) {
            if (emitterFeature != null && emitterFeature.Geometry != null) {
                var geoParticle = globalControl.CreateGeoParticle();
                var point3d = globalControl.CreatePoint3D();
                point3d.SetValue(emitterFeature.Geometry.Position.X,
                    emitterFeature.Geometry.Position.Y,
                    emitterFeature.Geometry.Position.Z);
                geoParticle.Position = point3d;
                geoParticle.AltitudeMode = 2;

                var emitter = globalControl.CreateRingParticleEmitter();
                emitter.TexturePath = "ParticleImage/flare1.png";

                emitter.SetSizeFix1(2.0, 2.0);
                emitter.VelFix = 1;
                emitter.VelRnd = 5;

                emitter.AngleXYFix = 180;
                emitter.AngleXYRnd = 90;

                emitter.AngleXZFix = 80;
                emitter.AngleXZRnd = 2;

                emitter.LifeFix = 0.5;
                emitter.LifeRnd = 0.0;

                emitter.RotFix = 0;
                emitter.RotRnd = 0;

                emitter.RotVelFix = 0;
                emitter.RotVelRnd = 0;

                emitter.EmitPerSec = 100;
                emitter.IsLumAdded = true;

                var white = globalControl.CreateColorRGBA();
                white.SetValue(255, 255, 255, 255);

                var black = globalControl.CreateColorRGBA();
                black.SetValue(0, 0, 0, 255);

                emitter.ColorRndStart = white;
                emitter.ColorRndEnd = black;

                var effector2 = globalControl.CreateColorParticleEffector();
                effector2.SetColorChanged1(0, -1, -1, 0);
                effector2.StartTime = 0.0;
                effector2.EndTime = -1.0;
                emitter.AddEffector(effector2);

                geoParticle.AddEmitter(emitter);

                geoParticle.Play();
                var feature = globalControl.CreateFeature();
                feature.Geometry = geoParticle;

                globalControl.Globe.MemoryLayer.AddFeature(feature);
            }
        },

        /***
         * 喷泉
         *
         * @param emitterFeature
         */
        penquan: function (emitterFeature) {
            if (emitterFeature != null && emitterFeature.Geometry != null) {
                var geoParticle = globalControl.CreateGeoParticle();
                var point3d = globalControl.CreatePoint3D();
                point3d.SetValue(emitterFeature.Geometry.Position.X,
                    emitterFeature.Geometry.Position.Y,
                    emitterFeature.Geometry.Position.Z);
                geoParticle.Position = point3d;// (point3d.X, point3d.Y,point3d.Z)
                // ;//添加到这个经纬度位置
                geoParticle.AltitudeMode = 2;

                var emitter = globalControl.CreatePointParticleEmitter();
                emitter.TexturePath = "ParticleImage/test.png";
                emitter.SetSizeFix1(0.5, 2);
                emitter.VelFix = 10;
                emitter.VelRnd = 2;

                emitter.GravityAcc = 9.8;
                emitter.AngleXYFix = 0;
                emitter.AngleXYRnd = 180;

                emitter.AngleXZFix = 88;
                emitter.AngleXZRnd = 2;

                emitter.LifeFix = 5.0;
                emitter.LifeRnd = 1.0;

                emitter.RotFix = 0;
                emitter.RotRnd = 0;

                emitter.RotVelFix = 0;
                emitter.RotVelRnd = 0;

                emitter.EmitPerSec = 1000;

                var colorRndStart = globalControl.CreateColorRGBA();
                colorRndStart.SetValue(255, 255, 255, 33);

                var colorRndEnd = globalControl.CreateColorRGBA();
                colorRndEnd.SetValue(255, 255, 255, 11);
                emitter.ColorRndStart = colorRndStart;
                emitter.ColorRndEnd = colorRndEnd;
                emitter.IsLumAdded = false;

                // 将三个发射器添加到粒子对象中
                geoParticle.AddEmitter(emitter);
                geoParticle.Play();

                var feature = globalControl.CreateFeature();
                feature.Geometry = geoParticle;
                globalControl.Globe.MemoryLayer.AddFeature(feature);
                //var layerLiqui = globalControl.Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
                //layerLiqui.AddFeature(feature);
            }
        },

        penquanLocation:function(point3d){
            var geoParticle = globalControl.CreateGeoParticle();
            geoParticle.Position = point3d;// (point3d.X, point3d.Y,point3d.Z)
            // ;//添加到这个经纬度位置
            geoParticle.AltitudeMode = 2;

            var emitter = globalControl.CreatePointParticleEmitter();
            emitter.TexturePath = "ParticleImage/drop2.png";
            emitter.SetSizeFix1(1, 2);
            emitter.VelFix = 10;
            emitter.VelRnd = 2;

            emitter.GravityAcc = 9.8;
            emitter.AngleXYFix = 0;
            emitter.AngleXYRnd = 180;

            emitter.AngleXZFix = 88;
            emitter.AngleXZRnd = 2;

            emitter.LifeFix = 5.0;
            emitter.LifeRnd = 1.0;

            emitter.RotFix = 0;
            emitter.RotRnd = 0;

            emitter.RotVelFix = 0;
            emitter.RotVelRnd = 0;

            emitter.EmitPerSec = 1000;

            var colorRndStart = globalControl.CreateColorRGBA();
            colorRndStart.SetValue(255, 255,255, 33);

            var colorRndEnd = globalControl.CreateColorRGBA();
            colorRndEnd.SetValue(255, 255, 255, 11);
            emitter.ColorRndStart = colorRndStart;
            emitter.ColorRndEnd = colorRndEnd;
            emitter.IsLumAdded = false;

            // 将三个发射器添加到粒子对象中
            geoParticle.AddEmitter(emitter);
            geoParticle.Play();

            var feature = globalControl.CreateFeature();
            feature.Geometry = geoParticle;
            globalControl.Globe.MemoryLayer.AddFeature(feature);
        },
        /**
         * 烟火
         *
         * @param emitterFeature
         * @constructor
         */
        AddFireSmoke: function (emitterFeature) {
            var line = emitterFeature.Geometry;
            var length = line.GetSpaceLength(true, 6378137); //线的长度
            var lineLine = line.GetSegment(0, length / 2);
            var point3d = lineLine.Item(lineLine.PartCount - 1).Item(lineLine.Item(lineLine.PartCount - 1).Count - 1); //line.Item(0).Item(0);//lineLine.Item(lineLine.PartCount - 1).Item(lineLine.Item(lineLine.PartCount - 1).Count - 1);

            //  烟火粒子示例,由三个发射器构成
            var geoParticle = globalControl.CreateGeoParticle();
            geoParticle.Position = point3d; //(116.31, 39.84, 0); // 添加到这个经纬度位置
            var emitter = globalControl.CreateRingParticleEmitter();
            emitter.TexturePath = "ParticleImage/烟1111111111111.png";
            emitter.SetSizeFix1(2.0, 2.0); //20,20
            emitter.VelFix = 70; //70
            emitter.AccFix = -3.0; //-0.3f
            emitter.AngleXYFix = 180;
            emitter.AngleXYRnd = 90;
            emitter.AngleXZFix = 80;
            emitter.AngleXZRnd = 2;
            emitter.InnerRadius = 0;
            emitter.OuterRadius = 1;
            emitter.LifeFix = 2.0;
            emitter.LifeRnd = 1.0;
            emitter.RotFix = 0;
            emitter.RotRnd = 10;
            emitter.RotVelFix = 0;
            emitter.RotVelRnd = 180;
            emitter.EmitPerSec = 60;
            var white = globalControl.CreateColorRGBA();
            white.SetValue(255, 255, 255, 255);
            var black = globalControl.CreateColorRGBA();
            black.SetValue(0, 0, 0, 255);
            emitter.ColorRndStart = white;
            emitter.ColorRndEnd = black;
            emitter.IsLumAdded = false;
            var effector1 = globalControl.CreateScaleParticleEffector();
            effector1.SetScale1(4, 4); //4,4
            effector1.StartTime = 0;
            effector1.EndTime = 1.8;
            // 添加效果器
            emitter.AddEffector(effector1);
            var effector2 = globalControl.CreateColorParticleEffector();
            effector2.SetColorChanged1(0.6, 0.6, 0.6, 0);
            effector2.StartTime = 0.0;
            effector2.EndTime = -1.0; // 负数表示整个粒子生命结束
            emitter.AddEffector(effector2);
            var effector3 = globalControl.CreateColorParticleEffector();
            effector3.SetColorChanged1(0, 0, 0, -1);
            effector3.StartTime = 0.5;
            effector3.StartTimeType = 1;//EnumEffectorTimeType.ToDeadSeconds; // 距离粒子生命结束0.5秒
            effector3.EndTime = 0;
            effector3.EndTimeType = 1;//EnumEffectorTimeType.ToDeadSeconds; // 距离粒子生命结束0秒
            emitter.AddEffector(effector3);
            var emitter2 = globalControl.CreateRingParticleEmitter();
            emitter2.TexturePath = "ParticleImage/fire_final_2.png";
            emitter2.SetSizeFix1(4, 4); //8,8
            emitter2.VelFix = 30; //30
            emitter2.GravityAcc = -3.0; // 重力加速度 -3.0
            emitter2.AngleXYFix = 0;
            emitter2.AngleXYRnd = 180;
            emitter2.AngleXZFix = 90;
            emitter2.AngleXZRnd = 5;
            emitter2.InnerRadius = 0;
            emitter2.OuterRadius = 2; //30
            emitter2.LifeFix = 2.0;
            emitter2.LifeRnd = 0.5;
            emitter2.RotFix = 0;
            emitter2.RotRnd = 30;
            emitter2.RotVelFix = 0;
            emitter2.RotVelRnd = 60;
            emitter2.EmitPerSec = 300;
            var colorRndStart = globalControl.CreateColorRGBA();
            colorRndStart.SetValue(255, 0.38 * 255, 0, 255);
            var colorRndEnd = globalControl.CreateColorRGBA();
            colorRndEnd.SetValue(255, 0.404 * 255, 0, 255);
            emitter2.ColorRndStart = colorRndStart; //Color.FromArgb(255, 255, (int)(0.38 * 255), 0);
            emitter2.ColorRndEnd = colorRndEnd; //Color.FromArgb(255, 255, (int)(0.404 * 255), 0);
            var effector4 = globalControl.CreateColorParticleEffector();
            effector4.SetColorChanged1(-1, -1, -1, 0);
            effector4.StartTime = 1;
            effector4.StartTimeType = 1; // EnumEffectorTimeType.ToDeadSeconds; // 距离粒子生命结束0.5秒
            effector4.EndTime = 0;
            effector4.EndTimeType = 1; //EnumEffectorTimeType.ToDeadSeconds; // 距离粒子生命结束0秒
            emitter2.AddEffector(effector4);
            var emitter3 = emitter2.Clone();
            emitter3.TexturePath = "ParticleImage/fire_final_1.png";
            emitter3.GravityAcc = -2.0; // 重力加速度 -2.0f
            // 将三个发射器添加到粒子对象中
            geoParticle.AddEmitter(emitter);
            geoParticle.AddEmitter(emitter2);
            geoParticle.AddEmitter(emitter3);
            geoParticle.Play();
            var feature = globalControl.CreateFeature();
            feature.Geometry = geoParticle;
            globalControl.Globe.MemoryLayer.AddFeature(feature);
        },
        //给水渗漏
        getSupplyWaterLeakage : function (feature, distance, txt) {

            featuresSupply = [];
            blinkCounterSupply = 0;

            var point = feature.Geometry.Position;
            if (point != null) {
                var pntPosition = new ActiveXObject("LOCASPACEPLUGIN.GSAPoint3d");
                pntPosition.X = point.X.toFixed(5);
                pntPosition.Y = point.Y.toFixed(5);
                pntPosition.Z = point.Z.toFixed(5);

                //绘制圆
                var lineLength = 20;
                var line = globalControl.CreateGeoPolyline3D();
                var part = globalControl.CreatePoint3ds();
                part.Add2(pntPosition);
                pntPosition.X += 0.000000001;
                part.Add2(pntPosition);
                line.AddPart(part);
                var polygon = line.CreateBuffer(lineLength * 2, true, 5, true, false);
                polygon.AltitudeMode = 2;
                polygon.MoveZ(2);
                circleSupplyAlarm = globalControl.CreateFeature();
                circleSupplyAlarm.Description = feature.Description;
                circleSupplyAlarm.Geometry = polygon;
                circleSupplyAlarm.Geometry.Style = globalControl.CreateSimplePolygonStyle3D();
                var color = globalControl.CreateColorRGBA();
                color.SetValue(150, 150, 130, 150);//水的颜色
                circleSupplyAlarm.Geometry.Style.FillColor = color;
                globalControl.Globe.MemoryLayer.AddFeature(circleSupplyAlarm);

                var layerSupplyPipe = globalControl.Globe.Layers.GetLayerByCaption("给水管线");
                var featuresAffected = layerSupplyPipe.FindFeaturesInPolygon(polygon, false);
                for (var i = 0; i < featuresAffected.Count; i++) {
                    featuresSupply.push(featuresAffected.item(i));
                }

                var desc = "影响区域半径：" + lineLength + "(米)\n"
                    + "影响管线数量：" + featuresAffected.Count + "(条)";
                var feature0 = globalControl.CreateFeature();
                var dynamicMarker = globalControl.CreateGeoDynamicMarker();
                dynamicMarker.Position = point;
                feature0.Geometry = dynamicMarker;
                var label = globalControl.CreateLabel();
                label.Text = desc;
                feature0.Label = label;
                globalControl.Globe.MemoryLayer.AddFeature(feature0);

                blinkSupplyId = setInterval(function () {
                    circleSupplyAlarm.Visible = !circleSupplyAlarm.Visible;
                    var color = globalControl.CreateColorRGBA();

                    if (0 == blinkCounterSupply) {
                        color.setValue(0, 255, 0, 255);
                        for (var i = 0; i < featuresSupply.length; i++) {
                            featuresSupply[i].Geometry.Style.LineColor = color;
                        }
                    }

                    blinkCounterSupply = blinkCounterSupply + 1;
                    if (4 == blinkCounterSupply) {
                        color.setValue(0, 0, 255, 255);
                        for (var i = 0; i < featuresSupply.length; i++) {
                            featuresSupply[i].Geometry.Style.LineColor = color;
                        }

                        clearInterval(blinkSupplyId);
                        blinkCounterSupply = 0;
                    }
                }, 800);
            }
        },
        //液位计液位超限报警影响区域分析，feature为marker
        getLiquidLeakage: function (feature) {

            featuresLiquid = [];
            blinkCounterLiquid = 0;

            var point = feature.Geometry.Position;
            if (point != null) {
                var pntPosition = new ActiveXObject("LOCASPACEPLUGIN.GSAPoint3d");
                pntPosition.X = point.X.toFixed(5);
                pntPosition.Y = point.Y.toFixed(5);
                pntPosition.Z = point.Z.toFixed(5);

                //绘制圆
                var lineLength = 20;
                var line = globalControl.CreateGeoPolyline3D();
                var part = globalControl.CreatePoint3ds();
                part.Add2(point);
                point.X += 0.000000001;
                part.Add2(point);
                line.AddPart(part);
                var polygonBuffer = line.CreateBuffer(lineLength * 2, true, 5, true, false);
                var polygon0 = globalControl.CreateGeoPolygon3D();
                polygon0.AltitudeMode = 1;
                polygonBuffer.AltitudeMode = 1;
                polygonBuffer.moveZ(2);
                var psOfPolygon = polygonBuffer.item(0);
                for (var i = 0; i < psOfPolygon.Count; i++) {
                    var pointOfPolygon = psOfPolygon.item(i);
                    if (pointOfPolygon != null && pointOfPolygon.Z == 0) {
                        pointOfPolygon.Z = 0.7;
                    }
//                    psOfPolygon.item(i) = pointOfPolygon;// 报错
                }
                polygon0.AddPart(psOfPolygon);

                circleLiquidAlarm = globalControl.CreateFeature();
                circleLiquidAlarm.Description = feature.Description;
                var geoWater = polygon0.ConvertToGeoWater();
                geoWater.ReflectSky = false;
                geoWater.Play();
                geoWater.WaveSpeedX = 20;
                geoWater.WaveSpeedY = 20;
                circleLiquidAlarm.Geometry = geoWater;
                globalControl.Globe.MemoryLayer.AddFeature(circleLiquidAlarm);
                globalControl.refresh();

                var layerLiquidPipe = globalControl.Globe.Layers.GetLayerByCaption("雨水管线");
                var featuresAffected = layerLiquidPipe.FindFeaturesInPolygon(polygonBuffer, false);
                for (var i = 0; i < featuresAffected.Count; i++) {
                    featuresLiquid.push(featuresAffected.item(i));
                }

                var desc = "影响区域半径：" + lineLength + "(米)\n"
                    + "影响管线数量：" + featuresAffected.Count + "(条)";
                var feature0 = globalControl.CreateFeature();
                var dynamicMarker = globalControl.CreateGeoDynamicMarker();
                dynamicMarker.Position = point;
                feature0.Geometry = dynamicMarker;
                var label = globalControl.CreateLabel();
                label.Text = desc;
                feature0.Label = label;
                globalControl.Globe.MemoryLayer.AddFeature(feature0);

                //Gis.showFlow1(feature, )

                blinkLiquidId = setInterval(function () {
                    circleLiquidAlarm.Visible = !circleLiquidAlarm.Visible;
                    var color = globalControl.CreateColorRGBA();

                    if (0 == blinkCounterLiquid) {
                        color.setValue(0, 255, 0, 255);
                        for (var i = 0; i < featuresLiquid.length; i++) {
                            featuresLiquid[i].Geometry.Style.LineColor = color;
                        }
                    }

                    blinkCounterLiquid = blinkCounterLiquid + 1;
                    if (4 == blinkCounterLiquid) {
                        color.setValue(250, 128, 0, 255);
                        for (var i = 0; i < featuresLiquid.length; i++) {
                            featuresLiquid[i].Geometry.Style.LineColor = color;
                        }

                        clearInterval(blinkLiquidId);
                        blinkCounterLiquid = 0;
                    }
                }, 800);
            }
        },
        /**
         * 清除测算结果
         *
         * @author liyulong
         * @time 2015.09.07
         */
        clearCalculate: function () {
            //清除label：除了marker关联的label，其他label都在memoryLayer中
            globalControl.Globe.MemoryLayer.RemoveAllFeature();
            globalControl.Globe.Layers.GetLayerByCaption(Gis.getCacheLayer()).RemoveAllFeature();
            gisTools.recoverRainPipeColor();
            gisTools.recoverGasPipeColor();
            gisTools.showAllLayers();
            gisTools.hideLabel();//隐藏label
            Gis.setDangerAreaVisiable(false);
            Gis.setDangerSourceVisiable(false);
            Gis.setAction(0);
            Gis.getGlobalControl().Globe.clearAnalysis();
            globalControl.Refresh();
        },

        setDangerAreaVisiable:function(trueOrFalse){
            var dangerAreaLayerName = Gis.getDangerAreaLayerName();
            var globalControl = Gis.getGlobalControl();
            //TODO LIST：predator：将危险区域图层定义层一个全局的变量，便于后期维护！
            var layer=globalControl.Globe.Layers.GetLayerByCaption(dangerAreaLayerName);
            layer.Visible = trueOrFalse;
        },
        setDangerSourceVisiable:function(trueOrFalse){
            var dangerSourceLayerName = Gis.getDangerPointLayerName();
            var globalControl = Gis.getGlobalControl();
            //TODO LIST：predator：将危险区域图层定义层一个全局的变量，便于后期维护！
            var layer=globalControl.Globe.Layers.GetLayerByCaption(dangerSourceLayerName);
            layer.Visible = trueOrFalse;
        },

        /**
         * 平台坐标转经纬度坐标
         * @param x
         * @param y
         * @returns {*}
         */
        point2dToPoint3D: function (x, y) {
            var point2d = globalControl.CreatePoint2d();
            point2d.X = x;
            point2d.Y = y;
            var projectManager = globalControl.CreateProjectManager();
            projectManager.SetCurProject(0);
            var point2d3D = projectManager.Inverse1(point2d);
            var point3D = globalControl.CreatePoint3D();
            point3D.SetValue(point2d3D.X, point2d3D.Y, 0);
            return point3D;
        },

        /**
         * 经纬度坐标转平面坐标
         * @param point3D
         * @returns {*}
         */
        point3DToPoint2d: function (point3D) {
            var point2d = globalControl.CreatePoint2d();
            point2d.X = point3D.X;
            point2d.Y = point3D.Y;
            var projectManager = globalControl.CreateProjectManager();
            projectManager.SetCurProject(0);
            var point2d3D = projectManager.Forward1(point2d);
            point2d.X = point2d3D.X;
            point2d.Y = point2d3D.Y;
            return point2d;
        },

        /**
         * 显示指定的线状feature中液位的高度
         *
         * @param feature   线状feature对象
         * @param depth     液位高度
         * @param trans     管线透明度
         */
        showFlow1: function (feature, depth, trans) {

            if (feature.Geometry == null || feature.Geometry.Type == 305) {
                alert("对象非线状Feature");
                return;
            }
            var line = feature.Geometry;
            if (line.Style != null) {
                var color = line.Style.LineColor;
                color.A = trans;
                line.Style.LineColor = color;
            }
            var radius = line.Style.Radius;

            if (depth > radius * 2) {
                alert("水面高度超出管线直径");
                return;
            }
            depth = depth * 0.9;
            var startPoint = line.item(0).item(0);
            var endPoint = line.item(0).item(1);
            var startPoint2d = Gis.point3DToPoint2d(startPoint);
            var startPoint2d2 = Gis.point3DToPoint2d(endPoint);

            var f = globalControl.CreateFeature();
            var p = globalControl.CreateGeoPolygon3D();
            var ps = globalControl.CreatePoint3ds();
            var startPointJia = globalControl.CreatePoint3d();
            var startPointJian = globalControl.CreatePoint3d();
            var endPointJia = globalControl.CreatePoint3d();
            var endPointJian = globalControl.CreatePoint3d();
            var xieLv = (startPoint2d.Y - startPoint2d2.Y) / (startPoint2d.X - startPoint2d2.X);

            //管线按高度分十分，看水面在那个高度区间里面
            var count = Math.floor(depth / (radius / 24));
            var points = globalControl.CreatePoint3ds();
            for (var j = 0; j <= count; j++) {
                var h1 = (radius / 24) * j;
                var z1 = startPoint.Z - radius + h1;
                var z2 = endPoint.Z - radius + h1;
                if (xieLv == 0) {
                    var startPoint2dJia = Gis.point2dToPoint3D(startPoint2d.X + radius, startPoint2d.Y);
                    var startPoint2dJian = Gis.point2dToPoint3D(startPoint2d.X - radius, startPoint2d.Y);
                    var endPoint2dJia = Gis.point2dToPoint3D(endPoint2d.X + radius, endPoint2d.Y);
                    var endPoint2dJian = Gis.point2dToPoint3D(endPoint2d.X - radius, endPoint2d.Y);

                    startPointJia.SetValue(startPoint2dJia.X, startPoint2dJia.Y, z1);
                    startPointJian.SetValue(startPoint2dJian.X, startPoint2dJian.Y, z1);
                    endPointJian.SetValue(endPoint2dJia.X, endPoint2dJia.Y, z2);
                    endPointJia.SetValue(endPoint2dJian.X, endPoint2dJian.Y, z2);
                }
                else {
                    var angle = Math.atan(xieLv);
                    var d = Math.sqrt((radius * radius) - ((h1 - radius) * (h1 - radius)));

                    var sin = d * Math.sin(angle);
                    var cos = d * Math.cos(angle);
                    var startPoint2dJia = Gis.point2dToPoint3D(startPoint2d.X + sin, startPoint2d.Y - cos);
                    var startPoint2dJian = Gis.point2dToPoint3D(startPoint2d.X - sin, startPoint2d.Y + cos);
                    var startPoint2dJia2 = Gis.point2dToPoint3D(startPoint2d2.X + sin, startPoint2d2.Y - cos);
                    var startPoint2dJian2 = Gis.point2dToPoint3D(startPoint2d2.X - sin, startPoint2d2.Y + cos)

                    startPointJia.SetValue(startPoint2dJia.X, startPoint2dJia.Y, z1);
                    startPointJian.SetValue(startPoint2dJian.X, startPoint2dJian.Y, z1);

                    endPointJian.SetValue(startPoint2dJia2.X, startPoint2dJia2.Y, z2);
                    endPointJia.SetValue(startPoint2dJian2.X, startPoint2dJian2.Y, z2);
                }
                points.Add2(startPointJia);
                points.Add2(startPointJian);
                points.Add2(endPointJia);
                points.Add2(endPointJian);
            }

            var z1 = startPoint.Z - radius + depth;
            var z2 = endPoint.Z - radius + depth;
            if (xieLv == 0) {
                var startPoint2dJia = Gis.point2dToPoint3D(startPoint2d.X + radius, startPoint2d.Y);
                var startPoint2dJian = Gis.point2dToPoint3D(startPoint2d.X - radius, startPoint2d.Y);
                var endPoint2dJia = Gis.point2dToPoint3D(endPoint2d.X + radius, endPoint2d.Y);
                var endPoint2dJian = Gis.point2dToPoint3D(endPoint2d.X - radius, endPoint2d.Y);

                startPointJia.SetValue(startPoint2dJia.X, startPoint2dJia.Y, z1);
                startPointJian.SetValue(startPoint2dJian.X, startPoint2dJian.Y, z1);
                endPointJian.SetValue(endPoint2dJia.X, endPoint2dJia.Y, z2);
                endPointJia.SetValue(endPoint2dJian.X, endPoint2dJian.Y, z2);
            }
            else {
                var angle = Math.atan(xieLv);
                var d = Math.sqrt((radius * radius) - ((depth - radius) * (depth - radius)));

                var sin = d * Math.sin(angle);
                var cos = d * Math.cos(angle);

                var startPoint2dJia = Gis.point2dToPoint3D(startPoint2d.X + sin, startPoint2d.Y - cos);
                var startPoint2dJian = Gis.point2dToPoint3D(startPoint2d.X - sin, startPoint2d.Y + cos);
                var startPoint2dJia2 = Gis.point2dToPoint3D(startPoint2d2.X + sin, startPoint2d2.Y - cos);
                var startPoint2dJian2 = Gis.point2dToPoint3D(startPoint2d2.X - sin, startPoint2d2.Y + cos);

                startPointJia.SetValue(startPoint2dJia.X, startPoint2dJia.Y, z1);
                startPointJian.SetValue(startPoint2dJian.X, startPoint2dJian.Y, z1);

                endPointJian.SetValue(startPoint2dJia2.X, startPoint2dJia2.Y, z2);
                endPointJia.SetValue(startPoint2dJian2.X, startPoint2dJian2.Y, z2);
            }
            ps.Add2(startPointJia);
            for (var j = count; j >= 0; j--) {
                ps.Add2(points.item(4 * j));
            }
            for (var j = 0; j <= count; j++) {
                ps.Add2(points.item(4 * j + 1));
            }

            ps.Add2(startPointJian);
            ps.Add2(startPointJia);
            ps.Add2(startPointJian);
            ps.Add2(endPointJia);
            for (var j = count; j >= 0; j--) {
                ps.Add2(points.item(4 * j + 2));
            }
            for (var j = 0; j <= count; j++) {
                ps.Add2(points.item(4 * j + 3));
            }
            ps.Add2(endPointJian);
            ps.Add2(endPointJia);
            ps.Add2(endPointJian);

            p.AddPart(ps);
            var water = p.ConvertToGeoWater();
            water.Play();
            water.WaveSpeedX = 20;
            water.WaveSpeedY = 20;
            water.AltitudeMode = 2;
            f.Geometry = water;
            //TODO LIST:加载到内存图层？
            var layer0 = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getCacheLayer());
            layer0.AddFeature(f);

            //绘制水面立面
            var featureLiMian = globalControl.CreateFeature();
            var p3d = globalControl.CreatePoint3Ds();
            var point3dstart = globalControl.CreatePoint3D();
            point3dstart.setValue(startPoint.X, startPoint.Y, startPoint.Z - radius + depth);
            var point3dend = globalControl.CreatePoint3D();
            point3dend.setValue(endPoint.X, endPoint.Y, endPoint.Z - radius + depth);
            p3d.Add2(point3dstart);
            p3d.Add2(point3dend);

            var lineLiMian = globalControl.CreateGeoPolyline3D();
            lineLiMian.addPart(p3d);
            featureLiMian.Geometry = lineLiMian;
            radius = radius * 0.9;

            lineLiMian.AltitudeMode = 2;
            var extendStyle = globalControl.CreateExtendSectionLineStyle3D();
            var sectionPoints = globalControl.CreatePoint3ds();

            var radiusLiMian = Math.sqrt(Math.abs((radius * radius) - ((depth - radius) * (depth - radius))));

            if (depth > radius) {

                var _length = (radius - radiusLiMian) / 8;

                for (var j = 0; j <= 8; j++) {
                    var x = 0 - _length * j - radiusLiMian;
                    var y = Math.sqrt((radius * radius) - (x * x)) + radius - depth;
                    var z = 0;
                    var point3dItem = globalControl.CreatePoint3D();
                    point3dItem.SetValue(x, y, z);
                    sectionPoints.Add2(point3dItem);
                }

                var lengthItem = (radius / 8 );
                for (var j = 0; j <= 16; j++) {
                    var x = lengthItem * j - radius;
                    var y = -Math.sqrt((radius * radius) - (x * x)) - depth + radius;
                    var z = 0;
                    var point3dItem = globalControl.CreatePoint3D();
                    point3dItem.SetValue(x, y, z);
                    sectionPoints.Add2(point3dItem);
                }

                for (var j = 8; j >= 0; j--) {
                    var x = _length * j + radiusLiMian;
                    var y = Math.sqrt((radius * radius) - (x * x)) + radius - depth;
                    var z = 0;
                    var point3dItem = globalControl.CreatePoint3D();
                    point3dItem.SetValue(x, y, z);
                    sectionPoints.Add2(point3dItem);
                }

            } else {

                var lengthItem = (radiusLiMian * 2 / (count ));
                for (var j = 0; j <= count; j++) {
                    var x = lengthItem * j - radiusLiMian;
                    var y = -Math.sqrt((radius * radius) - (x * x));
                    var z = 0;
                    var point3dItem = globalControl.CreatePoint3D();
                    point3dItem.SetValue(x, y, z);
                    sectionPoints.Add2(point3dItem);
                }
            }
            var color = globalControl.CreateColorRGBA();
            color.SetValue(112, 144, 201, 255);
            extendStyle.LineColor = color;

            extendStyle.SetSectionPoints(sectionPoints);
            extendStyle.Closed = false;
            extendStyle.SetAnchorByAlign(1);// topCenter
            lineLiMian.Style = extendStyle;
            featureLiMian.Geometry = lineLiMian;

            layer0.ObjectMaxVisibleDistance = 1000;
            layer0.AddFeature(featureLiMian);
            //featureLiMian.HighLight = true;
        },

        /**
         * 功能：显示指定的方井feature的液位高度
         *
         * @param length
         * @param width
         * @param percent
         * @param feature
         * @param layer
         */
        showDepthInFangJin: function (length, width, percent, feature) {
            if (null == feature) {
                return;
            }

            if (feature.Geometry.Type != 305) {//判断是否模型
                alert("请选择模型对象！");
                return;
            }
            var model = feature.Geometry;
            var rotateAngel = model.RotateZ - 90;
            var topCenterPoint = model.GeoTopCenterPoint;
            var bottomCenterPoint = model.GeoBottomCenterPoint;
            var bottomCenterPoint2d = Gis.point3DToPoint2d(bottomCenterPoint);
            var height = (topCenterPoint.Z - bottomCenterPoint.Z) * percent;

            var pointTopLeft2d = Gis.point2dToPoint3D(
                bottomCenterPoint2d.X - noneGisTools.getHalfWidth(width, length, rotateAngel, 1),
                bottomCenterPoint2d.Y - noneGisTools.getHalfLength(width, length, rotateAngel, 1));
            var pointTopRight2d = Gis.point2dToPoint3D(
                bottomCenterPoint2d.X + noneGisTools.getHalfWidth(width, length, rotateAngel, 3),
                bottomCenterPoint2d.Y - noneGisTools.getHalfLength(width, length, rotateAngel, 3));
            var pointBottomLeft2d = Gis.point2dToPoint3D(
                bottomCenterPoint2d.X - noneGisTools.getHalfWidth(width, length, rotateAngel, 3),
                bottomCenterPoint2d.Y + noneGisTools.getHalfLength(width, length, rotateAngel, 3));
            var pointBottomRight2d = Gis.point2dToPoint3D(
                bottomCenterPoint2d.X + noneGisTools.getHalfWidth(width, length, rotateAngel, 1),
                bottomCenterPoint2d.Y + noneGisTools.getHalfLength(width, length, rotateAngel, 1));

            var pointTopLeft = globalControl.CreatePoint3d();
            var pointTopRight = globalControl.CreatePoint3d();
            var pointBottomLeft = globalControl.CreatePoint3d();
            var pointBottomRight = globalControl.CreatePoint3d();
            pointTopLeft.SetValue(pointTopLeft2d.X, pointTopLeft2d.Y, bottomCenterPoint.Z + height);
            pointTopRight.SetValue(pointTopRight2d.X, pointTopRight2d.Y, bottomCenterPoint.Z + height);
            pointBottomLeft.SetValue(pointBottomLeft2d.X, pointBottomLeft2d.Y, bottomCenterPoint.Z + height);
            pointBottomRight.SetValue(pointBottomRight2d.X, pointBottomRight2d.Y, bottomCenterPoint.Z + height);

            var f = globalControl.CreateFeature();
            var p = globalControl.CreateGeoPolygon3D();
            var ps = globalControl.CreatePoint3ds();
            ps.Add2(pointTopLeft);
            ps.Add2(pointTopRight);
            ps.Add2(pointBottomRight);
            ps.Add2(pointBottomLeft);
            p.AddPart(ps);

            var extrudeStyle = globalControl.CreateExtrudeStyle();
            extrudeStyle.ExtrudeType = 1;
            extrudeStyle.ExtrudeValue = -height;
            var polygonStyle = globalControl.CreateSimplePolygonStyle3D();
            var color = globalControl.CreateColorRGBA();
            color.SetValue(52, 85, 129, 255);
            polygonStyle.FillColor = color;
            extrudeStyle.BodyStyle = polygonStyle;
            extrudeStyle.HeadPartVisible = false;
            extrudeStyle.BodyPartVisible = true;
            var water = p.ConvertToGeoWater();
            water.ExtrudeStyle = extrudeStyle;
            water.Play();
            water.WaveSpeedX = 10;
            water.WaveSpeedY = 10;
            water.AltitudeMode = 2;
            f.Geometry = water;
            f.Name = "yewei," + feature.getFieldValue("编号");

            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getCacheLayer());
            layer.visible = true;
            layer.AddFeature(f);
        },

        /**
         * depth：深度，radius，井半径
         * 功能：显示圆井feature的液位高度.
         */
        showCircleWellLiquidLevel: function (feature, percent, radius) {

            if (feature == null || feature.Geometry.Type != 305) {
                return null;
            }
            var model = feature.Geometry;
            var point3d = model.GeoCenterPoint;
            var bottom = feature.Geometry.GeoBottomCenterPoint.Z;
            var top = feature.geometry.GeoTopCenterPoint.Z;
            var depth = (top - bottom) * percent;
            point3d.Z = bottom + depth;

            var line = globalControl.CreateGeoPolyline3D();
            var ps = globalControl.CreatePoint3ds();
            ps.Add2(point3d);
            ps.Add(point3d.X + 0.000000001, point3d.Y, point3d.Z);
            line.AddPart(ps);

            var polygonBuffer = line.CreateBuffer(1.3, true, radius, true, false);
            var polygon = globalControl.CreateGeoPolygon3D();
            polygon.AltitudeMode = 1;
            var psOfPolygon = polygonBuffer.item(0);
            for (var i = 0; i < psOfPolygon.Count; i++) {
                var pointOfPolygon = psOfPolygon.item(i);
                if (pointOfPolygon != null && pointOfPolygon.Z == 0) {
                    pointOfPolygon.Z = point3d.Z;
                }
//                psOfPolygon.item(i) = pointOfPolygon;
            }
            polygon.addPart(psOfPolygon);
            polygon.ExtrudeStyle = globalControl.CreateExtrudeStyle()
            polygon.ExtrudeStyle.ExtrudeType = 1; //拉伸模式
            polygon.ExtrudeStyle.ExtrudeValue = depth;//拉伸长度


            var extrudeStyle = globalControl.CreateExtrudeStyle();
            extrudeStyle.ExtrudeType = 1;
            extrudeStyle.ExtrudeValue = -depth;
            var polygonStyle = globalControl.CreateSimplePolygonStyle3D();
            var color = globalControl.CreateColorRGBA();
            color.SetValue(52, 85, 129, 255);
            polygonStyle.FillColor = color;
            extrudeStyle.BodyStyle = polygonStyle;
            extrudeStyle.HeadPartVisible = false;
            extrudeStyle.BodyPartVisible = true;
            var water = polygon.ConvertToGeoWater();
            water.ExtrudeStyle = extrudeStyle;
            water.Play();
            var ff = globalControl.CreateFeature();
            ff.Geometry = water;
            ff.Name = "yewei," + feature.getFieldValue("编号");
            var watersLayer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getCacheLayer());
            watersLayer.visible = true;
            watersLayer.AddFeature(ff);
        },

        /**
         * 根据layerName获取图层，将Name为code的feature高亮
         *
         * @param code
         * @param layerName
         */
        jumpToFeature: function (code, layerName) {

            var layer = globalControl.Globe.Layers.getLayerByCaption(layerName);

            if (layer != null) {

                var features = layer.GetAllFeatures();

                for (var i = 0; i < features.Count; i++) {

                    var feature = features.Item(i);

                    if (feature.Name == code) {

                        feature.HighLight = true;

                        globalControl.Globe.JumpToFeature(feature, 50);

                        break;

                    }

                }

            }

        },
        ConvertToPoint3D: function (x, y) {
            try {
                var point2D = globalControl.CreatePoint2D();
                point2D.X = x;
                point2D.Y = y;
                var  point3D =globalControl.Globe.ScreenToScene(point2D);
                return point3D;
            } catch (e) {
                alert("坐标转换失败:x=" + x + " y=" + y);
                return null;
            }
        },

        /**
         * 爆管分析
         *
         * @author zhangfan
         */
        PipeBreakAnalysis: function (e,sender,x,y) {//e,sender,x,y
            var f = globalControl.Globe.SelectedObject;
            if (null == f) {
                return;
            }
            globalControl.Globe.MemoryLayer.RemoveAllFeature();
            var line = f.Geometry;
            //TODO LIST:待确认：现在将供电管线也判断进去了，302不能作为一个标识
            if(line.Type!=302) {
                alert("请选择天然气管线、给水管线!");
                return;
            }
            var layer = globalControl.Globe.SelectedObjectLayer;
            if((layer.Name != "天然气管线" && layer.Name != "给水管线")) {
                alert("请选择天然气管线、给水管线!");
                return;
            }

            var dataset = layer.Dataset;
            var netWorkDataset = dataset.DataSource.Item(dataset.Name + "Network");
            if (netWorkDataset == null) {
                alert(layer.Name+"：无该拓扑图层");
                return;
            }

            if(layer.Name.indexOf("给水")>=0) {
                var point3d = Gis.ConvertToPoint3D(x,y);
                Gis.penquanLocation(point3d);
            }
            //处理管线逻辑
            gisTools.showCloseValve(f);
            gisTools.clearMouseUpEvent();
            gisTools.clearFeatureMouseClickEvent();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
            Gis.setAction(0);
        },

        showAlarmTimeData: function (msgData) {

            $.extend($.gritter.options, {
                position: 'bottom-left'
            });
            var unique_id = $.gritter.add({

                position: 'bottom-left',

                title: '新报警消息！',

                text: msgData[temp],

                image: $('#context').val() + "/images/avatar1.jpg",
                //image: "",
                sticky: true,

                time: '',

                class_name: 'my-sticky-class'

            });

            setTimeout(function () {
                $.gritter.remove(unique_id, {
                    fade: true,
                    speed: 'slow'
                });
            }, 5000);

            $(".gritter-item").click(function () {
                var typeName = $(".gritter-item p")[0].innerText.split('\r\n')[0];
                var devCode = "";
                if (typeName != DeviceService.getGasDevTypeName()) {
                    devCode = $(".gritter-item p")[0].innerText.split('\r\n')[1];
                } else {
                    devCode = DeviceService.getGasDevCode();
                }
                var feature = DeviceService.getFeatureInLayer(
                    DeviceService.getMarkerPrefix() + devCode, Gis.getLayerLiquid());
                globalControl.Globe.JumpToFeature(feature, 20);
                feature.Label.Visible = true;
            });
            //$(".gritter-item").css("position","absolute");
            //$(".gritter-item").css("z-index","100");
        },

        showballoon: function (feature, evt) {
            if (null == feature) {
                return;
            }

            // 1.设备Marker
            if (feature.Name.indexOf(DeviceService.getMarkerPrefix()) == 0) {
                noneGisTools.drawDevDataCurve(feature);
                DeviceService.showEffect(feature, evt, featureTooltip);
            }

            // 3.显示管线及管线附属物详细信息
            var layerName = feature.Layer.Caption;
            if (layerName != "传感设备图层" && layerName != Gis.getCacheLayer() && layerName != Gis.getLayerLiquid()) {
                noneGisTools.showDetails(feature, evt, featureTooltip);
            } else if (layerName == "传感设备图层") {
                noneGisTools.showDevDetails(feature, evt, featureTooltip);
            }
            globalControl.Refresh();
        },

        hideballoon: function (feature, evt) {// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
            if (featureTooltip.Visible) {
                featureTooltip.HideBalloon();
            }
        },

        mouseinto: function (feature, evt) {
            if (feature.layer && "传感设备图层" == feature.layer.Caption
                && feature.getFieldValue("DEVICETYPE").indexOf("光纤") > -1) {
                var scenePoint = gisTools.screenToScene(evt.X, evt.Y);
                var dis = gisTools.getDisInFiber(feature, scenePoint[0], scenePoint[1]);

                var geometry = Gis.getGlobalControl().CreateGeoDynamicMarker();
                var position = globalControl.CreatePoint3d();
                position.X = scenePoint[0];
                position.Y = scenePoint[1];
                position.Z = 0;
                geometry.Position = position;

                var feat = globalControl.CreateFeature();
                feat.Geometry = geometry;
                Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(feat);
                var desc = "距离起始点" + dis + "米";
                var fontColor = globalControl.CreateColorRGBA();
                fontColor.SetValue(0, 0, 0, 255);//红色
                fiberLabelFeat = gisTools.addLabel(feat, desc, fontColor, 14);
                fiberLabelFeat.Label.Visible = true;
            }
        },

        mouseout: function (feature, evt) {
            if (feature.layer && "传感设备图层" == feature.layer.Caption
                && feature.getFieldValue("DEVICETYPE").indexOf("光纤") > -1) {
                fiberLabelFeat.delete();
            }
        },

        /**
         * 异步请求雨水管线level，并进行展示
         */
        showOneRainLevel: function () {
            var feature = globalControl.Globe.SelectedObject;
            if (feature == null) {
                return;
            }
            if ("雨水管线" != feature.layer.Caption() && "雨水管线附属物" != feature.layer.Caption()) {
                alert("请选择雨水管线或管井");
                return;
            }
            var featureName = feature.getFieldValue(0);
            $.ajax({
                type: "POST",
                url: $("#context").val() + "/alarm/rain-liquid-analy!getOneRainLevel.do",
                data: {
                    dbCord: featureName
                },
                success: function (data) {
                    data = eval("(" + data + ")");
                    var list = data.split(",");
                    if (null == list[0] || "" == list[0]) {
                        list[0] = 0;
                    }
                    if (null == list[1] || "" == list[1]) {
                        list[1] = 0;
                    }
                    if (featureName.indexOf("_4000_") > -1) {
                        var desc = "实时液位：" + (list[0] / 1000.0).toFixed(2) + "(米)\n" +
                            "高度百分比：" + (list[1] * 100).toFixed(1) + "%";
                        gisTools.addLabel(feature, desc);
                        Gis.showFlow1(feature, list[0] / 1000.0, 50);
                    } else {
                        var desc = "实时液位：" + (list[0] / 1000.0).toFixed(2) + "(米)\n" +
                            "高度百分比：" + (list[1] * 100).toFixed(1) + "%";
                        gisTools.addLabel(feature, desc);
                        var length = 0;
                        var width = 0;
                        if (featureName.indexOf("_4001_") > -1) {
                            var length = 0.75;
                            var width = 0.75;
                        } else if (featureName.indexOf("_4002_") > -1) {
                            var length = 0.275;
                            var width = 0.45;
                        }
                        Gis.showDepthInFangJin(length, width, list[0] / 1000.0, feature);
                    }

                    globalControl.Refresh();
                }

            });

            gisTools.clearMouseUpEvent();
            gisTools.clearFeatureMouseClickEvent();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
            Gis.setAction(0);
        },

        showOneLiquidLevel: function (devCode, wellCode, attachLayer, liquidValue) {
            $.ajax({
                type: "POST",
                url: $("#context").val() + "/alarm/rain-liquid-analy!getWellDepth.do",
                data: {
                    dbCord: wellCode,
                    attachLayer: attachLayer
                },
                success: function (data) {
                    var wellDepth = eval("(" + data + ")");
                    if (devCode.indexOf() < 0) {
                        devCode = DeviceService.getMarkerPrefix() + devCode;
                    }
                    var markFeat = DeviceService.getFeatureInLayer(devCode, DeviceService.getMainLayer());
                    if (wellDepth == 0) {
                        console.error("井深数据有误[0米]，无法展示液位！");
                        return;
                    }
                    gisTools.addWaterLevelByMarker(markFeat, liquidValue / wellDepth);
                }
            });
        },

        /**
         * TODO LIST:弹出对应的jsp文件，显示当前绘制区域内的液位监测仪对应的管径的液位
         *
         * @param e
         * @constructor
         */
        BufferAnalysis: function (e) {
            selectedFeatures = [];
            var layer = globalControl.Globe.Layers.GetLayerByCaption("传感设备图层");
            var features = layer.FindFeaturesInPolygon(e, false);
            for (var i = 0; i < features.Count; i++) {
                var feature = features.item(i);
                var modelPath = feature.GetFieldValue(feature.GetFieldCount() - 1);
                if (modelPath.indexOf("液位") != -1) {
                    selectedFeatures.push(feature);
                }
            }
            $("#arlam_record_layout").layout("expand", "south");
            var iframe = document.getElementById("iframeDlg");
            iframe.src = "content/alarm/selected-liquidDevice.jsp";
        },

        JumpToCameraState: function (Longitude, Latitude, Heading, Tilt, Distance, Altitude, AltitudeMode) {
            var cameraState = globalControl.CreateCameraState();
            cameraState.Longitude = Longitude;
            cameraState.Latitude = Latitude;
            cameraState.Heading = Heading;
            cameraState.Tilt = Tilt;
            cameraState.Distance = Distance;
            cameraState.Altitude = Altitude;
            cameraState.AltitudeMode = AltitudeMode;
            globalControl.Globe.JumpToCameraState(cameraState);
        },


        //燃气管线泄漏函数
        getGasLeakage0: function () {
            var emitterFeature = globalControl.Globe.SelectedObject;
            if (null == emitterFeature) {
                return;
            }
            var layer = emitterFeature.layer;
            if (null == layer) {
                return;
            }

            if ("天然气管线" != layer.Caption()||"燃气管线" != layer.Caption()) {
                alert("请选择天然气管线！");
                return;
            }
            layerGas = $.layer({
                type: 2,
                title: '燃气扩散分析',
                area: ["500px", "180px"],
                maxWidth: '800',
                iframe: {src: $('#context').val() + '/content/gasAnalysis.jsp'}
            });

            gisTools.clearMouseUpEvent();
            gisTools.clearFeatureMouseClickEvent();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
            Gis.setAction(0);
        },

        //燃气管线泄漏影响区域及影响管线分析,feature为燃气管线
        getGasLeakage: function (feature, distance, diameter,txt) {

            featuresGas = [];
            blinkCounterGas = 0;

            var line0 = feature.Geometry;
            var segment = line0.GetSegment(0, distance);
            if (segment != null) {
                var point = segment.Item(segment.PartCount - 1).Item(segment.Item(segment.PartCount - 1).Count - 1);
                var pntPosition = new ActiveXObject("LOCASPACEPLUGIN.GSAPoint3d");
                pntPosition.X = point.X.toFixed(5);
                pntPosition.Y = point.Y.toFixed(5);
                pntPosition.Z = point.Z.toFixed(5);

                //绘制圆
                var lineLength = diameter;
                var line = globalControl.CreateGeoPolyline3D();
                var part = globalControl.CreatePoint3ds();
                part.Add2(pntPosition);
                pntPosition.X += 0.000000001;
                part.Add2(pntPosition);
                line.AddPart(part);
                var polygon = line.CreateBuffer(lineLength * 2, true, 5, true, false);
                polygon.AltitudeMode = 2;
                polygon.MoveZ(4);

//                var geoWater = polygon.ConvertToGeoWater();
//                geoWater.ReflectSky = false;
//                geoWater.Play();
//                geoWater.WaveSpeedX = 20;
//                geoWater.WaveSpeedY = 20;
                circleGasAlarm = globalControl.CreateFeature();
                circleGasAlarm.Name = "Circle" + feature.Name.substring(6);
                circleGasAlarm.Geometry = polygon;
                circleGasAlarm.Geometry.Style = globalControl.CreateSimplePolygonStyle3D();
                var color = globalControl.CreateColorRGBA();
                color.SetValue(255, 0, 0, 50);
                circleGasAlarm.Geometry.Style.FillColor = color;
                globalControl.Globe.MemoryLayer.AddFeature(circleGasAlarm);

                var layerGasPipe = globalControl.Globe.Layers.GetLayerByCaption("天然气管线");
                if(layerGasPipe==null){
                    return;
                }
                var featuresAffected = layerGasPipe.FindFeaturesInPolygon(polygon, false);
                for (var i = 0; i < featuresAffected.Count; i++) {
                    featuresGas.push(featuresAffected.item(i));
                }

                var desc = "影响区域半径：" + lineLength + "(米)\n"
                    + "影响管线数量：" + featuresAffected.Count + "(条)";
                var feature0 = globalControl.CreateFeature();
                var dynamicMarker = globalControl.CreateGeoDynamicMarker();
                dynamicMarker.Position = point;
                feature0.Geometry = dynamicMarker;
                var label = globalControl.CreateLabel();
                label.Text = desc;
                feature0.Label = label;
                globalControl.Globe.MemoryLayer.AddFeature(feature0);

                blinkGasId = setInterval(function () {
                    circleGasAlarm.Visible = !circleGasAlarm.Visible;
                    var color = globalControl.CreateColorRGBA();

                    if (0 == blinkCounterGas) {
                        color.setValue(0, 255, 0, 255);
                        for (var i = 0; i < featuresGas.length; i++) {
                            featuresGas[i].Geometry.Style.LineColor = color;
                        }
                    }

                    blinkCounterGas = blinkCounterGas + 1;
                    if (16 == blinkCounterGas) {
                        color.setValue(215, 0, 64, 255);
                        for (var i = 0; i < featuresGas.length; i++) {
                            featuresGas[i].Geometry.Style.LineColor = color;
                        }

                        clearInterval(blinkGasId);
                        blinkCounterGas = 0;
                    }
                }, 800);
            }
        },

        /**
         * 获取管道流向并展示（关闭）
         *
         * @param layerName
         */
        pipelineLiuXiang: function (layerName, visible) {
            var layer = globalControl.Globe.Layers.GetLayerByCaption(layerName);
            if (null != layer) {
                var feats = layer.GetAllFeatures();
                for (var j = 0; j < feats.Count; j++) {
                    var feat = feats.Item(j);
                    if (feat.Geometry.Type == 302) {
                        var lineStyle = feat.Geometry.Style;
                        if (lineStyle == null) {
                            lineStyle = globalControl.CreatePipeLineStyle3D();
                        }
                        if (lineStyle.ArrowStyle == null) {
                            var arrowStyle = globalControl.CreateArrowStyle();
                            var arrowColor = globalControl.CreateColorRGBA();
                            arrowColor.setValue(255, 0, 255, 0);
                            arrowStyle.BodyWidth = 2;
                            arrowStyle.BodyLen = 6;
                            arrowStyle.HeadWidth = 8;
                            arrowStyle.HeadLen = 10;
                            arrowStyle.OutlineVisible = true;
                            arrowStyle.OutlineColor = arrowColor;
                            arrowStyle.Speed = 25;
                            arrowStyle.Play();
                            lineStyle.ArrowStyle = arrowStyle;
                            lineStyle.ArrowVisible = visible;
                        }
                        lineStyle.ArrowVisible = visible;
                        feat.Geometry.Style = lineStyle;
                    }
                }
                globalControl.Refresh();
            }
        },

        addDangerousArea: function (e) {
            var longitudeLatitudeCollects = "";
            var polygon = e;
            polygon.AltitudeMode = 2;
            polygon.MoveZ(4);
            dangerArea_polygon = polygon;
            for (var i = 0; i < polygon.PartCount; i++) {
                var polygonParts = polygon.Item(i);
                for (var j = 0; j < polygonParts.Count - 1; j++) {
                    var point = polygonParts.Item(j);
                    var x = point.X;
                    var y = point.Y;
                    var longitudeLatitude = (""+x + ":" + y+"");
                    longitudeLatitudeCollects += (longitudeLatitude + "-");
                }
                longitudeLatitudeCollects = longitudeLatitudeCollects.substring(0, longitudeLatitudeCollects.lastIndexOf("-"));
            }
            layerDangerousArea=$.layer({
                type: 2,
                title: '新增危险区域',
                area: ["500px", "150px"],
                maxWidth: '800',
                iframe: {src: $('#context').val() + '/content/dangerousAreaAdd.jsp?points='+longitudeLatitudeCollects}
            });

            Gis.setAction(0);
            gisTools.clearMouseUpEvent();
            gisTools.clearFeatureMouseClickEvent();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
        },
        addDangerousPoint: function (e,sender,x,y) {
            var point3D =Gis.ConvertToPoint3D(x,y);
            if(point3D==null) return;
            dangerSourcePoint = point3D;
            layerDangerousPoint = $.layer({
                type: 2,
                title: '新增危险源',
                area: ["500px", "200px"],
                maxWidth: '800',
                iframe: {src: $('#context').val() + '/content/dangerousPointAdd.jsp?longitude='+point3D.x+'&latitude='+point3D.y}
            });
            Gis.setAction(0);
            gisTools.clearMouseUpEvent();
            gisTools.clearFeatureMouseClickEvent();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
        },
        createLabel:function(Feature,labelMsg,textColor,name){
            var feature = globalControl.CreateFeature();
            if (Feature.Geometry.Type == 305) {
                //处理点模型
                var dynamicMarker = globalControl.CreateGeoDynamicMarker();
                dynamicMarker.Position = Feature.Geometry.Position;
                feature.Geometry = dynamicMarker;
                feature.name=name;
            }
            else if (Feature.Geometry.Type == 302 || Feature.Geometry.Type == 401) {
                //处理线模型
                feature.Geometry = Feature.Geometry;
                feature.name=name;
            }
            else if(Feature.Geometry.Type == 303){
                //处理面模型
                feature.Geometry = Feature.Geometry;
                feature.name=name;
            }
            var label = globalControl.CreateLabel();
            label.Text = labelMsg;
            label.Style = globalControl.CreateLabelStyle();
            label.Style.TextStyle = globalControl.CreateTextStyle();
            label.Style.TextStyle.FontSize = 12;
            label.Style.TextStyle.ForeColor = textColor;
            feature.Label = label;
            globalControl.Globe.MemoryLayer.AddFeature(feature);
            globalControl.refresh();
        },
        /*
         color: var color = globalControl.CreateColorRGBA()
         polygon: polygonEnd响应结束后传入的参数
         layerName:图层名称
         featureId:要数主键ID
         */
        addPolygonToLayer:function(color,polygon,layerName,featureId,fields,values) {
            try{
                var layer = globalControl.Globe.Layers.GetLayerByCaption(layerName);
                if(layer.GetFeatureByName(featureId, true).Count!=0)
                {
                    alert("该要素已经存在：" + featureId);
                    return;
                }
                var style = globalControl.CreateSimplePolygonStyle3D();
                style.FillColor = color;
                style.OutlineVisible = true;
                polygon.Style = style;
                var feature = layer.Dataset.CreateFeature();
                feature.Geometry = polygon;
                feature.Name = featureId;
                layer.AddFeature(feature);
                for (var i = 0; i < fields.length; i++) {
                    feature.SetFieldValue(fields[i], values[i]);
                }
                globalControl.Globe.JumpToFeature(feature, 2000);
                layer.save();
                alert("添加成功！");
            }
            catch (e){
                alert("添加面错误");
            }
        },

        showRQverticals: function () {
            var feature = globalControl.Globe.SelectedObject;
            if (null == feature) {
                return;
            }
            if (feature.Layer.Caption != "天然气管线") {
                alert("请选择天然气管线！");
                return;
            }

            featurePipeRqZongDuanMian = feature;

            var iframe = document.getElementById("iframeDlg");
            iframe.src = "";
            iframe.src = "content/alarm/ranQiZongDuanMian.jsp";
            $("#arlam_record_layout").layout("panel", "south").panel({title: "燃气管线局部纵断面分析"});
            $("#arlam_record_layout").layout("expand", "south");
        },
        getDs:function(){
            return ds;
        },
        addDevMarkerFeatureInLayer: function(layer, lat, long, icon, id, description) {

            var feature = globalControl.CreateFeature();
            var maker = globalControl.CreateGeoMarker();
            var mstyle = globalControl.CreateMarkerStyle3D();
            mstyle.IconPath = icon;
            mstyle.IconScale = 1.3;
            maker.Style = mstyle;

            maker.X = long;//经度
            maker.Y =lat;//纬度
            maker.Z = 0;

            feature.Geometry = maker;
            feature.Name = id;
            feature.Description = description;
            feature.visible = false;
            layer.AddFeature(feature);
        },
        /**
         * 制定图层中动态创建带Label，maker的点要素对象
         *
         * @param layer
         * @param lat
         * @param long
         * @param icon
         * @param desc
         * @param id
         * @param status(true：设备正常，false：设备异常)
         */
        addMarkerFeatureInLayer: function (layer, lat, long, icon, desc, id, description) {

            var feature = globalControl.CreateFeature();
            var maker = globalControl.CreateGeoMarker();
            var mstyle = globalControl.CreateMarkerStyle3D();
            mstyle.IconPath = icon;
            maker.Style = mstyle;

            maker.X = long;//经度
            maker.Y = lat;//纬度
            maker.Z = 0;

            var fontColor = globalControl.CreateColorRGBA();
            var status = description.split(',')[0];
            if (status == "true") {
                fontColor.SetValue(0, 0, 255, 255);//蓝色
            } else {
                fontColor.SetValue(255, 0, 0, 255);//红色
            }

            var label = globalControl.CreateLabel();
            label.Text = desc;
            label.Style = globalControl.CreateLabelStyle();
            label.Style.TracktionLineWidth = 2;
            label.Style.SizeFixed = true;
            label.Style.TracktionLineType = 0;
            label.Style.TextStyle = globalControl.CreateTextStyle();
            label.Style.TextStyle.FontSize = 14;
            label.Style.TextStyle.ForeColor = fontColor;

            feature.Geometry = maker;
            feature.Name = id;
            feature.Label = label;
            feature.Description = description;
//            feature.Label.Visible = true;
            layer.AddFeature(feature);
            globalControl.Globe.JumpToFeature(feature, 1000);
        },

        initEvents: function () {
            Gis.setAction(0);
            for (var i = actionCache.length - 1; i >= 0; i--) {
                var temp = actionCache[i];
                globalControl.detachEvent(temp.eventName, temp.callback);
            }
            actionCache = new Array();
            Gis.attachEvent("FireFeatureMouseClick", Gis.showballoon);
        },
        getDangerArea_polygon:function(){
            return dangerArea_polygon;
        },
        getDangerAreaLayerName:function(){
            return dangerAreaLayerName;
        },
        getDangerPointLayerName:function(){
            return dangerSourceLayerName;
        },
        getDangerPoint3D:function(){
            return dangerSourcePoint;
        },
        getDangerPointModelPathByGrade:function(dangerSourceRank){
            var modelPath = defaultRelativeDir+"管道配件gcm/加密点/加密点.gcm";
            if(dangerSourceRank=="一级")    modelPath = defaultRelativeDir+"管道配件gcm/交叉口/交叉口.gcm";
            else if(dangerSourceRank=="二级") modelPath = defaultRelativeDir+"管道配件gcm/交越点/交越点.gcm";
            else if(dangerSourceRank=="三级") modelPath = defaultRelativeDir+"管道配件gcm/加密点/加密点.gcm";
            return modelPath;
        },
        getDangerAreaColor:function(level){
            var textColor = parent.Gis.getGlobalControl().CreateColorRGBA();
            if(level=="一级")  textColor.SetValue(255,0,0,255);
            else if(level=="二级")  textColor.SetValue(160,160,0,255);
            else if(level=="三级") textColor.SetValue(0,255,0,255);
            else  textColor.SetValue(255,255,255,255);
            return textColor;
        }

    }
}();
