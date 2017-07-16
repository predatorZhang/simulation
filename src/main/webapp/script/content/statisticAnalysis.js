/**
 * Created by lenovo on 2017/4/7.
 */
var Analysis = function () {

    var defaultDays = 7;

    /**
     * 工具函数，测试 o 对象是否为函数对象
     * @param o
     * @returns {boolean}
     */
    function isFunction(o) {
        if (o && typeof o === "function") {
            return true;
        }
        return false;
    }

    /**
     * 远程跨域调用通用方法
     * @param url
     * @param askdata
     * @param success
     * @param error
     */
    function ajaxJSONP(url, askdata, success, error) {
        $.ajax({
            type: "get",
            url: url,
            crossDomain:true,
            data: askdata,
            dataType: 'jsonp',
            jsonp: "jsoncallback",
            jsonpCallback:"success_jsoncallback",
            success: function (data) {
                if (isFunction(success)) {
                    success(data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (isFunction(error)) {
                    error(XMLHttpRequest, textStatus, errorThrown);
                }
            }
        });
    }

    /**
     * 默认的异常处理函数
     * @param XMLHttpRequest
     * @param textStatus
     * @param errorThrown
     */
    function defaultError(XMLHttpRequest, textStatus, errorThrown) {
        alert(textStatus);
    }

    /**
     * 生成默认的成功处理函数
     * @param id
     * @returns {Function}
     */
    function defaultSuccess(id) {
        return function(result) {
            if (result.success) {
                var list = result.data;
                var numList = [];
                var legendList = [];
                for(var i=0; i<list.length; i++) {
                    numList.push(list[i][0]);
                    legendList.push(list[i][1]);
                }
                defaultBarOption.xAxis[0].data = legendList;
                defaultBarOption.series[0].data = numList;
                var charts = echarts3.init(
                    document.getElementById(id)
                );
                charts.setOption(defaultBarOption);
            }
        }
    }

    /**
     * 获取日期范围
     * @param startDate
     * @param endDate
     * @param days
     * @returns {{dateStart: *, dateEnd: *}}
     */
    function timeRangeMaker(startDate, endDate, days) {
        if (!startDate.datebox("getValue")
            && !endDate.datebox("getValue")) {
            parent.gisTools.setStartAndEndDate(
                startDate, endDate, days
            );
        }
        return {
            dateStart: startDate.datebox("getValue"),
            dateEnd: endDate.datebox("getValue")
        }
    }

    /**
     * 获取时间点
     * @param date
     * @returns {{date: *}}
     */
    function timePointMaker(date) {
        if (!date.datebox("getValue")) {
            date.datebox("setValue", parent.Gis.myformatter(new Date()));
        }
        return {
            dateStart: date.datebox("getValue"),
            dateEnd: date.datebox("getValue")
        }
    }

    /**
     * 通用柱状图表option
     */
    var defaultBarOption = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '5%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'异常次数',
                type:'bar',
                barWidth: '60%'
            }
        ]
    };

    return {
        init: function() {
            $("#timeAnalysisQuery").click(Analysis.getCountByTime);
            $("#eventTypeQuery").click(Analysis.getCountByType);
            $("#levelAnalysisQuery").click(Analysis.getCountByPipeType);
            $("#stateAnalysisQuery").click(Analysis.getCountByState);
        },

        getCountByTime : function() {
            ajaxJSONP(
                $("#rsURL").val() + "/alarm/getCountByTimeRange.do",
                timeRangeMaker(
                    $('#timeAnalysisStartDate'),
                    $("#timeAnalysisEndDate"),
                    defaultDays
                ),
                defaultSuccess('timeAnalysis'),
                defaultError
            );
        },

        getCountByType : function() {
            ajaxJSONP(
                $("#rsURL").val() + "/alarm/getCountByType.do",
                timeRangeMaker(
                    $('#eventTypeStartDate'),
                    $("#eventTypeEndDate"),
                    defaultDays
                ),
                defaultSuccess('eventTypeAnalysis'),
                defaultError
            );
        },

        getCountByPipeType : function() {
            ajaxJSONP(
                $("#rsURL").val() + "/alarm/getCountByPipeType.do",
                timeRangeMaker(
                    $('#levelAnalysisStartDate'),
                    $("#levelAnalysisEndDate"),
                    defaultDays
                ),
                defaultSuccess('levelAnalysis'),
                defaultError
            );
        },

        getCountByState : function() {
            ajaxJSONP(
                $("#rsURL").val() + "/alarm/getCountByState.do",
                timeRangeMaker(
                    $('#stateAnalysisStartDate'),
                    $("#stateAnalysisEndDate"),
                    defaultDays
                ),
                defaultSuccess('stateAnalysis'),
                defaultError
            );
        }
    }
}();