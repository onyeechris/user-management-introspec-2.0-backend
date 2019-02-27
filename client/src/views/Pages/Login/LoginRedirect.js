import React, { Component } from "react";
import "../../../custom.css";
import {
    Card,
    CardBody,
    CardGroup,
    Col,
    Container,
    Row
} from "reactstrap";
import { Redirect } from "react-router-dom";

class LoginRedirect extends Component {
    constructor(props) {
        super(props);
        this.state = {
            
            redirectToReferrer: false,
            redirectToMainMenu: false
        };
    }


    render() {
        let loggedInUser = sessionStorage.getItem("extUserData");
        let appToken;
        if (loggedInUser) {
            appToken = JSON.parse(loggedInUser).token;
        }
        else {
            return <Redirect to={"/applogin"} />;
        }


        return (
            <div className="app flex-row align-items-center">

                <Container>
                    <Row className="justify-content-center">
                        <Col md="8">
                            <CardGroup>
                                <Card className="p-4">
                                    <CardBody>
                                        <h1>Successfull!</h1>
                                        Your Token: <br /><br/><p className="text-muted">{appToken}</p>
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

export default LoginRedirect;
