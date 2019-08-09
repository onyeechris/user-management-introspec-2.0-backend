import React, { Component } from 'react';
import {
  FormGroup, Label, Card, CardBody, CardHeader, Col, Row,
  //  Form, Table 
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchModule } from '../../../actions/action_module';
import { Link } from "react-router-dom";
// import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';

import ModuleTable from "./ModuleTable";

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
    };
    this.changePageItem = this.changePageItem.bind(this);
  }

  componentDidMount = () => {

    console.log(this.props);
    console.log(this.props.moduleData);

    this.loadData();
    // if (this.state.singleViewModuleData.data) {
    //   this.setState({ singleModuleData: this.state.singleViewModuleData.data });
    //   this.setState({ initialPermissionData: this.state.singleViewModuleData.data.permissions });
    //   firstPermissionsData = this.state.singleViewModuleData.data.permissions;

    //   let permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);

    //   this.setState({ permissionData: permData });
    // }

    // let singleViewModuleData = JSON.parse(sessionStorage.getItem("currentSingleModuleData"));

    // // console.log(singleViewModuleData);


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
    if (this.state.singleViewModuleData.data) {
      this.setState({ singleModuleData: this.state.singleViewModuleData.data });
      this.setState({ initialPermissionData: this.state.singleViewModuleData.data.permissions });
      firstPermissionsData = this.state.singleViewModuleData.data.permissions;

      permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);
      console.log(permData);
      // this.setState({ permissionData: permData });
    }
  }

  render() {
    // let { singleModuleData } = this.state;

    let currentModuleData = this.state.singleViewModuleData.data ? this.state.singleViewModuleData.data : {};
    let currentModulePermissions = currentModuleData.permissions ? currentModuleData.permissions : [];
    // let singleModuleData = currentModuleData;
    initialPermissionData = currentModulePermissions;
    firstPermissionsData = currentModulePermissions;

    permData = firstPermissionsData.slice((this.state.itemsPerPage * this.state.activePage) - this.state.itemsPerPage, this.state.itemsPerPage);

    return (
      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <Link to='/modules'>
                  <i className="fa fa-arrow-left"></i> {' '}
                  <FormattedMessage id="Back" defaultMessage="Back" />
                  {/* {this.props.module.moduleFetched.data.name} */}
                </Link>
              </CardHeader>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      <FormattedMessage id="Name" defaultMessage="Name" />
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {/* {singleModuleData.name} */}
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.name : ""}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      <FormattedMessage id="Description" defaultMessage="Description" />
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {/* {singleModuleData.description} */}
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.description : ""}
                  </Col>
                </FormGroup>
                <br />
                {/* <h5>
                  <FormattedMessage id="Module Permissions" defaultMessage="Module Permissions" />: &nbsp;
                    <div style={this.state.hideField}>
                    {this.props.moduleData.moduleFetched ? initialPermissionData = this.props.moduleData.moduleFetched.data.permissions : ""}
                    {this.props.moduleData.moduleFetched ? permData = this.props.moduleData.moduleFetched.data.permissions : ""}
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

                <ModuleTable
                  hideField={this.state.hideField}
                  changePageItem={this.changePageItem}
                  handlePageChange={this.handlePageChange}
                  initialPermissionData={this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.permissions : []}
                  itemsPerPage={this.state.itemsPerPage}
                  activePage={this.state.activePage} />

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
    moduleData: state.module
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchModule
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(ModulesView);
// export default ModulesView;
