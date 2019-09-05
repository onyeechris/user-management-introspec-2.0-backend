import React from "react";
import Widget05 from "../Widgets/Widget05";

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  fetchUsersByApp, fetchGroupsByApp, fetchPermissionsByApp
} from '../../actions/action_module_data';


const AppWidget = (props) => {
  const { moduleName, moduleDescription, moduleCode } = props;
  let totalUsers = '';
  let totalGroups = '';
  let totalPermissions = '';
  // console.log(moduleCode);
  // console.log(props);
  props.fetchUsersByApp(moduleCode).then(result => {
    totalUsers = result.data.payload;
    console.log(totalUsers);
    console.log(result);
  }, error => {
    console.log(error);
  });

  props.fetchGroupsByApp(moduleCode).then(result => {
    totalGroups = result.data.payload;
  }, error => {
    console.log(error);
  });

  props.fetchPermissionsByApp(moduleCode).then(result => {
    totalPermissions = result.data.payload;
  }, error => {
    console.log(error);
  });


  return (
    <Widget05
      metric1={totalUsers.length}
      icon1="icon-user"
      metric2={totalGroups.length}
      icon2="icon-people"
      metric3={totalPermissions.length}
      icon3="icon-pie-chart"
      color="warning"
      header={moduleName}
      value="100"
    >
      {moduleDescription}
    </Widget05>
  )
}

const mapStateToProps = (state) => {
  console.log('State is ', state)
  return {
    // moduleData: state.moduleData
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
