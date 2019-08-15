import React, { Component } from 'react';
import {
  FormGroup, Label, Card, CardBody, CardHeader, Col, Row,
  Table, Button, Alert
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchModule } from '../../../actions/action_module';
import { Link } from "react-router-dom";
import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';
import {
  fetchStaffs,
  // fetchStaff, deleteStaff, createStaff, updateStaff 
} from '../../../actions/action_staff';

// import ModuleTable from "./ModuleTable";

let firstPermissionsData = [];
let initialPermissionData = [];
let permData = [];
class ModulesView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      moduleData: [],
      initialPermissionData: [],
      itemsPerPage: 10,
      activePage: 1,
      permissionData: [],
      permissionTableData: {},
      myOtherModuleObject: {},
      selected: [],
      newlySelected: [],
      singleModuleData: {},
      singlePermissionData: {},
      addedModule: {},
      loadedPermissionsData: [],
      newCreatedModule: {},
      showAction: {
        "display": "none"
      },
      hideField: {
        "display": "none"
      },
      permissionsDeleteList: [],
      singleViewModuleData: this.props.moduleData.moduleSaved ? this.props.moduleData.moduleSaved : "",
      staffData: [],
      staffTableData: {},
      newCreatedStaff: {},
      visible: false,
      visibleUpdate: false,
    };
    this.changePageItem = this.changePageItem.bind(this);
  }

  componentDidMount = () => {

    console.log(this.props);
    console.log(this.props.moduleData);

    this.loadData();

    // Fetch Staffs for current module
    let type = '?size=' + this.state.itemsPerPage;
    this.props.fetchStaffs(type).then(result => {
      console.log(result);
      this.setState({ staffData: result.data.payload });
      this.setState({ staffTableData: result.data.meta });
      this.setState({ itemsPerPage: result.data.meta.size });
    }, error => {
      console.log(error);
      this.setState({ currentError: error });
    }
    )
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
    if (this.state.singleViewModuleData.data) {
      this.setState({ singleModuleData: this.state.singleViewModuleData.data });
      // this.setState({ permissionData: permData });
    }
  }

  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }

  render() {

    let { groupData } = this.state;
    let { showAction } = this.state;

    const showGroup = (groupID) => {
      let itemGroupName = "";
      groupData.forEach(group => {
        if (groupID === group.id) {
          itemGroupName = group.name;
        }
      })
      return itemGroupName;
    }

    let staff = this.state.staffData;

    return (
      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <Link to='/apps'>
                  <i className="fa fa-arrow-left"></i> {' '}
                  <FormattedMessage id="Back" defaultMessage="Back" />
                </Link>
                <p style={{ color: 'red' }}>{this.state.currentError}</p>
                <Alert color="warning" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("User")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been created and submitted for activation")}.
                </Alert>
                <Alert color="warning" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  {this.translate("Update request for user")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been submitted for authorization")}.
                </Alert>
              </CardHeader>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      <FormattedMessage id="Name" defaultMessage="Name" />
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.name : ""}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      <FormattedMessage id="Code" defaultMessage="Code" />
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.code : ""}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      <FormattedMessage id="Description" defaultMessage="Description" />
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.description : ""}
                  </Col>
                </FormGroup>
                <br />
              </CardBody>
            </Card>
          </Col>
        </Row>


        <br />

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> <FormattedMessage id="AllStaffs" defaultMessage="All Staffs" />
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}><FormattedMessage id="Create New Staff" defaultMessage="Create New Staff" /></Button>
                </div>
              </CardHeader>
              <CardBody>

                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th><FormattedMessage id="tableId" defaultMessage="ID" /></th>
                      <th><FormattedMessage id="tableFirstName" defaultMessage="First Name" /></th>
                      <th><FormattedMessage id="tableEmail" defaultMessage="Email" /></th>
                      <th><FormattedMessage id="tableRoleName" defaultMessage="Role" /></th>
                      <th><FormattedMessage id="tableAction" defaultMessage="Action" /></th>
                    </tr>
                  </thead>
                  <tbody>{staff.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.first_name}</td>
                        <td>{item.email}</td>
                        <td>{item.maker_checker}</td>
                        <td>
                          <Button size="sm" color="secondary" onClick={e => { console.log("Clicked!") }}><i className="fa fa-note"></i>
                            {' '}<FormattedMessage id="View" defaultMessage="View" />
                          </Button>
                        </td>
                      </tr>
                    )
                  })}
                  </tbody>
                </Table>
                <nav>
                  <Pagination2
                    activePage={this.state.activePage}
                    itemsCountPerPage={this.state.itemsPerPage}
                    totalItemsCount={this.state.staffTableData ? this.state.staffTableData.totalElements : null}
                    pageRangeDisplayed={5}
                    onChange={this.handlePageChange}
                  />
                </nav>
                <div className="pull-left">
                  <select onChange={this.changePageItem.bind(this)} className="form-control">
                    <option value="20">No of Items</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="50">50</option>
                  </select>
                </div>
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
    moduleData: state.module,
    staffData: state.staff
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchModule,
    fetchStaffs
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(ModulesView);
// export default ModulesView;
