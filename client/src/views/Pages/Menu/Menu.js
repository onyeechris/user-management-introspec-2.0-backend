import React, { Component } from "react";
import { Redirect } from "react-router-dom";

class Menu extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentModule: this.props.currentModule,
      redirectToMainMenu: false
    };
  }

  menuHome(module, event) {
    this.setState({ currentModule: module });
  }

  render() {

    if (this.props.currentModule) {
      return <Redirect to={"/login"} />;
    }

    return (
      <div>
        <a href="#" onClick={this.menuHome.bind(this, 'Introspec')}> Introspec</a>
        <a href="#" onClick={this.menuHome.bind(this, 'Settlement')}> Main Menu</a>
      </div>
    );
  }
}

export default Menu;
