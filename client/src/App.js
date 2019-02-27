import React, { Component } from "react";
import { HashRouter, Route, Switch } from "react-router-dom";
// import { renderRoutes } from 'react-router-config';
import Loadable from "react-loadable";
import "./App.scss";

const loading = () => (
  <div className="animated fadeIn pt-3 text-center">Loading...</div>
);

// Containers
const DefaultLayout = Loadable({
  loader: () => import("./containers/DefaultLayout"),
  loading
});

// Menu;
const Menu = Loadable({
  loader: () => import("./containers/menu_container"),
  loading
});

// Pages;
const Admin = Loadable({
  loader: () => import("./views/Pages/Admin"),
  loading
});

const Login = Loadable({
  // loader: () => import("./views/Pages/Login"),
  loader: () => import("./containers/user_container"),
  loading
});

const AppLogin = Loadable({
  loader: () => import("./views/Pages/Login/LoginApp"),
  loading
});

const AppRedirect = Loadable({
  loader: () => import("./views/Pages/Login/LoginRedirect"),
  loading
});

const Register = Loadable({
  loader: () => import("./views/Pages/Register"),
  loading
});

const Page404 = Loadable({
  loader: () => import("./views/Pages/Page404"),
  loading
});

const Page500 = Loadable({
  loader: () => import("./views/Pages/Page500"),
  loading
});

class App extends Component {
  render() {
    return (
      <HashRouter>
        <Switch>
          <Route exact path="/admin" name="Admin Page" component={Admin} />
          <Route exact path="/login" name="Login Page" component={Login} />
          <Route exact path="/applogin" name="App Login Page" component={AppLogin} />
          <Route exact path="/appredirect" name="App Redirect Page" component={AppRedirect} />
          <Route exact path="/register" name="Register Page" component={Register} />
          <Route exact path="/404" name="Page 404" component={Page404} />
          <Route exact path="/500" name="Page 500" component={Page500} />
          <Route exact path="/menu" name="Main Menu" component={Menu} />
          <Route path="/" name="Home" component={DefaultLayout} />

        </Switch>
      </HashRouter>
    );
  }
}

export default App;
