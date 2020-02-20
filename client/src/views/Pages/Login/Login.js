import React, { Component } from "react";
// import { Link } from "react-router-dom";
import "../../../custom.css";
import jwtDecode from "jwt-decode";
import { Card, CardBody, CardGroup, Col, Container, Input, FormGroup, Label, Form, Row } from "reactstrap";
import { Redirect } from "react-router-dom";
import { FormattedMessage } from "react-intl";
import LoginForm from "./LoginForm";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { setLocale } from "../../../actions/action_locale";
import { fetchAllStaffs } from "../../../actions/action_staff";

class Login extends Component {
	constructor (props) {
		super(props);
		this.state = {
			userLogin          : {
				username : "",
				password : "",
			},
			redirectToReferrer : false,
			loginError         : "",
			value              : "en",
			staffData          : {},
		};
		this.loginUser = this.loginUser.bind(this);
		this.onChange = this.onChange.bind(this);
	}

	storedLanguage = "";

	componentDidMount () {
		this.storedLanguage = localStorage.se8lementLang;
		this.setState({ value: localStorage.se8lementLang });
	}

	change (event) {
		this.setState({
			value : event.target.value,
		});
		this.props.setLocale(event.target.value);
	}

	onChange (e) {
		this.setState({ [e.target.name]: e.target.value });
	}

	updateValue (field, event) {
		var userInfo = JSON.parse(JSON.stringify(this.state.userLogin));
		userInfo[field] = event.target.value;
		this.setState({ userLogin: userInfo });
	}

	loginUser = (loginUserData) => {
		this.props.actions.fetchUser("auth", loginUserData).then(
			(result) => {
				if (JSON.stringify(result).includes("401")) {
					this.setState({ loginError: "Username or password incorrect!" });
				} else if (JSON.stringify(result).includes("400") || JSON.stringify(result).includes("500")) {
					this.setState({ loginError: "Username or password incorrect!" });
				} else {
					sessionStorage.setItem("userData", JSON.stringify(result));
					sessionStorage.setItem("loggedInUser", loginUserData.username);
					this.setState({ redirectToReferrer: true });

					//decode the token
					let userData = jwtDecode(result.token);
					sessionStorage.setItem("userRole", userData.authorities[0]);

					// Get the user's data to store in session storage
					this.props.fetchAllStaffs("?size=1000").then(
						(result) => {
							result.data.payload.forEach((staff) => {
								if (staff.email === loginUserData.username) {
									sessionStorage.setItem("loggedInUserData", JSON.stringify(staff));
								}
							});
						},
						(error) => {
							//console.log(error);
						},
					);
				}
			},
			(error) => {
				this.setState({ loginError: "Username or password incorrect!" });
			},
		);
	};

	render () {
		const topMenu = {
			backgroundColor : " #20a8d8 ",
			width           : "100%",
			height          : "50px",
			position        : "fixed",
			top             : "0px",
			zIindex         : "3",
			color           : "white",
			padding         : "9px 20px 0 15px",
			fontSize        : "17px",
			textDecoration  : "none",
		};
		const appTitle = {
			float : "left",
		};
		const footerStyle = {
			position : "fixed",
			bottom   : "15px",
			right    : "15px",
			display  : "inline-block",
		};
		const errorStyle = {
			color : "red",
		};

		if (this.state.redirectToReferrer) {
			return <Redirect to={"/dashboard"} />;
		}
		if (sessionStorage.getItem("userData")) {
			return <Redirect to={"/dashboard"} />;
		}
		return (
			<div className='app flex-row align-items-center'>
				<div style={topMenu}>
					<div style={appTitle}>
						<FormattedMessage id='app.title' defaultMessage='Introspec User Management' />
					</div>
				</div>
				<Container>
					<Row className='justify-content-center'>
						<div md='8'>
							<img
								src={"../../assets/img/introspec-logo.png"}
								alt='Introspec Logo'
								style={{ width: "270px" }}
							/>
						</div>
						<br />
					</Row>
					<Row className='justify-content-center'> &nbsp;</Row>
					<Row className='justify-content-center'>
						<Col md='8'>
							<CardGroup>
								<Card className='p-4'>
									<CardBody>
										<h1>
											<FormattedMessage id='Login' defaultMessage='Login' />
										</h1>
										<p className='text-muted'>
											<FormattedMessage
												id='Sign In to your application'
												defaultMessage='Sign In to your application'
											/>
										</p>
										<p style={errorStyle}>
											{this.props.profile.userFetchError ? (
												this.props.profile.userFetchError
											) : (
												this.state.loginError
											)}
										</p>

										<LoginForm
											onSubmit={this.loginUser}
											ClearText={
												<FormattedMessage id='Clear Values' defaultMessage='Clear Values' />
											}
											LoginText={<FormattedMessage id='Login' defaultMessage='Login' />}
											BackText={<FormattedMessage id='Back' defaultMessage='Back' />}
										/>
									</CardBody>
								</Card>
								<Card className='text-white bg-primary py-5 d-md-down-none' style={{ width: "44%" }}>
									<CardBody className='text-center'>
										<div>
											<p>
												<img
													src={"../../assets/img/wallet-outline.png"}
													alt='User Management'
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
							<Label htmlFor='language'>Language: &nbsp; &nbsp;</Label>
							<Input
								type='select'
								className='form-control'
								name='select'
								id='language'
								onChange={this.change.bind(this)}
								value={this.state.value}
							>
								<option value='en'>English</option>
								<option value='fr'>Français</option>
								<option value='pt'>Português</option>
								<option value='es'>Español</option>
							</Input>
						</FormGroup>
					</Form>
				</div>
			</div>
		);
	}
}

const mapStateToProps = (state) => {
	// //console.log('State is ', state)
	return {
		lang : state.locale.lang,
	};
};

const mapDispatchToProps = (dispatch) => {
	return bindActionCreators(
		{
			setLocale,
			fetchAllStaffs,
		},
		dispatch,
	);
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
// export default Login;
