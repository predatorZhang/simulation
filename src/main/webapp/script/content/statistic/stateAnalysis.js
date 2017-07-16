/**
 * Created by lenovo on 2017/3/15.
 */
var stateAnalysisOption = {
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
            data : ['处理中', '待派发'],
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
            data:[156, 22]
        }
    ]
};
function initStateAnalysisChart() {
    var stateAnalysisChart = echarts3.init(document.getElementById('stateAnalysis'));
    stateAnalysisChart.setOption(stateAnalysisOption);
}
