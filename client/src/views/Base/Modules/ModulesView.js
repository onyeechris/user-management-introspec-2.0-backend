import React, { Component } from 'react';
import {
  FormGroup, Label, Card, CardBody, CardHeader, Col, Row,
  Table, Button, Alert,
  Modal, ModalHeader, ModalBody,
  ModalFooter,
  // Input, 
  Form, Fade
} from 'reactstrap';
import Widget04 from '../../Widgets/Widget04';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchModule } from '../../../actions/action_module';
import { fetchGroup, fetchGroups, updateGroup } from '../../../actions/action_group';
import { fetchPermissions } from '../../../actions/action_permission';
import { Link } from "react-router-dom";
import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';
import {
  fetchStaff, updateStaff, fetchStaffs, fetchAppUsers, addUserToApp, fetchUserApp, removeFromApp
  // fetchStaff, deleteStaff, createStaff, updateStaff 
} from '../../../actions/action_staff';
import { CopyToClipboard } from 'react-copy-to-clipboard';

import DropdownTreeSelect from 'react-dropdown-tree-select';
import 'react-dropdown-tree-select/dist/styles.css';
import '../../Dashboard/css/dashboard.css';

// import ComboSelect from 'react-combo-select';
// let standardArray = ["JA007D", "JA008D", "JA009D", "JA010D"];

