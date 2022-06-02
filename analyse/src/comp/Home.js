import React from 'react';
import * as echarts from 'echarts';

export default class Home extends React.Component {
  
    componentDidMount() {
        drawHome();
    }
  
    render() {
      return (
        <div className="pure-g g1 home">
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecHomeBar"></div>
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecHomeName"></div>
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecHomeTag"></div>
        </div>
      );
    }
}
  
function drawHome(){
  drawHomeBar();
  drawHomeName();
  drawHomeTag();
}

function drawHomeBar(){
  var chartDom = document.getElementById('ecHomeBar');
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    graphic: {
      elements: [
        {
          type: 'group',
          left: 'center',
          top: 'center',
          children: new Array(15).fill(0).map((val, i) => ({
            type: 'rect',
            x: i * 20,
            shape: {
              x: 0,
              y: -40,
              width: 10,
              height: 80
            },
            style: {
              fill: '#5470c6'
            },
            keyframeAnimation: {
              duration: 1000,
              delay: i * 200,
              loop: true,
              keyframes: [
                {
                  percent: 0.5,
                  scaleY: 0.3,
                  easing: 'cubicIn'
                },
                {
                  percent: 1,
                  scaleY: 1,
                  easing: 'cubicOut'
                }
              ]
            }
          }))
        }
      ]
    }
  };


  option && myChart.setOption(option);
}

function drawHomeName(){
  var chartDom = document.getElementById('ecHomeName');
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    graphic: {
      elements: [
        {
          type: 'text',
          left: 'center',
          top: 'center',
          style: {
            text: 'Shatrughan Rai',
            fontSize: 40,
            lineDash: [0, 200],
            lineDashOffset: 0,
            fill: 'transparent',
            stroke: 'black',
            lineWidth: 1
          },
          keyframeAnimation: {
            duration: 10000,
            // loop: true,
            keyframes: [
              {
                percent: 0.5,
                style: {
                  fill: 'transparent',
                  lineDashOffset: 200,
                  lineDash: [200, 0]
                }
              },
              {
                // Stop for a while.
                percent: 0.7,
                style: {
                  fill: 'transparent'
                }
              },
              {
                percent: 1,
                style: {
                  fill: 'blue'
                }
              }
            ]
          }
        }
      ]
    }
  };

  option && myChart.setOption(option);
}

function drawHomeTag(){
  var chartDom = document.getElementById('ecHomeTag');
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    graphic: {
      elements: [
        {
          type: 'text',
          left: 'center',
          top: 'center',
          style: {
            text: 'Modder | Polyglot',
            fontSize: 20,
            lineDash: [0, 100],
            lineDashOffset: 0,
            fill: 'transparent',
            stroke: 'black',
            lineWidth: 1
          },
          keyframeAnimation: {
            duration: 5000,
            // loop: true,
            keyframes: [
              {
                percent: 0.7,
                style: {
                  fill: 'transparent',
                  lineDashOffset: 200,
                  lineDash: [200, 0]
                }
              },
              {
                // Stop for a while.
                percent: 0.8,
                style: {
                  fill: 'transparent'
                }
              },
              {
                percent: 1,
                style: {
                  fill: 'green'
                }
              }
            ]
          }
        }
      ]
    }
  };

  option && myChart.setOption(option);
}
