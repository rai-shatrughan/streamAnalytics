import _ from 'lodash';
import printMe from './print.js';
import drawMe from'./dot.js';
import '../styles/index.css';
import Icon from '../images/icon.png';
import 'purecss/build/buttons.css';

function component() {
  const element = document.createElement('div');
  const chartDiv = document.createElement('div');
  chartDiv.setAttribute("id", "main");
  const btn = document.createElement('button');

  // Lodash, now imported by this script
  element.innerHTML = _.join(['Hello', 'webpack'], ' ');

  const myIcon = new Image();
  myIcon.src = Icon;
  element.appendChild(myIcon);

  btn.innerHTML = 'Click me and check the console!';
  btn.onclick = drawMe;
  element.appendChild(btn);

  element.appendChild(chartDiv);

  return element;
}

document.body.appendChild(component());
