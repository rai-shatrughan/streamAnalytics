import React from 'react';
import * as echarts from 'echarts';
import json from '../data/tools.json';

export default class Tools extends React.Component {
  
    componentDidMount() {
        drawTools();
    }
  
    render() {
      return (
        <div className="pure-g g3 tools">
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecTools"></div>
        </div>
      );
    }
}

function drawTools(){
  var chartDom = document.getElementById('ecTools');
  var myChart = echarts.init(chartDom);
  var option;

  const data = json;

  option = {
    title: {
      text: 'TechStack',
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
      name: 'TechStack',
      data: data,
      radius: [0, '90%'],
      sort: undefined,
      // emphasis: {
      //   focus: 'ancestor'
      // },
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
            align: 'right',
            position: 'bottomLeft',
          }
        },
        {
          r0: '90%',
          r: '70%',
          label: {
            position: 'topRight',
            padding: 3,
            silent: false
          },
          itemStyle: {
            borderWidth: 1
          }
        }
      ]
    }
  };

  option && myChart.setOption(option);
}