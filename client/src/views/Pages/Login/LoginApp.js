import React, { Component } from "react";
import "../../../custom.css";
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
//import { Redirect } from "react-router-dom";
// import axios from 'axios';
// import CryptoJS from "react-native-crypto-js";
import CryptoJS from "crypto-js";

import { connect } from 'react-redux';
import { fetchUser } from '../../../actions/action_login';
import { bindActionCreators } from 'redux';

import { FormattedMessage } from "react-intl";
import LoginForm from "./LoginForm";

class LoginApp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userLogin: {
                username: "",
                password: ""
            },
            baseUrl: 'http://localhost:9100/api/',
            redirectToReferrer: false,
            redirectToUrl: false,
            loginError: ""
        };
        this.login = this.login.bind(this);
        this.redirectUser = this.redirectUser.bind(this);
        this.onChange = this.onChange.bind(this);
    }


    login = (loginUserData) => {

        // event.preventDefault();
        this.props.actions.fetchUser("auth", loginUserData).then(result => {
            console.log(result);
            sessionStorage.setItem("extUserData", JSON.stringify(result));
            this.setState({ redirectToReferrer: true });
        }, error => {
            this.setState({ loginError: "Username or password incorrect!" })
        }
        )



        // //console.log(this.state.userLogin);
        //     this.props.actions.fetchUser("auth", this.state.userLogin).then(result => {
        //         // let responseJSON = result;
        //         // //console.log(responseJSON);
        //         // if (responseJSON.token) {
        //         //     sessionStorage.setItem("userData", JSON.stringify(responseJSON));
        //         //     this.setState({ redirectToReferrer: true });
        //         sessionStorage.setItem("extUserData", JSON.stringify(response.data));
        //             this.setState({ redirectToReferrer: true });
        //         }, error {
        //             //console.log("Login error");
        //         }
        //     });
        // }


        // event.preventDefault();
        // // //console.log(this.state.userLogin);
        // if (this.state.userLogin.username && this.state.userLogin.password) {
        //     axios.post('http://localhost:9100/auth',
        //         this.state.userLogin,
        //     )
        //         .then(response => {
        //             //console.log(response.data);
        //             // sessionStorage.setItem("userData", JSON.stringify(response.data));
        //             sessionStorage.setItem("extUserData", JSON.stringify(response.data));
        //             //console.log(sessionStorage.getItem("extUserData"));
        //             this.setState({ redirectToReferrer: true });
        //         })
        //         .catch(err => {

        //             this.setState({ loginError: "Username or password incorrect!" });
        //             // debugger;
        //         })
        // } else {
        //     this.setState({ loginError: "Please fill both fields!" });
        // }
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    updateValue(field, event) {
        var userInfo = JSON.parse(JSON.stringify(this.state.userLogin));
        userInfo[field] = event.target.value;
        this.setState({ userLogin: userInfo });
        //console.log(this.state.userLogin);
    }

    redirectUser() {
        this.setState({ redirectToUrl: true });
    }

    render() {
        const topMenu = {
            'backgroundColor': ' #2471a3 ',
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
        //     'color': 'red'
        // };

        if (this.state.redirectToReferrer) {
            // return <Redirect to={"/appredirect"} />;
            const redirectUrl = sessionStorage.getItem("redirectUrl");
            const appLang = sessionStorage.getItem("appLang");
            const myToken = JSON.parse(sessionStorage.getItem("extUserData")).token;
            //console.log(myToken);

            // Encypt Token
            let ciphertext = CryptoJS.AES.encrypt(myToken, 'introspecAppToken').toString();
            //console.log("Encrypted:" + ciphertext);

            // let ciphertext2 = "U2FsdGVkX19YHifcv0u4sdb4KL7QnqdopTE3urv4FxXqWhyfdkJ8ErczlIT4VSCensA1PYsSTxG01sRR5Ztk9fsqokfALqkXVKLtsLpB3Not83BR6S3yz8gpXGRIqrLMX0qjOxXNAJHJn43JnZsRH2mc6CJE8wu6h1OcXADUqx+xpL2Y1i65K3WeodZwxzvTEOeyc0KWWD7b+TkEUmgD4Jgc49+rfuRVaXdg11hAhbCXycx8fi+rsmdNqWSTqu2posW5l1gfKK67SAGByk/VRZ+vbn5zGPrz/fsEVKR3nwYcYps9U0gMhnQBAFb45oQYn7M5Czxy0qpKvwNXMhfqunzPUMwIrHPBiMGxERG4Lix4C070tRMhcVLv9een9acn";

            // Decrypt
            //let bytes = CryptoJS.AES.decrypt(ciphertext, 'introspecAppToken');
            //let decryptedToken = bytes.toString(CryptoJS.enc.Utf8);

            //console.log("Decrypted:" + decryptedToken); // 'my token'

            const urlParam = "?var=" + ciphertext;
            window.location.href = redirectUrl + urlParam + "&lang=" + appLang;
        }
        if (this.state.redirectToUrl) {
            // Redirect to the calling URL
            const redirectUrl = sessionStorage.getItem("redirectUrl");
            window.location.href = redirectUrl;
        }
        return (
            <div className="app flex-row align-items-center">
                <div style={topMenu}>
                    Introspec | <a href="/home" className="menuLink" onClick={this.menuHome}> <FormattedMessage id="User Sign In" defaultMessage="User Sign In" /></a>
                </div>
                <Container>
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
                                                    onChange={this.updateValue.bind(this, 'password')}
                                                />
                                            </InputGroup>

                                            <p className="text-muted" class={errorStyle} style={{ color: 'red' }}>{this.state.loginError}</p>
                                            <Row>
                                                <Col xs="12">
                                                    <Button
                                                        color="primary"
                                                        type="submit"
                                                        className="px-4"
                                                        onClick={this.login}
                                                    >
                                                        <FormattedMessage id="Login" defaultMessage="Login" />
                                                    </Button> {' '}{' '}
                                                    <Button
                                                        color="secondary"

                                                        className="px-4"
                                                        onClick={this.redirectUser}
                                                    >
                                                        <FormattedMessage id="Back" defaultMessage="Back" />
                                                    </Button>
                                                </Col>
                                            </Row>
                                        </form> */}
                                        <LoginForm onSubmit={this.login} externalApp="true" returnToApp={this.redirectUser} />
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


const mapStateToProps = state => {
    console.log(state);
    return {
        profile: state.profile
    };
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        actions: bindActionCreators({ fetchUser }, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginApp);
