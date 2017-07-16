var IndexJS = function () {
    /**
     * 增加新的tag
     * @param title
     * @param url
     */
    var addTab = function (title, url) {
        if ($("#tab_div").tabs("exists", title)) {
            $("#tab_div").tabs("select", title);
        } else {
            var content = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>';
            $("#tab_div").tabs('add', {
                title: title,
                content: content,
                closable: true,
                fit: true,
                border: false
            })
        }
    }

    /**
     * 初始化tab
     * @param title
     */
    var selectTab = function (title) {
        if (title != "GIS数据浏览") {
            var tab = $("#tab_div").tabs("getTab", title);
            tab.panel("refresh");
        }
    }

    return {
        init: function () {
            $("#tab_div").tabs({
                border: false,
                fit: true,
                cache: false,
                onSelect: selectTab
            });

            $("#event_management").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    $("#tab_div").tabs("select", 'GIS数据浏览');

                    var iframe = document.getElementById("iframeDlg");
                    iframe.src = "";

                    if (row.id == 1) {
                        iframe.src = "./content/eventManagement.jsp";
                    }
                }
            });

            //雨水管线专项分析
            $("#rainPipeLineFunTbl").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    var iframe = document.getElementById("iframeDlg");
                    iframe.src = "";

                    $("#tab_div").tabs("select", 'GIS数据浏览');
                    if (row.id == 1) {
                        iframe.src = "./content/dangerousPoint.jsp";
                    }else if(row.id==2){
                        iframe.src = "./content/dangerousArea.jsp";
                    }else if(row.id==3){
                        iframe.src = "./content/fuhejiance.jsp";
                    }else if(row.id==4){
                        iframe.src = "./content/breakage.jsp";
                    }else if(row.id==5){
                        iframe.src = "./content/healthAnalysis.jsp";
                    }else if(row.id==6){
                        $("#clickEvent").val("water_pipe_line_tbl.1");
//                        Gis.initEvents();
//                        Gis.clearCalculate();
                        IndexJS.AfterSensorMarkerDone();
                        iframe.src = "./content/leakage.jsp";
                    }
                }
            });

            //处置预案管理
            $("#disposition_plan_manage").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    var iframe = document.getElementById("iframeDlg");
                    $("#tab_div").tabs("select", 'GIS数据浏览');
                    if (row.id == 1) {
                        iframe.src = "./content/dispositionPlan.jsp";
                    }
                }
            });

            //统计分析
            $("#statistic_analysis_table").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    if (row.id == 1) {
                        addTab("统计分析", "content/statisticAnalysis.jsp");
                    }
                }
            });

            //基础信息管理
            $("#process_config_manage_table").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    if (row.id == 1) {
                        addTab("处置流程管理", "flow/flow-show.do");
                    } else if (row.id == 2) {
                        addTab("事件派发管理", "content/model-flowDistribute.jsp");
                    } else if (row.id == 3) {
                        addTab("事件处置节点设置", "content/processConfig.jsp");
                    }
                }
            });

            //系统管理
            $("#system_manage_table").treegrid({
                onClickRow: function (row) {
                    Gis.clearCalculate();
                    DeviceService.hideAll();
                    var iframe = document.getElementById("iframeDlg");
                    $("#tab_div").tabs("select", 'GIS数据浏览');
                    if (row.id == 1) {
                        addTab("日志管理", "content/logManage.jsp");
                    }
                    if (row.id == 21) {
                        iframe.src = "./content/dma.jsp"
                    }
                }
            });

            //绘制设备的marker信息
        },

        showSensorMarker:function() {
            ALREADY_UPDATE = false;
            IndexJS.showSensorMarkerItv = setInterval(IndexJS.doShowSensorMarker,100);
        },
        doShowSensorMarker: function () {
            var layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(
                "传感设备图层"
            );
            var liquid_layer = Gis.getGlobalControl().Globe.Layers.GetLayerByCaption(Gis.getLayerLiquid());
            if (null != layer && null != liquid_layer) {
                clearInterval(IndexJS.showSensorMarkerItv);
                DeviceService.update();
            }
        },
        AfterSensorMarkerDone:function(){
            IndexJS.AfterSensorMarkerItv = setInterval(IndexJS.doAfterSensorMarkerDone,100);
        },
        doAfterSensorMarkerDone:function(){
            if(ALREADY_UPDATE){

                clearInterval(IndexJS.AfterSensorMarkerItv);
//                Gis.setSymbolImage("/images/earth/gsTuli.png",180,336);
                gisTools.hideAllLayers();
                DeviceService.showMarkerByPipeType("给水管线", "");
                DeviceService.showDevByPipeType("给水管线", "");
            }
        },
        /**
         * 开启底部信息栏
         * @param size
         */
        expendSouthPanel: function (size) {
            $("#global_center_layout").layout("expand", "south");
            if (size) {
                $("#global_center_layout").layout('panel', 'south').panel({
                    height: size
                })
            }
        },

        /**
         * 关闭底部信息栏
         */
        collapseSouthPanel: function () {
            $("#global_center_layout").layout("collapse", "south");
        },
        setSouthPanelTitle: function (title) {
            $("#global_center_layout").layout('panel', 'south').panel('setTitle', title);
        }
    }
}();