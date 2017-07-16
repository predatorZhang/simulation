/**
 * Created by lenovo on 2017/3/15.
 */
var levelAnalysisOption = {
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
            data : ['一般事件', '高危事件'],
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
            data:[252, 200]
        }
    ]
};
function initLevelAnalysisChart() {
    var levelAnalysisChart = echarts3.init(document.getElementById('levelAnalysis'));
    levelAnalysisChart.setOption(levelAnalysisOption);
}
