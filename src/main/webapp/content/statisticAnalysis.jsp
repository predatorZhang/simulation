<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/taglibs.jsp"%>
<html>
<head>
    <title>统计分析</title>
    <link href="${ctx}/css/statisticAnalysis.css" rel="stylesheet" type="text/css">
    <!-- 引用easyUI样式 -->
    <link href="${ctx}/script/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/script/easyui/themes/icon.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="display: none;">
    <div id="analysis_tabs" class="easyui-tabs" style="height: 100%; width: 100%;" data-options="tabPosition:'left',tabHeight:60">
        <div title="<span class='tt-inner'><img src='${ctx}/images/statistic/large_smartart.png'/><br>区域专题图</span>" style="padding:5px">
            <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'north'" style="height:40px;padding-top:5px;padding-left: 20px;">
                        <table cellpadding="0" style="width:100%">
                            <tr>
                                <td>
                                    <label class="condition-label">日期:</label>
                                    <input id="regionStartDate" class="easyui-datebox" style="width:110px"/>
                                    <label class="condition-label">至</label>
                                    <input id="regionEndDate" class="easyui-datebox" style="width:110px"/>
                                </td>
                                <td style="text-align:left;">
                                    <a class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">统计</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'" style="padding:10px">
                        <div id="regionAnalysis" class="full-inside-container"></div>
                    </div>
                </div>
            </div>
        </div>
        <div title="<span class='tt-inner'><img src='${ctx}/images/statistic/large_shapes.png'/><br>时间段专题图</span>"  style="padding:5px">
            <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'north'" style="height:40px;padding-top:5px;padding-left: 20px;">
                        <table cellpadding="0" style="width:100%">
                            <tr>
                                <td>
                                    <label class="condition-label">日期:</label>
                                    <input id="timeAnalysisStartDate" class="easyui-datebox" style="width:110px"/>
                                    <label class="condition-label">至</label>
                                    <input id="timeAnalysisEndDate" class="easyui-datebox" style="width:110px"/>
                                </td>
                                <td style="text-align:left;">
                                    <a id="timeAnalysisQuery" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">统计</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'" style="padding:10px">
                        <div id="timeAnalysis" class="full-inside-container"></div>
                    </div>
                </div>
            </div>
        </div>
        <div title="<span class='tt-inner'><img src='${ctx}/images/statistic/large_chart.png'/><br>按事故类型统计</span>"  style="padding:5px">
            <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'north'" style="height:40px;padding-top:5px;padding-left: 20px;">
                        <table cellpadding="0" style="width:100%">
                            <tr>
                                <td>
                                    <label class="condition-label">日期:</label>
                                    <input id="eventTypeStartDate" class="easyui-datebox" style="width:110px"/>
                                    <label class="condition-label">至</label>
                                    <input id="eventTypeEndDate" class="easyui-datebox" style="width:110px"/>
                                </td>
                                <td style="text-align:left;">
                                    <a id="eventTypeQuery" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">统计</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'" style="padding:10px">
                        <div id="eventTypeAnalysis" class="full-inside-container"></div>
                    </div>
                </div>
            </div>
        </div>
        <div title="<span class='tt-inner'><img src='${ctx}/images/statistic/large_chart.png'/><br>按管线类型统计</span>"  style="padding:5px">
            <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'north'" style="height:40px;padding-top:5px;padding-left: 20px;">
                        <table cellpadding="0" style="width:100%">
                            <tr>
                                <td>
                                    <label class="condition-label">日期:</label>
                                    <input id="levelAnalysisStartDate" class="easyui-datebox" style="width:110px"/>
                                    <label class="condition-label">至</label>
                                    <input id="levelAnalysisEndDate" class="easyui-datebox" style="width:110px"/>
                                </td>
                                <td style="text-align:left;">
                                    <a id="levelAnalysisQuery" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">统计</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'" style="padding:10px">
                        <div id="levelAnalysis" class="full-inside-container"></div>
                    </div>
                </div>
            </div>
        </div>
        <div title="<span class='tt-inner'><img src='${ctx}/images/statistic/large_chart.png'/><br>按状态统计</span>"  style="padding:5px">
            <div class="easyui-panel" style="position: absolute;width: 100%;height: 100%;">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'north'" style="height:40px;padding-top:5px;padding-left: 20px;">
                        <table cellpadding="0" style="width:100%">
                            <tr>
                                <td>
                                    <label class="condition-label">日期:</label>
                                    <input id="stateAnalysisStartDate" class="easyui-datebox" style="width:110px"/>
                                    <label class="condition-label">至</label>
                                    <input id="stateAnalysisEndDate" class="easyui-datebox" style="width:110px"/>
                                </td>
                                <td style="text-align:left;">
                                    <a id="stateAnalysisQuery" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">统计</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'" style="padding:10px">
                        <div id="stateAnalysis" class="full-inside-container"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="rsURL" value="${rsURL}"/>
</body>
</html>

<!-- core js jquery 1.11 -->
<script src='${ctx}/script/easyui/jquery.min.js'></script>
<script src='${ctx}/script/easyui/jquery.easyui.min.js'></script>
<script src='${ctx}/script/easyui/locale/easyui-lang-zh_CN.js'></script>
<script src='${ctx}/script/echarts/echarts.min.js'></script>

<script src='${ctx}/script/content/statistic/regionAnalysis.js'></script>
<script src='${ctx}/script/content/statistic/timeAnalysis.js'></script>
<script src='${ctx}/script/content/statistic/eventTypeAnalysis.js'></script>
<script src='${ctx}/script/content/statistic/levelAnalysis.js'></script>
<script src='${ctx}/script/content/statistic/stateAnalysis.js'></script>

<script src="${ctx}/script/content/statisticAnalysis.js"></script>

<script>
    document.onreadystatechange = function() {
        if (document.readyState == "complete") {
            Analysis.init();
            initRegionAnalysisChart();
            $("#analysis_tabs").tabs({
                onSelect: function(title) {
                    if (title.indexOf("区域专题图") > 0) {
                        initRegionAnalysisChart();
                    } else if (title.indexOf("时间段专题图") > 0){
                        Analysis.getCountByTime();
                    } else if (title.indexOf("按事故类型统计") > 0){
                        Analysis.getCountByType();
                    } else if (title.indexOf("按管线类型统计") > 0){
                        Analysis.getCountByPipeType();
                    } else if (title.indexOf("按状态统计") > 0){
                        Analysis.getCountByState();
                    }
                }
            });
        }
    }
</script>