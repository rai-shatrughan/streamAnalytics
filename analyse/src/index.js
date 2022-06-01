import React from 'react';
import { createRoot } from 'react-dom/client';
import Intro from './comp/Intro';

import './style/index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';
import Skills from './comp/Skills';
import Tools from './comp/Tools';
import Graph from './comp/Graph';

class Home extends React.Component {

  render() {
    return (
      <div className="App">
        <Intro></Intro>
        <Skills></Skills>
        <Tools></Tools>  
        <Graph></Graph>
      </div>
    );
  }
}

const container = document.getElementById('root');
const root = createRoot(container);
root.render(<Home tab="home" />);

