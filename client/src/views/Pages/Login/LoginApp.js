import React, { Component } from "react";
import "../../../custom.css";
import {
    Button,
    Card,
    CardBody,
    CardGroup,
    Col,
    Container,
    Input,
    InputGroup,
    InputGroupAddon,
    InputGroupText,
    Row
} from "reactstrap";
//import { Redirect } from "react-router-dom";
import axios from 'axios';
// import CryptoJS from "react-native-crypto-js";
import CryptoJS from "crypto-js";


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


    login(event) {
        // //console.log(this.state.userLogin);
        // if (this.state.userLogin.username && this.state.userLogin.password) {
        //     this.props.actions.fetchUser("auth", this.state.userLogin).then(result => {
        //         let responseJSON = result;
        //         //console.log(responseJSON);
        //         if (responseJSON.token) {
        //             sessionStorage.setItem("userData", JSON.stringify(responseJSON));
        //             this.setState({ redirectToReferrer: true });
        //         } else {
        //             //console.log("Login error");
        //         }
        //     });
        // }

        event.preventDefault();
        // //console.log(this.state.userLogin);
        if (this.state.userLogin.username && this.state.userLogin.password) {
            axios.post('http://localhost:9100/auth',
                this.state.userLogin,
            )
                .then(response => {
                    //console.log(response.data);
                    // sessionStorage.setItem("userData", JSON.stringify(response.data));
                    sessionStorage.setItem("extUserData", JSON.stringify(response.data));
                    //console.log(sessionStorage.getItem("extUserData"));
                    this.setState({ redirectToReferrer: true });
                })
                .catch(err => {

                    this.setState({ loginError: "Username or password incorrect!" });
                    // debugger;
                })
        } else {
            this.setState({ loginError: "Please fill both fields!" });
        }
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
        //console.log("Redirecting... should have gone by now");
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

        const errorStyle = {
            'color': 'red'
        };

        if (this.state.redirectToReferrer) {
            // return <Redirect to={"/appredirect"} />;
            const redirectUrl = sessionStorage.getItem("redirectUrl");
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
            //console.log(redirectUrl + urlParam);
            window.location.href = redirectUrl + urlParam;
            // return <Redirect to={redirectUrl + urlParam} />;
        }
        if (this.state.redirectToUrl) {
            // Redirect to the calling URL
            const redirectUrl = sessionStorage.getItem("redirectUrl");

            window.location.href = redirectUrl;
        }
        return (
            <div className="app flex-row align-items-center">
                <div style={topMenu}>
                    Introspec | <a href="/home" className="menuLink" onClick={this.menuHome}> User Sign In</a>
                </div>
                <Container>
                    <Row className="justify-content-center">
                        <Col md="8">
                            <CardGroup>
                                <Card className="p-4">
                                    <CardBody>
                                        <form action="" method="post">
                                            <h1>Login</h1>
                                            <p className="text-muted">Sign In to your application</p>
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
                                                        Login
                                                    </Button> {' '}{' '}
                                                    <Button
                                                        color="secondary"

                                                        className="px-4"
                                                        onClick={this.redirectUser}
                                                    >
                                                        Back
                                                    </Button>
                                                </Col>
                                                {/* <Col xs="6" className="text-right">
                                                    
                                                    <Button color="link" className="px-0">
                                                        Forgot password?
                                                    </Button>
                                                </Col> */}
                                            </Row>
                                        </form>
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

export default LoginApp;
