import React from 'react';
import * as echarts from 'echarts';
import json from '../data/domains.json';

export default class Domains extends React.Component {
  
    componentDidMount() {
        drawDomains();
    }
  
    render() {
      return (
        <div className="pure-g g4 domains">
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecDomains"></div>
        </div>
      );
    }
}

function drawDomains(){
    var chartDom = document.getElementById('ecDomains');
    var myChart = echarts.init(chartDom);
    var option;

    const graph = json;

    myChart.showLoading();

    myChart.hideLoading();
    graph.nodes.forEach(function (node) {
        node.label = {
        show: node.symbolSize > 5
        };
    });
    option = {
        title: {
        // text: 'Domains',
        // top: 'top',
        // left: 'left',
        // z: 2,
        },
        textStyle: {
            fontSize: 12,
        },        
        tooltip: {},
        legend: [
        {
            data: graph.categories.map(function (a) {
            return a.name;
            }),
            orient: "horizontal",
            left: "center",
            top: 0,
        }
        ],
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
        {
            name: 'Domain',
            type: 'graph',
            layout: 'circular',
            circular: {
            rotateLabel: true
            },
            data: graph.nodes,
            links: graph.links,
            categories: graph.categories,
            roam: true,
            label: {
            position: 'right',
            formatter: '{b}'
            },
            lineStyle: {
            color: 'source',
            curveness: 0.3
            }
        }
        ]
    };
    myChart.setOption(option);


    option && myChart.setOption(option);
}