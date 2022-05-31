import * as echarts from 'echarts';
import webkitDep from '../data/data.json';

export default function drawMe() {
var chartDom = document.getElementById("ecSkills");
var myChart = echarts.init(chartDom);
var option;

myChart.showLoading();
myChart.showLoading();
  myChart.hideLoading();
  option = {
    legend: {
      data: ['HTMLElement', 'WebGL', 'SVG', 'CSS', 'Other']
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        animation: true,
        label: {
          position: 'right',
          formatter: '{b}'
        },
        draggable: true,
        data: webkitDep.nodes.map(function (node, idx) {
          node.id = idx;
          return node;
        }),
        categories: webkitDep.categories,
        force: {
          edgeLength: 5,
          repulsion: 20,
          gravity: 0.2
        },
        edges: webkitDep.links
      }
    ]
  };
  myChart.setOption(option);

option && myChart.setOption(option);
}
