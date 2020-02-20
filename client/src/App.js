import React, { Component } from "react";
import { HashRouter, Route, Switch } from "react-router-dom";
// import { renderRoutes } from 'react-router-config';
import Loadable from "react-loadable";
import "./App.scss";

import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { setLocale } from "./actions/action_locale";

import { IntlProvider } from "react-intl";

import locale_en from "react-intl/locale-data/en";
import locale_de from "react-intl/locale-data/de";
import locale_fr from "react-intl/locale-data/fr";
import locale_pt from "react-intl/locale-data/pt";
import locale_es from "react-intl/locale-data/es";

import { addLocaleData } from "react-intl";

import messages_de from "./translations/de.json";
import messages_en from "./translations/en.json";
import messages_fr from "./translations/fr.json";
import messages_pt from "./translations/pt.json";
import messages_es from "./translations/es.json";

const messages = {
	de : messages_de,
	en : messages_en,
	fr : messages_fr,
	pt : messages_pt,
	es : messages_es,
};

addLocaleData([ ...locale_en, ...locale_de, ...locale_fr, ...locale_pt, ...locale_es ]);

const loading = () => <div className='animated fadeIn pt-3 text-center'>Loading...</div>;

// Containers
const DefaultLayout = Loadable({
	loader  : () => import("./containers/DefaultLayout"),
	loading,
});

// Menu;
const Menu = Loadable({
	loader  : () => import("./containers/menu_container"),
	loading,
});

// Pages;
const Admin = Loadable({
	loader  : () => import("./views/Pages/Admin"),
	loading,
});

const Login = Loadable({
	// loader: () => import("./views/Pages/Login"),
	loader  : () => import("./containers/user_container"),
	loading,
});

const AppLogin = Loadable({
	loader  : () => import("./views/Pages/Login/LoginApp"),
	loading,
});

const AppRedirect = Loadable({
	loader  : () => import("./views/Pages/Login/LoginRedirect"),
	loading,
});

const Register = Loadable({
	loader  : () => import("./views/Pages/Register"),
	loading,
});

const Page404 = Loadable({
	loader  : () => import("./views/Pages/Page404"),
	loading,
});

const Page500 = Loadable({
	loader  : () => import("./views/Pages/Page500"),
	loading,
});

class App extends Component {
	render () {
		const { lang } = this.props;

		if (localStorage.se8lementLang) {
			this.props.setLocale(localStorage.se8lementLang);
		}

		return (
			<IntlProvider locale={lang} messages={messages[lang]}>
				<HashRouter>
					<Switch>
						<Route exact path='/admin' name='Admin Page' component={Admin} />
						<Route exact path='/login' name='Login Page' component={Login} />
						<Route exact path='/applogin' name='App Login Page' component={AppLogin} />
						<Route exact path='/appredirect' name='App Redirect Page' component={AppRedirect} />
						<Route exact path='/register' name='Register Page' component={Register} />
						<Route exact path='/404' name='Page 404' component={Page404} />
						<Route exact path='/500' name='Page 500' component={Page500} />
						<Route exact path='/menu' name='Main Menu' component={Menu} />
						<Route path='/' name='Home' component={DefaultLayout} />
					</Switch>
				</HashRouter>
			</IntlProvider>
		);
	}
}

const mapStateToProps = (state) => {
	return {
		lang : state.locale.lang,
	};
};

const mapDispatchToProps = (dispatch) => {
	return bindActionCreators(
		{
			setLocale,
		},
		dispatch,
	);
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
// export default App;
