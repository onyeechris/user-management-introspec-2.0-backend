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
  Input,
  FormGroup,
  Label,
  Form,
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
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { setLocale } from '../../../actions/action_locale';
import { fetchAllStaffs } from '../../../actions/action_staff';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userLogin: {
        username: "",
        password: ""
      },
      redirectToReferrer: false,
      loginError: "",
      value: 'en',
      staffData: {}
    };
    // this.login = this.login.bind(this);
    this.loginUser = this.loginUser.bind(this);
    this.onChange = this.onChange.bind(this);
  }

  storedLanguage = ''

  componentWillMount() {
    this.storedLanguage = localStorage.se8lementLang;
    this.setState({ value: localStorage.se8lementLang });
  }

  change(event) {
    this.setState({
      value: event.target.value
    })
    this.props.setLocale(event.target.value);
  }

  // login(event) {
  //   event.preventDefault();
  //   if (this.state.userLogin.username && this.state.userLogin.password) {
  //     this.props.actions.fetchUser("auth", this.state.userLogin).then(result => {
  //       sessionStorage.setItem("userData", JSON.stringify(result));
  //       sessionStorage.setItem("loggedInUser", this.state.userLogin.username);
  //       this.setState({ redirectToReferrer: true });
  //       //decode the token
  //       let userData = jwtDecode(result.token);
  //       sessionStorage.setItem("userRole", userData.authorities[0]);
  //     }, error => {
  //       this.setState({ loginError: "Username or password incorrect!" })
  //     }
  //     )
  //   } else {
  //     this.setState({ loginError: "Please fill both fields!" })
  //   }
  // }

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

  loginUser = (loginUserData) => {
    console.log(loginUserData);
    this.props.actions.fetchUser("auth", loginUserData).then(result => {
      console.log(result);
      sessionStorage.setItem("userData", JSON.stringify(result));
      sessionStorage.setItem("loggedInUser", loginUserData.username);
      this.setState({ redirectToReferrer: true });
      //decode the token
      let userData = jwtDecode(result.token);
      console.log(userData);
      sessionStorage.setItem("userRole", userData.authorities[0]);

      // Get the user's role
      this.props.fetchAllStaffs("?size=1000").then(result => {
        console.log(result.data);
        result.data.payload.forEach(staff => {
          console.log(staff.first_name);
          if (staff.email === loginUserData.username) {
            console.log(staff);
            sessionStorage.setItem("loggedInUserData", JSON.stringify(staff));
          }
        })
      }, error => {
        console.log(error);
      })
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
    const appTitle = {
      'float': 'left'
    }
    const footerStyle = {
      'position': 'fixed',
      'bottom': '15px',
      'right': '15px',
      'display': 'inline-block',
    }
    const errorStyle = {
      'color': 'red'
    }

    console.log(this.props);

    if (this.state.redirectToReferrer) {
      return <Redirect to={"/dashboard"} />;
    }
    if (sessionStorage.getItem("userData")) {
      return <Redirect to={"/dashboard"} />;
    }
    return (
      <div className="app flex-row align-items-center">
        <div style={topMenu}>
          <div style={appTitle}><FormattedMessage id="app.title" defaultMessage="Introspec User Management" /></div>

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
          </Row>
          <Row className="justify-content-center"> &nbsp;
          </Row>
          <Row className="justify-content-center">
            <Col md="8">
              <CardGroup>
                <Card className="p-4">
                  <CardBody>
                    <h1><FormattedMessage id="Login" defaultMessage="Login" /></h1>
                    <p className="text-muted"><FormattedMessage id="Sign In to your application" defaultMessage="Sign In to your application" /></p>
                    <p style={errorStyle}>{this.props.profile.userFetchError}</p>
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

                    <LoginForm onSubmit={this.loginUser}
                      ClearText={<FormattedMessage id="Clear Values" defaultMessage="Clear Values" />}
                      LoginText={<FormattedMessage id="Login" defaultMessage="Login" />}
                      BackText={<FormattedMessage id="Back" defaultMessage="Back" />}
                    />
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
        <div style={footerStyle}>
          <Form inline>
            <FormGroup>
              <Label htmlFor="language">Language: &nbsp; &nbsp;</Label>
              <Input type="select" className="form-control" name="select" id="language" onChange={this.change.bind(this)} value={this.state.value}>
                <option value="en" >English</option>
                <option value="fr" >Français</option>
                <option value="pt" >Português</option>
                <option value="es" >Español</option>
              </Input>
            </FormGroup>
          </Form>
        </div>
      </div >
    );
  }
}

const mapStateToProps = (state) => {
  // console.log('State is ', state)
  return {
    lang: state.locale.lang
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators(
    {
      setLocale,
      fetchAllStaffs
    },
    dispatch
  )
}

export default connect(mapStateToProps, mapDispatchToProps)(Login);
// export default Login;
