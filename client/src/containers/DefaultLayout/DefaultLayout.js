import React, { Component, Suspense } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { Container } from 'reactstrap';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';


import {
  AppAside,
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  AppSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from '../../_nav';
// routes config
import routes from '../../routes';

import { setLocale } from '../../actions/action_locale';

const DefaultAside = React.lazy(() => import('./DefaultAside'));
const DefaultFooter = React.lazy(() => import('./DefaultFooter'));
const DefaultHeader = React.lazy(() => import('./DefaultHeader'));

class DefaultLayout extends Component {

  constructor(props) {
    super(props);
    this.state = {
      redirectToReferrer: false
    };
    this.signOut = this.signOut.bind(this);

  }

  loading = () => <div className="animated fadeIn pt-1 text-center">Loading...</div>

  componentDidMount() {

    if (sessionStorage.getItem("userData")) {
      console.log("User Logged In");

    } else if (sessionStorage.getItem("extUserData")) {
      console.log("External Application : User Logged In");
    } else {
      this.setState({ redirectToReferrer: true });
    }


  }

  signOut(e) {
    e.preventDefault()
    this.props.history.push('/login')
  }

  render() {
    if (/appredirect/.test(window.location.href)) {
      if (sessionStorage.getItem("extUserData")) {
        return <Redirect to={"/appredirect"} />;
      } else {
        return <Redirect to={"/applogin"} />;
      }
    }

    if (/applogin/.test(window.location.href)) {
      let url_string = window.location.href;
      let url = new URL(url_string);
      let UrlParam = url.searchParams.get("redirectUrl");
      let appLang = url.searchParams.get("lang");
      this.props.setLocale(appLang);
      sessionStorage.setItem("redirectUrl", UrlParam);
      console.log(sessionStorage.getItem("redirectUrl"));
      return <Redirect to={"/applogin"} />;
    }

    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }

    return (
      <div className="app">
        <AppHeader fixed>
          <Suspense fallback={this.loading()}>
            <DefaultHeader onLogout={e => this.signOut(e)} />
          </Suspense>
        </AppHeader>
        <div className="app-body">
          <AppSidebar fixed display="lg">
            <AppSidebarHeader />
            <AppSidebarForm />
            <Suspense>
              <AppSidebarNav navConfig={navigation} {...this.props} />
            </Suspense>
            <AppSidebarFooter />
            <AppSidebarMinimizer />
          </AppSidebar>
          <main className="main">
            <AppBreadcrumb appRoutes={routes} />
            <Container fluid>
              <Suspense fallback={this.loading()}>
                <Switch>
                  {routes.map((route, idx) => {
                    return route.component ? (
                      <Route
                        key={idx}
                        path={route.path}
                        exact={route.exact}
                        name={route.name}
                        render={props => (
                          <route.component {...props} />
                        )} />
                    ) : (null);
                  })}
                  <Redirect from="/" to="/dashboard" />
                </Switch>
              </Suspense>
            </Container>
          </main>
          <AppAside fixed>
            <Suspense fallback={this.loading()}>
              <DefaultAside />
            </Suspense>
          </AppAside>
        </div>
        <AppFooter>
          <Suspense fallback={this.loading()}>
            <DefaultFooter />
          </Suspense>
        </AppFooter>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  console.log('State is ', state);
  return {
    groupData: state.group.groupFetched ? state.group.groupFetched.data : '',
    lang: state.locale.lang
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    setLocale
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(DefaultLayout);

// export default DefaultLayout;
