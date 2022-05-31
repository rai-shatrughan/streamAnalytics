import * as echarts from 'echarts';
import json from '../data/tools.json';

export default function drawTools(){
  var chartDom = document.getElementById('ecTools');
  var myChart = echarts.init(chartDom);
  var option;

  const data = json;

  option = {
    title: {
      text: 'Tools',
      textStyle: {
        fontSize: 14,
        align: 'center'
      },
      subtextStyle: {
        align: 'center'
      }
    },
    series: {
      type: 'sunburst',
      data: data,
      radius: [0, '90%'],
      sort: undefined,
      emphasis: {
        focus: 'ancestor'
      },
      levels: [
        {},
        {
          r0: '15%',
          r: '35%',
          itemStyle: {
            borderWidth: 2
          },
          label: {
            rotate: 'tangential'
          }
        },
        {
          r0: '35%',
          r: '70%',
          label: {
            align: 'right'
          }
        },
        {
          r0: '70%',
          r: '72%',
          label: {
            position: 'outside',
            padding: 3,
            silent: false
          },
          itemStyle: {
            borderWidth: 3
          }
        }
      ]
    }
  };

  option && myChart.setOption(option);
}
