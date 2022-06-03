import React from 'react';
import { createRoot } from 'react-dom/client';

import './style/index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';

import Header from './comp/Header';
import Home from './comp/Home';
import Skills from './comp/Skills';
import TechStack from './comp/TechStack';
import Domains from './comp/Domains';

const menus = ["Home", "Skills", "TechStack", "Domains"];

class Index extends React.Component {

    constructor(props) {
        super(props);
        this.headerClicked = this.headerClicked.bind(this);
        this.state = {show: "Home"};
    }

    headerClicked(menu, e) {
        e.preventDefault(); 
        console.log("menu :", menu);
        this.setState({show: menu});
    }

  render() {
    let mainItem;
    console.log(this.state.show);
    if (this.state.show === "Home") {      
        mainItem = <Home />;    
    } else if (this.state.show === "Skills") {      
        mainItem = <Skills />;    
    } else if (this.state.show === "TechStack") {  
        mainItem = <TechStack />;    
    } else if (this.state.show === "Domains") {  
        mainItem = <Domains />;  
    }

    return (
        <React.Fragment>
            <div className="pure-menu pure-menu-horizontal">
                {menus.map((menu) =>
                    <Header menu={menu} key={menu} onClicked={(e)=>this.headerClicked(menu,e)}></Header>
                )}
            </div>
            {mainItem}
        </React.Fragment>
    );
  }
}

const container = document.getElementById('root');
const root = createRoot(container);
root.render(<Index />);

