/**
 * Created by lenovo on 2017/3/15.
 */
var regionAnalysisOption = {
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
            data : ['长安街', '复兴路', '西翠路', '永定路', '玉泉路', '阜石路', '莲石路'],
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
            data:[10, 52, 200, 334, 390, 330, 220]
        }
    ]
};
function initRegionAnalysisChart() {
    var regionAnalysisChart = echarts3.init(document.getElementById('regionAnalysis'));
    regionAnalysisChart.setOption(regionAnalysisOption);
}
