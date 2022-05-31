import * as echarts from 'echarts';
import json from '../data/tools.json';

export default function drawTools(){
  var chartDom = document.getElementById('ecTools');
  var myChart = echarts.init(chartDom);
  var option;

  const data = json;

  option = {
    visualMap: {
      type: 'continuous',
      min: 0,
      max: 10,
      inRange: {
        color: ['#2F93C8', '#AEC48F', '#FFDB5C', '#F98862', '#975e6d', '#f99e1c', '#f7f1bd']
      }
    },
    series: {
      type: 'sunburst',
      data: data,
      radius: [0, '100%'],
      label: {
        rotate: 'radial'
      }
    }
  };

  option && myChart.setOption(option);
}
