import React, { Component } from "react";
import { Link } from "react-router-dom";
import Widget04 from '../Widgets/Widget04';
import {
  Col,
  Row
} from "reactstrap";

import { FormattedMessage } from "react-intl";

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchTodos } from '../../actions/action_todo';
import { fetchStaffs } from '../../actions/action_staff';
import { fetchPermissions } from '../../actions/action_permission';
import { fetchGroups } from '../../actions/action_group';
import { fetchModules } from '../../actions/action_module';

import { Redirect } from "react-router-dom";

class Dashboard extends Component {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.onRadioBtnClick = this.onRadioBtnClick.bind(this);

    this.state = {
      dropdownOpen: false,
      radioSelected: 2,
      groupData: [],
      permissionData: [],
      staffData: [],
      todoData: [],
      appData: [],
    };
  }


  componentWillMount() {
    if (sessionStorage.getItem("userData")) {
      //Group Data
      this.props.fetchGroups()
        .then((response) => {
          this.setState({ groupData: response.data.payload });
        }).catch(err => {
          console.log(err);
        });

      //Permission Data
      // this.props.fetchPermissions("?size=1000")
      //   .then((response) => {
      //     this.setState({ permissionData: response.data.payload });
      //   }).catch(err => {
      //     console.log(err);
      //   })

      // Application Data
      this.props.fetchModules()
        .then((response) => {
          this.setState({ appData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

      //ToDo Data
      this.props.fetchTodos()
        .then(result => {
          this.setState({ todoData: result.data.payload });
        }, error => {
          console.log(error);
          this.setState({ currentError: error });
        }
        )

      //Staff Data
      this.props.fetchStaffs("?size=1000")
        .then((response) => {
          this.setState({ staffData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

    } else {
      this.setState({ redirectToReferrer: true });
    }

  }

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  onRadioBtnClick(radioSelected) {
    this.setState({
      radioSelected: radioSelected
    });
  }

  loading = () => (
    <div className="animated fadeIn pt-1 text-center">Loading...</div>
  );

  render() {
    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }
    return (
      <div className="animated fadeIn">
        <Row>
          <Col sm="6" md="6">
            <Link to='/apps'>
              <Widget04 icon="icon-pie-chart" color="danger" header={this.state.appData ? this.state.appData.length : "0"} value="10">
                <FormattedMessage id="App Modules" defaultMessage="App Modules" />
              </Widget04>
            </Link>
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
          <Col sm="6" md="6">
            <Link to='/staffs'>
              <Widget04 icon="icon-user" color="info" header={this.state.staffData ? this.state.staffData.length : "0"} value="10">
                <FormattedMessage id="Authorized Staffs" defaultMessage="Authorized Staffs" />
              </Widget04>
            </Link>
          </Col>
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
  console.log('State is ', state)
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
    fetchModules
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
// export default Dashboard;
