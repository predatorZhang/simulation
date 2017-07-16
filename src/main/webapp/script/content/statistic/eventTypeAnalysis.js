/**
 * Created by lenovo on 2017/3/15.
 */
var eventTypeAnalysisOption = {
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
            data : ['热力管线异常', '给水管线异常', '污水管线异常', '燃气管线异常', '通讯管线异常', '雨水管线异常'],
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
            barWidth: '60%',
            data:[52, 200, 334, 390, 330, 220]
        }
    ]
};
function initEventTypeAnalysisChart() {
    var eventTypeAnalysisChart = echarts3.init(document.getElementById('eventTypeAnalysis'));
    eventTypeAnalysisChart.setOption(eventTypeAnalysisOption);
}
