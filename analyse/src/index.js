import React from 'react';
import { createRoot } from 'react-dom/client';

import './style/index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';
import MyRoutes from './comp/MyRoutes';

const menuList = ["Home", "Skills", "TechStack", "Domains"]
class Home extends React.Component {

  render() {
    return (
      <div className="App">
        <div className="pure-menu pure-menu-horizontal">
            {menuList.map((menu) =>
                <a href={menu} className="pure-menu-heading pure-menu-link" key={menu}>{menu}</a>
            )}
        </div>
        <MyRoutes />
      </div>
    );
  }
}

const container = document.getElementById('root');
const root = createRoot(container);
root.render(<Home tab="home" />);

