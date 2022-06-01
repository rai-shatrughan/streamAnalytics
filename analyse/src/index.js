import React from 'react';
import { createRoot } from 'react-dom/client';

import drawIntro from './intro';
import drawSkills from './skills';
import drawTools from './tools';

import './index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';

class Home extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      message: "Default message"
    }
  }

  componentDidMount() {
        this.setState({message: "hello"});
        drawIntro();
        drawSkills();
        drawTools();
  }

  render() {
    return (
      <div className="App">
      <div className="pure-g g1 intro">
        {/* <span>{this.state.message}</span> */}
          <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecIntroBar"></div>
          <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecIntroName"></div>
          <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecIntroTag"></div>
          </div>

          <div className="pure-g g2 skills">
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsMS"></div>
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsOB"></div>
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsST"></div>
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsNLP"></div>
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsRP"></div>
          <div className="pure-u-1 pure-u-md-1-3 skillsItem" id="ecSkillsCV"></div>
          </div>

          <div className="pure-g g3 tools">
          <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="ecTools"></div>
          </div>

          <div className="pure-g g4 appendix">

      </div>
  </div>
    );
  }
}

const container = document.getElementById('root');
const root = createRoot(container);
root.render(<Home tab="home" />);

