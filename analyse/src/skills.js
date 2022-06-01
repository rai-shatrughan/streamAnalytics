import * as echarts from 'echarts';

export default function drawSkills(){
  drawSkill('ecSkillsMS', 'MicroServices', 70);
  drawSkill('ecSkillsOB', 'Observability', 75);
  drawSkill('ecSkillsST', 'Streaming', 75);
  drawSkill('ecSkillsNLP', 'NLP', 60);
  drawSkill('ecSkillsRP', 'Reactive Programming', 60);
  drawSkill('ecSkillsCV', 'Computer Vision', 60);
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
        detail: {
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
