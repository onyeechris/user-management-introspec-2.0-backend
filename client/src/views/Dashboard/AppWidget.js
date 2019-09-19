import React, { Component } from "react";
import Widget05 from "../Widgets/Widget05";

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  fetchUsersByApp, fetchGroupsByApp, fetchPermissionsByApp
} from '../../actions/action_module_data';


class AppWidget extends Component {
  // const AppWidget = (props) => {

  constructor(props) {
    super(props);
    this.state = {
      totalUsers: '',
      totalGroups: '',
      totalPermissions: '',
      appName: '',
      appDesc: '',
    };
  }
  componentDidMount() {
    const { moduleCode } = this.props;

    // console.log(moduleCode);
    // console.log(this.props);

    this.props.fetchUsersByApp(moduleCode).then(result => {
      this.setState({ totalUsers: result.payload })
    }, error => {
      console.log(error);
    });

    this.props.fetchGroupsByApp(moduleCode).then(result => {
      this.setState({ totalGroups: result.payload });
    }, error => {
      console.log(error);
    });

    this.props.fetchPermissionsByApp(moduleCode).then(result => {
      this.setState({ totalPermissions: result.payload });
    }, error => {
      console.log(error);
    });
  }


  render() {
    const { totalPermissions, totalGroups, totalUsers } = this.state;
    const { moduleName, moduleDescription } = this.props;
    return (
      <Widget05
        metric1={totalUsers.length}
        icon1="icon-user"
        metric2={totalGroups.length}
        icon2="icon-people"
        metric3={totalPermissions.length}
        icon3="icon-pie-chart"
        color="default"
        header={moduleName}
        value="00"
      >
        {moduleDescription}
      </Widget05>
    )
  }
}

const mapStateToProps = (state) => {
  // console.log('State is ', state)
  return {
    moduleData: state.moduleData
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchUsersByApp,
    fetchGroupsByApp,
    fetchPermissionsByApp
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(AppWidget);
