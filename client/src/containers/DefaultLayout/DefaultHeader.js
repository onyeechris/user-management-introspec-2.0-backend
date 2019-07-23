import React, { Component } from "react";
// import { Link } from "react-router-dom";
import {
  // Badge,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Nav,
  NavItem,
  NavLink
} from "reactstrap";
import PropTypes from "prop-types";

import {
  // AppAsideToggler,
  AppHeaderDropdown,
  AppNavbarBrand,
  AppSidebarToggler
} from "@coreui/react";
import logo from "../../assets/img/brand/logo.svg";
import sygnet from "../../assets/img/brand/sygnet.svg";
import { Redirect } from "react-router-dom";

import { FormattedMessage } from 'react-intl';

const propTypes = {
  children: PropTypes.node
};

const defaultProps = {};

class DefaultHeader extends Component {
  constructor(props) {
    super(props);
    this.state = {
      redirectToReferrer: false,
      loggedInUser: sessionStorage.getItem("loggedInUser"),
    };
    this.logout = this.logout.bind(this);
  }
  componentWillMount() {
    if (sessionStorage.getItem("userData")) {
      console.log("call User Feed");
    } else if (sessionStorage.getItem("extUserData")) {
      console.log("External Application Access");
    } else {
      this.setState({ redirectToReferrer: true });
    }
  }
  logout() {
    sessionStorage.setItem("userData", "");
    //sessionStorage.clear();
    this.setState({ redirectToReferrer: true });
  }
  render() {
    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }
    // eslint-disable-next-line
    const { children, ...attributes } = this.props;

    return (
      <React.Fragment>
        <AppSidebarToggler className="d-lg-none" display="md" mobile />
        <AppNavbarBrand
          full={{ src: logo, width: 130, alt: "Introspec Logo" }}
          minimized={{ src: sygnet, width: 30, height: 30, alt: "Introspec Logo" }}
        />
        <AppSidebarToggler className="d-md-down-none" display="lg" />

        <Nav className="d-md-down-none" navbar>
          <NavItem className="px-3">
            <NavLink href="/"><FormattedMessage id="app.title" defaultMessage="Introspec User Management" /></NavLink>
          </NavItem>
        </Nav>
        <Nav className="ml-auto" navbar>
          <AppHeaderDropdown direction="down">
            <DropdownToggle nav>
              <img
                src={"../../assets/img/avatars/7.jpg"}
                className="img-avatar"
                alt="admin@bootstrapmaster.com"
              />
            </DropdownToggle>
            <DropdownMenu right style={{ right: "auto" }}>
              <DropdownItem header tag="div" className="text-center">
                <strong><FormattedMessage id="UserAccount" defaultMessage="Account" />: </strong> {this.state.loggedInUser}
              </DropdownItem>
              <DropdownItem onClick={this.logout}>
                <i className="fa fa-lock" /> <FormattedMessage id="Logout" defaultMessage="Logout" />
              </DropdownItem>
            </DropdownMenu>
          </AppHeaderDropdown>
        </Nav>
        {/* <AppAsideToggler className="d-md-down-none" /> */}
        {/*<AppAsideToggler className="d-lg-none" mobile />*/}
      </React.Fragment>
    );
  }
}

DefaultHeader.propTypes = propTypes;
DefaultHeader.defaultProps = defaultProps;

export default DefaultHeader;