let selectedUsers = [];
let selectedGroups = [];
class ModulesView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      appName: this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.name : "",
      moduleData: [],
      sessionModuleData: JSON.parse(sessionStorage.getItem("moduleData")),
      initialPermissionData: [],
      itemsPerPage: 10,
      activePage: 1,
      myOtherModuleObject: {},
      selected: [],
      newlySelected: [],
      singleModuleData: {},
      singlePermissionData: {},
      addedModule: {},
      loadedStaffsData: [],
      newCreatedModule: {},
      showAction: {
        "display": "none"
      },
      hideField: {
        "display": "none"
      },
      permissionsDeleteList: [],
      singleViewModuleData: this.props.moduleData.moduleSaved ? this.props.moduleData.moduleSaved : "",
      groupData: [],
      permissionData: [],
      appStaffData: [],
      staffTableData: {},
      newAddedStaff: {},
      visible: false,
      staffList: [],
      formError: "",
      addError: "",
      modal: false,
      deleteModal: false,
      groupModal: false,
      greyedOut: false,
      singleStaffData: {},
      staffArray: [],
      groupArray: [],
      fadeIn: false,
      timeout: 300,
      copied: "",
    };
    this.changePageItem = this.changePageItem.bind(this);
    this.toggle = this.toggle.bind(this);
    // this.addUserToApp = this.addUserToApp.bind(this);
    this.removeFromApp = this.removeFromApp.bind(this);
    this.findStaffRemove = this.findStaffRemove.bind(this);
    this.findStaffView = this.findStaffView.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.toggleUserGroup = this.toggleUserGroup.bind(this);
    this.toggleFade = this.toggleFade.bind(this);
    this.onUserSelect = this.onUserSelect.bind(this);
    this.copyText = this.copyText.bind(this);
    this.addGroupsToUserAndToApp = this.addGroupsToUserAndToApp.bind(this);
  }

  componentDidMount = () => {

    console.log(this.props);
    console.log(this.props.moduleData);

    this.loadData();

    // Fetch All Staffs to select from
    this.props.fetchStaffs('?size=1000').then(result => {
      this.setState({ staffArray: [] });
      let staffObj = {};
      console.log(result);
      this.setState({ staffList: result.data.payload });

      // logic to populate the add users select widget
      result.data.payload.forEach(staff => {
        let currentStaffArray = this.state.staffArray;
        staffObj = {};
        staffObj.label = staff.first_name + " - " + staff.email;
        staffObj.value = staff.id;
        currentStaffArray.push(staffObj);
        this.setState({ staffArray: currentStaffArray }, console.log(this.state.staffArray));
      })
    }, error => {
      console.log(error);
    })


    // Fetch Staffs for current app module
    let type = '?size=' + this.state.itemsPerPage;
    this.props.fetchAppUsers(type).then(result => {
      console.log(result);
      this.setState({ appStaffData: result.data.payload });
      this.setState({ staffTableData: result.data.meta });
      this.setState({ itemsPerPage: result.data.meta.size });
    }, error => {
      console.log(error);
      // this.setState({ currentError: error });
    })

    // Fetch Groups for current app module
    this.props.fetchGroups('?size=1000').then(result => {
      console.log(result);
      this.setState({ groupArray: [] });
      let groupObj = {};
      let groupArray = [];
      this.setState({ groupData: result.data.payload });
      groupArray = result.data.payload;
      groupArray.forEach(group => {
        let currentGroupArray = this.state.groupArray;
        groupObj = {};
        groupObj.label = group.name;
        // + " - " + group.description;
        groupObj.value = group.id;
        currentGroupArray.push(groupObj);
        this.setState({ groupArray: currentGroupArray }, console.log(this.state.groupArray));
      });
    }, error => {
      console.log(error);
      // this.setState({ currentError: error });
    })

    // Fetch Permissions for current app module
    this.props.fetchPermissions('?size=1000').then(result => {
      console.log(result);
      this.setState({ permissionData: result.data.payload });
    }, error => {
      console.log(error);
      // this.setState({ currentError: error });
    })

    let loggedInUserRole = sessionStorage.getItem("userRole");
    if (loggedInUserRole.toLowerCase().includes("admin")) {
      let makeVisible = {
        "display": "inline-block"
      }
      this.setState({ showAction: makeVisible });
    }
  }


  toggle() {
    this.setState({ formError: "" });
    this.setState({ addError: "" });
    console.log("Game!");
    this.setState({
      modal: !this.state.modal,
    });
    // this.setState({ greyedOut: true });
    // greyedOut = true;
  }

  toggleConfirm() {
    this.setState({ addError: "" });
    this.setState({ formError: "" });
    this.setState({
      deleteModal: !this.state.deleteModal,
    });
  }

  toggleUserGroup() {
    this.setState({ addError: "" });
    this.setState({ formError: "" });
    this.setState({
      groupModal: !this.state.groupModal,
    });
  }

  toggleFade() {
    this.setState({ fadeIn: !this.state.fadeIn });
  }

  fetchStaffs() {
    let type = '?size=' + this.state.itemsPerPage;
    this.props.fetchAppUsers(type).then(result => {
      console.log(result);
      this.setState({ appStaffData: result.data.payload });
      this.setState({ staffTableData: result.data.meta });
      this.setState({ itemsPerPage: result.data.meta.size });
    }, error => {
      console.log(error);
      this.setState({ currentError: error });
    })
  }

  handlePageChange = (pageNumber) => {
    let pageNumberParam = pageNumber - 1;
    this.fetchStaffsPage(pageNumberParam);
    this.setState({ activePage: pageNumber });
  }

  // Select number of items to display on table

  changePageItem(numberOfItems) {
    //console.log("Your items per page: " + numberOfItems.target.value);
    const updateStateVariable = () => {
      this.setState({ itemsPerPage: numberOfItems.target.value });
      return true;
    }

    //using an asynchronous function
    const reloadTable = async () => {
      try {
        const response = await updateStateVariable();
        if (response) {
          this.fetchStaffs();
        }
      }
      catch (error) {
        //console.log(error);
      }
    }
    reloadTable();
  }




  // Function to add multiple groups to a staff and then add the staff to app
  addGroupsToUserAndToApp = () => {
    if (selectedUsers.length > 0) {
      let currentUser = {};
      this.props.fetchStaff(selectedUsers[0].value).then(result => {
        currentUser = result.data;
        console.log(currentUser);

        // logic to check if the user already exists
        let userIds = [];
        this.state.staffList.forEach(staff => {
          userIds.push(staff.id);
          userIds.indexOf(currentUser.id)
        })

        // if (userIds.indexOf(currentUser.id) < 0) {

        const addGroupsToUser = () => {
          const populateGroups = () => {
            selectedGroups.forEach(group => {
              let groupObj = {};
              this.props.fetchGroup(group.value).then(result => {
                console.log(result.data);
                groupObj = result.data;

                groupObj.staffs.push({ id: currentUser.id });
                currentUser.groups.push({ id: groupObj.id });
                console.log(currentUser);

                this.props.updateGroup(groupObj, 1).then(result => {
                  console.log(result);
                }, error => {
                  console.log(error);
                  this.setState({ addError: error });
                })
              }, error => {
                console.log(error);
              })
            });
            return true;
          }

          const addGroups = async () => {
            try {
              const response = await populateGroups();
              if (response) {
                this.props.updateStaff(currentUser);
              }
            }
            catch (error) {
              console.log(error);
            }
          }
          addGroups();
          return true;
        }

        const addUserToApp = (appUser) => {
          console.log(appUser);
          let staffToAdd = {};
          let currentDate = new Date();
          console.log(currentDate);
          let formatted_date = currentDate.getFullYear() + "-" + (("0" + (currentDate.getMonth() + 1)).slice(-2)) + "-" + ("0" + currentDate.getDate()).slice(-2)
            + " " + (("0" + currentDate.getHours()).slice(-2)) + ":" + (("0" + currentDate.getMinutes()).slice(-2)) + ":" + (("0" + currentDate.getSeconds()).slice(-2));
          console.log(formatted_date);
          staffToAdd.assign_at = formatted_date;
          staffToAdd.staff = appUser;
          // staffToAdd.grade = 2; // hard coded (To change)
          staffToAdd.module = sessionStorage.getItem("userModule");
          console.log(staffToAdd);
          this.props.addUserToApp(staffToAdd).then(result => {
            this.fetchStaffs();
            this.fetchStaffs();
            this.toggle();
            // this.setState({ greyedOut: true });
            // greyedOut = true;
            this.setState({ newAddedStaff: appUser }, this.setState({ visible: true }));
            console.log(result);
          }, error => {
            this.setState({ addError: error });
            console.log(error);
          })
          return true;
        }

        const addUser = async () => {
          try {
            const response = await addGroupsToUser();
            if (response) {
              addUserToApp(currentUser);
            }
          }
          catch (error) {
            console.log(error);
          }
        }
        addUser();

        // } else {
        //   this.setState({ addError: "User " + currentUser.first_name + " has already been authorized." });
        // }

      }, error => {
        console.log(error);
        this.setState({ addError: error });
      });
    }

    else {
      this.setState({ addError: "Please select a user." }, console.log(this.state.addError));
    }
  }

  // fetch app user to remove
  findStaffRemove(userId) {
    this.props.fetchUserApp(userId).then(result => {
      console.log(result);
      this.setState({ singleStaffData: result.data }, this.toggleConfirm());
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    })
  }

  removeFromApp = (staffId) => {
    this.props.removeFromApp(staffId).then(result => {
      this.fetchStaffs();
      this.fetchStaffs();
      this.toggleConfirm();
      console.log(result);
    }, error => {
      console.log("Could not remove staff: " + error);
      this.setState({ addError: error });
    });
  }

  // fetch staff to view group
  findStaffView(staffId) {
    this.props.fetchStaff(staffId).then(result => {
      console.log(result);
      let staffData = result.data;
      let initStaffDataGroups = staffData.groups;
      let finalStaffDataGroups = initStaffDataGroups.filter(group => group.mod === this.state.sessionModuleData.code);
      staffData.groups = finalStaffDataGroups;
      this.setState({ singleStaffData: staffData }, this.toggleUserGroup());
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    })
  }

  loadData() {
    if (this.state.singleViewModuleData.data) {
      this.setState({ singleModuleData: this.state.singleViewModuleData.data });
      // this.setState({ permissionData: permData });
    }
  }

  // fakeFunction(value, text) {
  //   console.log(value, text);
  // }


  copyText = () => {
    this.setState({ copied: "Key Copied" });
    console.log("copied!");
  }


  onUserSelect = (currentNode, selectedNodes) => {
    console.log('onChange::', currentNode);
    console.log('selected nodes::', selectedNodes);
    selectedUsers = selectedNodes;
    // greyedOut = false;
    console.log(selectedUsers);
    console.log(selectedNodes[0].value);
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    let appUsers = this.state.appStaffData;
    console.log(appUsers);
    // if (selectedNodes[0].value) {
    //   appUsers.forEach(appUser => {
    //     if (selectedNodes[0].value === appUser.staff.id) {
    //       this.setState({ addError: "User " + appUser.staff.first_name + " has already been authorized." },
    //         this.setState({ greyedOut: true }));
    //     } else {
    //       this.setState({ addError: "" },
    //         this.setState({ greyedOut: false }));
    //     }
    //   });
    // }
  }

  onGroupSelect = (currentNode, selectedNodes) => {
    console.log('onChange::', currentNode);
    console.log('selected nodes::', selectedNodes);
    selectedGroups = selectedNodes;
    let groupSelectedBool = true;
    console.log(groupSelectedBool);
    console.log(selectedGroups);
  }


  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }



  render() {

    let appStaff = this.state.appStaffData;
    // let allStaff = this.state.staffList;
    let staffArray = this.state.staffArray;
    let groupArray = this.state.groupArray;

    let { showAction } = this.state;

    return (
      <div className="animated fadeIn">
        {/* <Link to='/apps'> */}
        <Button onClick={this.props.history.goBack}>
          <i className="fa fa-arrow-left"></i> {' '}
          {this.translate("Back")}
        </Button>
        {/* </Link> */}
        <br />
        <br />

        <Row>
          <Col md="8">
            <Card>
              <CardHeader>
                <p>{this.translate("App Details")}</p>
                <p style={{ color: 'red' }}>{this.state.currentError}</p>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("User")} <strong>{this.state.newAddedStaff.first_name}</strong> {' '}{this.translate("has been added successfully")}.
                </Alert>
              </CardHeader>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      {this.translate("Name")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.name : this.state.sessionModuleData.name}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      {this.translate("Id")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.id : this.state.sessionModuleData.id}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      {this.translate("Description")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.description : this.state.sessionModuleData.description}
                  </Col>
                </FormGroup>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="description"><strong>
                      {this.translate("App Key")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    <Button onClick={this.toggleFade}>{this.state.fadeIn ? this.translate("Hide Key") : this.translate("Show Key")} </Button>
                    <Fade
                      timeout={this.state.timeout} in={this.state.fadeIn}
                      tag="h5" className="mt-3"
                    >
                      {this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.key : this.state.sessionModuleData.key}
                      &nbsp; {' '}
                      <CopyToClipboard text={this.props.moduleData.moduleFetched ? this.props.moduleData.moduleFetched.data.description : this.state.sessionModuleData.description}>
                        <Button color="default" onClick={this.copyText}>{this.translate("Copy")}</Button>
                      </CopyToClipboard>
                      &nbsp; <span style={{ color: "grey", fontSize: "10px" }}> {this.state.copied}</span>
                    </Fade>
                  </Col>
                </FormGroup>
              </CardBody>
            </Card>
          </Col>
          <Col md="4">
            <Col sm="12" md="12">
              <Link to='/apps/module_view/groups'>
                <Widget04 icon="icon-people" color="success" header={this.state.groupData ? `${this.state.groupData.length}` : "0"} value="10">
                  {this.translate("User Groups")}
                </Widget04>
              </Link>
            </Col>
            <Col sm="12" md="12">
              <Link to='/apps/module_view/permissions'>
                <Widget04 icon="icon-people" color="success" header={this.state.permissionData ? `${this.state.permissionData.length}` : "0"} value="10">
                  {this.translate("App Permissions")}
                </Widget04>
              </Link>
            </Col>
          </Col>
        </Row>


        <br />

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> {this.translate("Authorized Staffs")}
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>
                    <i className="fa fa-plus-square-o"></i>&nbsp;&nbsp;
                    {this.translate("Import User")}
                  </Button>
                </div>
              </CardHeader>
              <CardBody>

                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>{this.translate("ID")}</th>
                      <th>{this.translate("First Name")}</th>
                      <th>{this.translate("Email")}</th>
                      {/* <th>{this.translate("Grade")}</th> */}
                      <th>{this.translate("Date Assigned")}</th>
                      <th>{this.translate("Action")}</th>
                    </tr>
                  </thead>
                  <tbody>{appStaff.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.staff.first_name}</td>
                        <td>{item.staff.email}</td>
                        {/* <td>{item.grade}</td> */}
                        <td>{item.assign_at}</td>
                        <td>
                          <Button size="sm" color="danger" onClick={e => this.findStaffRemove(item.id)} style={showAction}><i className="fa fa-trash"></i>
                            {' '}{this.translate("Remove")}
                          </Button>{' '}
                          <Button size="sm" color="secondary" onClick={e => this.findStaffView(item.staff.id)}><i className="fa fa-sign-in"></i>
                            {' '}{this.translate("View")}
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


        {/** Add Users Modal */}

        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>{this.translate("Import Users to")}{" "} {this.state.appName}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Select User and Add Groups")}
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.addError}</p>
                <Form action="" method="post" className="form-horizontal" >
                  <p>{this.translate("Select Staffs")}</p>
                  <FormGroup row>
                    <Col md="12">
                      <DropdownTreeSelect
                        data={staffArray}
                        onChange={this.onUserSelect}
                        mode="simpleSelect"
                      />
                    </Col>
                  </FormGroup>
                  <p>{this.translate("Select Groups")}</p>
                  <FormGroup>
                    <DropdownTreeSelect
                      data={groupArray}
                      onChange={this.onGroupSelect}
                    />
                  </FormGroup>
                </Form>
                {/* <ComboSelect text="-Select me-" type="multiselect" data={standardArray} onChange={this.fakeFunction} /> */}

              </CardBody>
            </Card>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onClick={e => this.addGroupsToUserAndToApp()} disabled={this.state.greyedOut ? true : false}>
              {this.translate("Add Users To App")}</Button>{' '}
            <Button color="secondary" onClick={this.toggle}>{this.translate("Cancel")}</Button>
          </ModalFooter>
        </Modal >






        {/** Delete Users from Module */}

        <Modal isOpen={this.state.deleteModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>{this.translate("Delete from")}{" "} {this.state.appName}</ModalHeader>
          <ModalBody>
            <Card>
              <CardBody>
                <Row>
                  <Col md="12">
                    <p style={{ color: 'red' }}>{this.state.addError}</p>
                    <Form action="" method="post" className="form-horizontal" >
                      <FormGroup row>
                        <p style={{ margin: "10px" }}>
                          {this.translate("Are you sure you want to remove staff")} &nbsp;<strong> {this.state.singleStaffData.staff ? this.state.singleStaffData.staff.first_name : ""} </strong>&nbsp; {this.translate("from")}&nbsp;{this.state.appName}?
                        </p>
                      </FormGroup>
                    </Form>
                  </Col>
                </Row>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.removeFromApp(this.state.singleStaffData.id)} >{this.translate("Remove")}</Button>{' '}
                  <Button color="secondary" onClick={this.toggleConfirm}>{this.translate("Cancel")}</Button>
                </ModalFooter>

              </CardBody>
            </Card>
          </ModalBody>
        </Modal >


        {/** View User Groups Modal */}

        <Modal isOpen={this.state.groupModal} toggle={this.toggleUserGroup} className={this.props.className}>
          <ModalHeader toggle={this.toggleUserGroup}>{this.translate("View User")}
            {/* {" "} {this.state.appName} */}
          </ModalHeader>
          <ModalBody>
            <Card>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      {this.translate("Name")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.state.singleStaffData.first_name} {' '} {this.state.singleStaffData.last_name}
                  </Col>
                </FormGroup>

                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="email"><strong>
                      {this.translate("Email")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.state.singleStaffData.email}
                  </Col>
                </FormGroup>

                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="email"><strong>
                      {this.translate("Role")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.state.singleStaffData.user_type}
                  </Col>
                </FormGroup>

                <Row>
                  <Col md="12">
                    <Table hover bordered striped responsive size="sm">
                      <thead>
                        <tr>
                          {/* <th>{this.translate("tableId" defaultMessage="ID" /></th> */}
                          <th>{this.translate("Name")}</th>
                          <th>{this.translate("Description")}</th>
                        </tr>
                      </thead>
                      <tbody>{
                        this.state.singleStaffData.groups ?

                          this.state.singleStaffData.groups.map((item, key) => {
                            return (
                              <tr key={key}>
                                {/* <td>{item.id}</td> */}
                                <td>{item.name}</td>
                                <td>{item.description}</td>
                              </tr>
                            )
                          })

                          : ""}
                      </tbody>
                    </Table>
                  </Col>
                </Row>

                <ModalFooter>
                  <Button color="secondary" onClick={this.toggleUserGroup}>{this.translate("Done")}</Button>
                </ModalFooter>

              </CardBody>
            </Card>
          </ModalBody>
        </Modal >



      </div >

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
    fetchStaffs,
    fetchGroups,
    fetchPermissions,
    fetchAppUsers,
    addUserToApp,
    fetchUserApp,
    removeFromApp,
    fetchStaff,
    updateStaff,
    updateGroup,
    fetchGroup
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(ModulesView);
// export default ModulesView;
