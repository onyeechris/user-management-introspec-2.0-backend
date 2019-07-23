import React, { Component } from "react";
// import { Link } from "react-router-dom";
import "../../../custom.css";
import jwtDecode from "jwt-decode";
import {
  // Button,
  Card,
  CardBody,
  CardGroup,
  Col,
  Container,
  // Input,
  // InputGroup,
  // InputGroupAddon,
  // InputGroupText,
  Row
} from "reactstrap";
//import { PostData } from "../../services/PostData";
import { Redirect } from "react-router-dom";
//import { fetchUser } from "../../../actions/action_login";
import { FormattedMessage } from "react-intl";
// import { SimpleReactValidator } from "simple-react-validator";
// import { Values } from "redux-form-website-template";
import LoginForm from "./LoginForm";

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userLogin: {
        username: "",
        password: ""
      },
      redirectToReferrer: false,
      redirectToMainMenu: false,
      loginError: ""
    };
    this.login = this.login.bind(this);
    this.onChange = this.onChange.bind(this);
    // this.validator = new SimpleReactValidator()({
    //   messages: {
    //     email: 'That is not an email.'
    //   },
    // });
  }


  login(event) {
    event.preventDefault();
    if (this.state.userLogin.username && this.state.userLogin.password) {
      this.props.actions.fetchUser("auth", this.state.userLogin).then(result => {
        sessionStorage.setItem("userData", JSON.stringify(result));
        sessionStorage.setItem("loggedInUser", this.state.userLogin.username);
        this.setState({ redirectToReferrer: true });
        //decode the token
        let userData = jwtDecode(result.token);
        sessionStorage.setItem("userRole", userData.authorities[0]);
      }, error => {
        this.setState({ loginError: "Username or password incorrect!" })
      }
      )
    } else {
      this.setState({ loginError: "Please fill both fields!" })
    }
  }

  onChange(e) {
    this.setState({ [e.target.name]: e.target.value });
  }

  updateValue(field, event) {
    var userInfo = JSON.parse(JSON.stringify(this.state.userLogin));
    userInfo[field] = event.target.value;
    this.setState({ userLogin: userInfo });
    // this.setState({ userLogin: userInfo }, e => console.log(this.state.userLogin));
    // console.log(this.state.userLogin);
  }

  menuHome() {
    this.setState({ redirectToMainMenu: true });
    this.props.currentModule = null;
  }

  loginUser = (loginUserData) => {
    console.log(loginUserData);
    this.props.actions.fetchUser("auth", loginUserData).then(result => {
      sessionStorage.setItem("userData", JSON.stringify(result));
      sessionStorage.setItem("loggedInUser", loginUserData.username);
      this.setState({ redirectToReferrer: true });
      //decode the token
      let userData = jwtDecode(result.token);
      sessionStorage.setItem("userRole", userData.authorities[0]);
    }, error => {
      this.setState({ loginError: "Username or password incorrect!" })
    }
    )
  }

  render() {
    const topMenu = {
      'backgroundColor': ' #20a8d8 ',
      'width': '100%',
      'height': '50px',
      'position': 'fixed',
      'top': '0px',
      'zIindex': '3',
      'color': 'white',
      'padding': '9px 20px 0 15px',
      'fontSize': '17px',
      'textDecoration': 'none'
    };
    // const errorStyle = {
    //   'color': 'red',
    //   'fontSize': '20px'
    // }


    if (this.state.redirectToReferrer) {
      return <Redirect to={"/dashboard"} />;
    }
    if (sessionStorage.getItem("userData")) {
      return <Redirect to={"/dashboard"} />;
    }
    if (this.state.redirectToMainMenu) {
      return <Redirect to={"/menu"} />;
    }
    return (
      <div className="app flex-row align-items-center">
        <div style={topMenu}>
          <FormattedMessage id="app.title" defaultMessage="Introspec User Management" />
        </div>
        <Container>
          <Row className="justify-content-center">
            <div md="8">
              <img
                src={"../../assets/img/introspec-logo.png"}
                alt="Introspec Logo"
                style={{ width: "270px" }}
              />
            </div>
            <br />
            <br />
            <br />
            <br />
          </Row>
          <Row className="justify-content-center">
            <Col md="8">
              <CardGroup>
                <Card className="p-4">
                  <CardBody>
                    <h1><FormattedMessage id="Login" defaultMessage="Login" /></h1>
                    <p className="text-muted"><FormattedMessage id="Sign In to your application" defaultMessage="Sign In to your application" /></p>
                    {/* <form action="" method="post">
                      <InputGroup className="mb-3">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-user" />
                          </InputGroupText>
                        </InputGroupAddon>
                        <Input
                          type="text"
                          name="username"
                          placeholder="Username"
                          autoComplete="username"
                          required
                          onChange={this.updateValue.bind(this, 'username')}
                        
                        />
                        
                      </InputGroup>
                      <InputGroup className="mb-4">
                        <InputGroupAddon addonType="prepend">
                          <InputGroupText>
                            <i className="icon-lock" />
                          </InputGroupText>
                        </InputGroupAddon>
                        <Input
                          type="password"
                          name="password"
                          placeholder="Password"
                          autoComplete="current-password"
                          required
                          onChange={this.updateValue.bind(this, 'password')}
                        />
                      </InputGroup>
                      <p className="text-muted" class={errorStyle} style={{ color: 'red' }}>{this.state.loginError}</p>
                      <Row>
                        <Col xs="6">
                          <Button
                            type="submit"
                            color="primary"

                            className="px-4"
                            onClick={this.login}
                          >
                            <FormattedMessage id="Login" defaultMessage="Login" />
                          </Button>
                        </Col>
                      </Row>
                    </form> */}

                    <LoginForm onSubmit={this.loginUser} />
                    {/* <Values form="login" /> */}
                  </CardBody>
                </Card>
                <Card
                  className="text-white bg-primary py-5 d-md-down-none"
                  style={{ width: "44%" }}
                >
                  <CardBody className="text-center">
                    <div>
                      <p>
                        <img
                          src={"../../assets/img/wallet-outline.png"}
                          alt="User Management"
                          style={{ width: "250px" }}
                        />
                      </p>
                    </div>
                  </CardBody>
                </Card>
              </CardGroup>
            </Col>
          </Row>
        </Container>
      </div >
    );
  }
}

export default Login;
