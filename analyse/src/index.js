import React from 'react';
import { createRoot } from 'react-dom/client';

import './style/index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';

import Header from './comp/Header';
import Home from './comp/Home';

class Index extends React.Component {

  render() {
    return (
      <div className="App">
        <Header></Header>
        <div id="main">
            <Home></Home>
        </div>
      </div>
    );
  }
}

const container = document.getElementById('root');
const root = createRoot(container);
root.render(<Index />);

