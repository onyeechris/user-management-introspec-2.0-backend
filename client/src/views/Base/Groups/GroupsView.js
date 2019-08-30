import React, { Component } from 'react';
import {
  FormGroup, Label, Card, CardBody, CardHeader, Col, Row,
  Button,
  //  Form, Table 
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchGroup } from '../../../actions/action_group';
// import { Link } from "react-router-dom";
// import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';

import GroupPermissionsTable from "./GroupPermissionsTable";
import GroupStaffsTable from './GroupStaffsTable';

let firstPermissionsData = [];
let initialPermissionData = [];
let permData = [];
class GroupsView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      groupData: [],
      initialPermissionData: [],
      itemsPerPage: 10,
      activePage: 1,
      permissionData: [],
      permissionTableData: {},
      myOtherGroupObject: {},
      selected: [],
      newlySelected: [],
      singleGroupData: {},
      singlePermissionData: {},
      addedGroup: {},
      loadedPermissionsData: [],
      newCreatedGroup: {},
      showAction: {
        "display": "none"
      },
      hideField: {
        "display": "none"
      },
      permissionsDeleteList: [],
      singleViewGroupData: this.props.groupData.groupSaved ? this.props.groupData.groupSaved : "",
    };
    this.changePageItem = this.changePageItem.bind(this);
  }

  componentDidMount = () => {

    console.log(this.props);
    console.log(this.props.groupData);

    this.loadData();
    // if (this.state.singleViewGroupData.data) {
    //   this.setState({ singleGroupData: this.state.singleViewGroupData.data });
    //   this.setState({ initialPermissionData: this.state.singleViewGroupData.data.permissions });
    //   firstPermissionsData = this.state.singleViewGroupData.data.permissions;

    //   let permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);

    //   this.setState({ permissionData: permData });
    // }

    // let singleViewGroupData = JSON.parse(sessionStorage.getItem("currentSingleGroupData"));

    // // console.log(singleViewGroupData);


  }

  handlePageChange = (pageNumber) => {
    // console.log(pageNumber);
    let permDataNew = [];
    const updateStateVariable = () => {
      this.setState({ activePage: pageNumber });
      firstPermissionsData = { ...initialPermissionData };
      permDataNew = firstPermissionsData.splice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);
      return true;
    }

    //using an asynchronous function
    const reloadTable = async () => {
      try {
        const response = await updateStateVariable();
        if (response) {
          this.setState({ permissionData: permDataNew });
          permData = permDataNew;
          console.log(permData);
        }
      }
      catch (error) {
        // console.log(error);
      }
    }
    reloadTable();
    // firstPermissionsData = this.state.initialPermissionData;
    // let permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);
    // this.setState({ permissionData: permData });
  }

  changePageItem(numberOfItems) {
    // console.log(numberOfItems.target.value);
    let permDataNew = [];
    const updateStateVariable = () => {
      this.setState({ itemsPerPage: numberOfItems.target.value });
      firstPermissionsData = { ...initialPermissionData };
      permDataNew = firstPermissionsData.splice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);
      return true;
    }

    //using an asynchronous function
    const reloadTable = async () => {
      try {
        const response = await updateStateVariable();
        if (response) {
          this.setState({ permissionData: permDataNew });
          permData = permDataNew;
        }
      }
      catch (error) {
        // console.log(error);
      }
    }
    reloadTable();
  }


  loadData() {
    if (this.state.singleViewGroupData.data) {
      this.setState({ singleGroupData: this.state.singleViewGroupData.data });
      this.setState({ initialPermissionData: this.state.singleViewGroupData.data.permissions });
      firstPermissionsData = this.state.singleViewGroupData.data.permissions;

      permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);
      console.log(permData);
      // this.setState({ permissionData: permData });
    }
  }

  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }

  render() {
    // let { singleGroupData } = this.state;

    let currentGroupData = this.state.singleViewGroupData.data ? this.state.singleViewGroupData.data : {};
    let currentGroupPermissions = currentGroupData.permissions ? currentGroupData.permissions : [];
    // let singleGroupData = currentGroupData;
    initialPermissionData = currentGroupPermissions;
    firstPermissionsData = currentGroupPermissions;

    permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);

    return (
      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                {/* <Link to='/apps/module_view/groups'> */}
                <Button onClick={this.props.history.goBack}>
                  <i className="fa fa-arrow-left"></i> {' '}
                  {this.translate("Back")}
                </Button>
                {/* </Link> */}
              </CardHeader>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      {this.translate("Name")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {/* {singleGroupData.name} */}
                    {this.props.groupData.groupFetched ? this.props.groupData.groupFetched.data.name : ""}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      {this.translate("Description")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {/* {singleGroupData.description} */}
                    {this.props.groupData.groupFetched ? this.props.groupData.groupFetched.data.description : ""}
                  </Col>
                </FormGroup>
                <br />
                {/* <h5>
                  {this.translate("Group Permissions")}: &nbsp;
                    <div style={this.state.hideField}>
                    {this.props.groupData.groupFetched ? initialPermissionData = this.props.groupData.groupFetched.data.permissions : ""}
                    {this.props.groupData.groupFetched ? permData = this.props.groupData.groupFetched.data.permissions : ""}
                  </div>
                  {initialPermissionData.length > 0 ? initialPermissionData.length : "None"}</h5>

                <Table hover bordered striped responsive size="sm" style={permData.length > 0 ? {} : this.state.hideField}>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Action</th>
                      <th>Description</th>
                    </tr>
                  </thead>
                  <tbody>{permData.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.action}</td>
                        <td>{item.description}</td>

                      </tr>
                    )
                  })}
                  </tbody>

                  <nav>
                    <Pagination2
                      activePage={this.state.activePage}
                      itemsCountPerPage={this.state.itemsPerPage}
                      totalItemsCount={initialPermissionData ? initialPermissionData.length : null}
                      pageRangeDisplayed={5}
                      onChange={this.handlePageChange}
                    />
                  </nav>

                  <div className="pull-left">
                    <select onChange={this.changePageItem.bind(this)} className="form-control">
                      <option value="10">No of Items: 10</option>
                      <option value="5">5</option>
                      <option value="10">10</option>
                      <option value="20">20</option>
                      <option value="50">50</option>
                    </select>
                  </div>

                </Table> */}
                <Row>
                  <Col md="6">
                    <GroupPermissionsTable
                      hideField={this.state.hideField}
                      changePageItem={this.changePageItem}
                      handlePageChange={this.handlePageChange}
                      initialPermissionData={this.props.groupData.groupFetched ? this.props.groupData.groupFetched.data.permissions : []}
                      itemsPerPage={this.state.itemsPerPage}
                      activePage={this.state.activePage}
                    />
                  </Col>
                  <Col md="6">
                    <GroupStaffsTable
                      hideField={this.state.hideField}
                      changePageItem={this.changePageItem}
                      handlePageChange={this.handlePageChange}
                      staffsData={this.props.groupData.groupFetched ? this.props.groupData.groupFetched.data.staffs : []}
                      itemsPerPage={this.state.itemsPerPage}
                      activePage={this.state.activePage}
                    />
                  </Col>
                </Row>
              </CardBody>
            </Card>
          </Col>
        </Row>




      </div>

    );
  }
}

const mapStateToProps = (state) => {
  console.log('State is ', state)
  return {
    groupData: state.group
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchGroup
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupsView);
// export default GroupsView;
