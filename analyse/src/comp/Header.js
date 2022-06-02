import React from 'react';
import { createRoot } from 'react-dom/client';

import Home from './Home';
import Skills from './Skills';
import TechStack from './TechStack';
import Domains from './Domains';

const menuList = ["Home", "Skills", "TechStack", "Domains"];

export default class Header extends React.Component {
  
    componentDidMount() {
        // drawHome();
    }

    handleClick = (props, e) => {
        e.preventDefault(); 
        var element;
        console.log("menu value", props.menuItem); 
        switch(props.menuItem) {
            case 'Home':
                element = <Home />;
                break;
            case 'Skills':
                element = <Skills />;
                break;
            case 'TechStack':
                element = <TechStack />;
                break;
            case 'Domains':
                element = <Domains />;
                break;
            default:
                element = <Home />;
                console.log("Default called");  
        } 
        console.log(props.menuItem); 
        const container = document.getElementById('main');
        const main = createRoot(container);
        main.render(element);
    }
  
    render() {
        return (
          <div className="menu" id="menu">
            <div className="pure-menu pure-menu-horizontal">
                {menuList.map((menuItem) => {               
                    return (<a onClick={(e) => this.handleClick({menuItem}, e)} href={menuItem} className="pure-menu-heading pure-menu-link" key={menuItem} 
                    >{menuItem}</a>)
                    }
                )}
            </div>
          </div>
        );
      }
}
