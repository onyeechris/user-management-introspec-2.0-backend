import React, { Component } from "react";
import { Link } from "react-router-dom";
// import Widget05 from "../Widgets/Widget05";
import Widget01 from '../Widgets/Widget01';
import {
  Col,
  Row,
  // CardGroup
  Table,
  // Button,
} from "reactstrap";

import { FormattedMessage } from "react-intl";

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchTodos } from '../../actions/action_todo';
import { fetchStaffs } from '../../actions/action_staff';
import { fetchPermissions } from '../../actions/action_permission';
import { fetchGroups } from '../../actions/action_group';
import {
  fetchModules, fetchModule, saveModuleInfo
} from '../../actions/action_module';
// import {
//   fetchUsersByApp, fetchGroupsByApp, fetchPermissionsByApp
// } from '../../actions/action_module_data';

import './css/dashboard.css';
import { Redirect } from "react-router-dom";
import AppWidget from "./AppWidget";

class Dashboard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      groupData: [],
      permissionData: [],
      staffData: [],
      todoData: [],
      appData: [],
    };
    // this.getTotalUsers = this.getTotalUsers.bind(this);
    // this.getTotalGroups = this.getTotalGroups.bind(this);
    // this.getTotalPermissions = this.getTotalPermissions.bind(this);
  }


  componentWillMount() {
    if (sessionStorage.getItem("userData")) {
      // //Group Data
      // this.props.fetchGroups()
      //   .then((response) => {
      //     this.setState({ groupData: response.data.payload });
      //   }).catch(err => {
      //     console.log(err);
      //   });

      // //Permission Data
      // this.props.fetchPermissions("?size=1000")
      //   .then((response) => {
      //     this.setState({ permissionData: response.data.payload });
      //   }).catch(err => {
      //     console.log(err);
      //   })

      //ToDo Data
      // this.props.fetchTodos()
      //   .then(result => {
      //     this.setState({ todoData: result.data.payload });
      //   }, error => {
      //     console.log(error);
      //     this.setState({ currentError: error });
      //   })

      // Application Data
      this.props.fetchModules()
        .then((response) => {
          console.log(response.data.payload,'data')
          this.setState({ appData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

      //Staff Data 
      this.props.fetchStaffs("?size=10")
        .then((response) => {
          this.setState({ staffData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

    } else {
      this.setState({ redirectToReferrer: true });
    }

  }

  loading = () => (
    <div className="animated fadeIn pt-1 text-center">Loading...</div>
  );

  // Get specific module to view module Details
  findModuleView = (moduleId) => {
    this.props.fetchModule(moduleId).then(result => {
      this.props.saveModuleInfo(result);
      sessionStorage.setItem("userModule", moduleId);
      sessionStorage.setItem("moduleData", JSON.stringify(result.data));
      this.setState({ singleModuleData: result.data }, this.props.history.push('/apps/module_view'));
    }, error => {
      this.setState({ formError: error });
    });
  }

  // getTotalUsers = (moduleId) => {
  //   let totalUsers = '';
  //   this.props.fetchUsersByApp(moduleId).then(result => {
  //     totalUsers = result.data.payload;
  //   }, error => {
  //     console.log(error);
  //   })
  //   return totalUsers.length;
  // }

  // getTotalGroups = (moduleId) => {
  //   let totalGroups = '';
  //   this.props.fetchGroupsByApp(moduleId).then(result => {
  //     totalGroups = result.data.payload;
  //   }, error => {
  //     console.log(error);
  //   })
  //   return totalGroups.length;
  // }

  // getTotalPermissions = (moduleId) => {
  //   let totalPermissions = '';
  //   this.props.fetchPermissionsByApp(moduleId).then(result => {
  //     totalPermissions = result.data.payload;
  //     console.log(totalPermissions.length);
  //   }, error => {
  //     console.log(error);
  //   })
  //   return totalPermissions.length;
  // }


  render() {
    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }

    return (
      <div className="animated fadeIn">
        <Row>
          <Col md="6">
            {/* <Link to='/apps'>
              <Widget04 icon="icon-pie-chart" color="danger" header={this.state.appData ? this.state.appData.length : "0"} value="10">
                <FormattedMessage id="App Modules" defaultMessage="App Modules" />
              </Widget04>
            </Link> */}
            <Row>
              {this.state.appData.map((item, key) => {
                return (
                  <Col md="6" key={key} >
                    <Link to="#" onClick={e => this.findModuleView(item.id)}>
                      {/* <Widget05
                        metric1={this.getTotalUsers(item.code)}
                        icon1="icon-user"
                        metric2={this.getTotalGroups(item.code)}
                        icon2="icon-people"
                        metric3={this.getTotalPermissions(item.code)}
                        icon3="icon-pie-chart"
                        color="success"
                        header={item.name}
                        value="0">
                        {item.description}
                      </Widget05> */}
                      <AppWidget
                        moduleName={item.name}
                        moduleDescription={item.description}
                        moduleId={item.id}
                      />
                    </Link>
                  </Col>

                )
              })}
            </Row>
          </Col>

          <Col sm="6" md="6">
            <Link to='/staffs'>
              <Widget01 color="primary" variant="inverse" value="0" mainText="System Users" smallText="Click to view all users" header={this.state.staffData ? this.state.staffData.length + '' : "0"} />
            </Link>

            <Table hover bordered striped responsive size="sm">
              <thead>
                <tr>
                  <th><FormattedMessage id="tableId" defaultMessage="ID" /></th>
                  <th><FormattedMessage id="tableFirstName" defaultMessage="First Name" /></th>
                  <th><FormattedMessage id="tableEmail" defaultMessage="Email" /></th>
                  <th><FormattedMessage id="tableRoleName" defaultMessage="Role" /></th>
                  {/* <th><FormattedMessage id="tableAction" defaultMessage="Action" /></th> */}
                </tr>
              </thead>
              <tbody>{this.state.staffData.map((item, key) => {
                return (
                  <tr key={key}>
                    <td>{item.id}</td>
                    <td>{item.first_name}</td>
                    <td>{item.email}</td>
                    <td>{item.user_type}</td>
                    {/* <td>
                      <Button size="sm" color="secondary" onClick={e => { console.log("Clicked!") }}><i className="fa fa-note"></i>
                        {' '}<FormattedMessage id="View" defaultMessage="View" />
                      </Button>
                    </td> */}
                  </tr>
                )
              })}
              </tbody>
            </Table>
          </Col>

          {/* <Col sm="6" md="6">
            <Link to='/groups'>
              <Widget04 icon="icon-people" color="success" header={this.state.groupData ? this.state.groupData.length : "0"} value="10">
                <FormattedMessage id="Staff Groups" defaultMessage="Staff Groups" />
              </Widget04>
            </Link>
          </Col> */}
        </Row>
        <Row>

          {/* <Col sm="6" md="6">
            <Link to='/staffs'>
              <Widget04 icon="icon-user" color="success" header={this.state.staffData ? this.state.staffData.length : "0"} value="10">
                <FormattedMessage id="Authorized Staffs" defaultMessage="Authorized Staffs" />
              </Widget04>
            </Link>
          </Col> */}
          {/* <Col sm="6" md="6">
            <Link to='/todos'>
              <Widget04 icon="icon-basket" color="primary" header={this.state.todoData ? this.state.todoData.length : "0"} value="10">
                <FormattedMessage id="Pending Authorizations" defaultMessage="Pending Authorizations" />
              </Widget04>
            </Link>
          </Col> */}
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  // console.log('State is ', state)
  return {
    todo: state.todo,
    staff: state.staff,
    group: state.group,
    permission: state.permission,
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchTodos,
    fetchGroups,
    fetchPermissions,
    fetchStaffs,
    fetchModules,
    fetchModule,
    saveModuleInfo
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
// export default Dashboard;
