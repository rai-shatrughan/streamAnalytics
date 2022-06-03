import React from 'react';

export default class Header extends React.Component {
    
    render() {
        return (
            <React.Fragment>
                <a className="pure-menu-heading pure-menu-link" 
                href={this.props.menu} 
                key={this.props.menu}
                onClick={this.props.onClicked}
                >{this.props.menu}</a>
            </React.Fragment>
        );
      }
}
