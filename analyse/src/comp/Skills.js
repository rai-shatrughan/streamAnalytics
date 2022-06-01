import React from 'react';
import * as echarts from 'echarts';

export default class Skills extends React.Component {
  
    componentDidMount() {
        drawSkills();
    }
  
    render() {
      return (
        <div className="pure-g g2 skills">
            {Array.from(skills.keys()).map((key) => 
                <div className="pure-u-1 pure-u-md-1-3 skillsItem" id={key} key={key}></div>
            )}          
        </div>
      );
    }
}

const skills = new Map([
    ['MicroServices', 70],
    ['Observability', 75],
    ['Streaming', 75],
    ['NLP', 60],
    ['Reactive Programming', 60],
    ['Computer Vision', 60]
]);


function drawSkills(){
    for (const [key, value] of skills.entries()) {
        drawSkill(key, key, value)
    }
}

function drawSkill(elementId, chartName, score){
  var chartDom = document.getElementById(elementId);
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    tooltip: {
      formatter: '{b} : {c}%'
    },
    series: [
      {
        name: chartName,
        type: 'gauge',
        progress: {
          show: true
        },
        detail: {
          valueAnimation: true,
          formatter: '{value}'
        },
        data: [
          {
            value: score,
            name: chartName
          }
        ]
      }
    ]
  };

  option && myChart.setOption(option);
}
