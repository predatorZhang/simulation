/**
 * Created by lenovo on 2017/3/15.
 */
var timeAnalysisOption = {
    tooltip: {
        trigger: 'axis'
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '5%',
        containLabel: true
    },
    xAxis:  {
        type: 'category',
        boundaryGap: false,
        data: ['0点','1点','2点','3点','4点','5点','6点','7点','8点','9点','10点','11点','12点','13点','14点','15点','16点','17点','18点','19点','20点','21点','22点','23点']
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value} 次'
        }
    },
    series: [
        {
            name:'异常次数',
            type:'line',
            data:[12, 9, 5, 8, 11, 14, 10, 9, 5, 13, 20, 23, 25, 37, 30, 29, 22, 21, 22, 17, 28, 36, 11, 7],
            markLine: {
                data: [
                    {type: 'average', name: '平均值'},
                    [{
                        symbol: 'none',
                        x: '90%',
                        yAxis: 'max'
                    }, {
                        symbol: 'circle',
                        label: {
                            normal: {
                                position: 'start',
                                formatter: '最大值'
                            }
                        },
                        type: 'max',
                        name: '最高点'
                    }]
                ]
            }
        }
    ]
};
function initTimeAnalysisChart() {
    var timeAnalysisChart = echarts3.init(document.getElementById('timeAnalysis'));
    timeAnalysisChart.setOption(timeAnalysisOption);
}
