import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import {
  Form, FormGroup, Input, Label, Alert,
  //  ButtonDropdown, DropdownToggle, DropdownItem, DropdownMenu
  Nav, NavItem, NavLink, TabContent, TabPane
} from 'reactstrap';
import axios from 'axios';
import DualListBox from 'react-dual-listbox';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import classnames from 'classnames';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchGroup, fetchGroups, createGroup, deleteGroup, updateGroup } from '../../../actions/action_group';
import { fetchPermissions } from '../../../actions/action_permission';
// import { MultiSelectDropdown } from 'reusable-react-components';

// const options = [
//   { value: 'one', label: 'Create User' },
//   { value: 'two', label: 'Activate User' },
//   { value: 'three', label: 'Delete User' },
//   { value: 'four', label: 'Create Processors' },
//   { value: 'five', label: 'View Processors' },
//   { value: 'six', label: 'Update Processors' },
//   { value: 'seven', label: 'Set-up Products' },
// ];
let groupDataToUpdate = {};
groupDataToUpdate.permissions = [];

let loadedPermissionsData = [];
let loggedInUserRole = "";
let myCurrentPermissions = [];
let myStaticPermissions = [];
let permissionsToDelete = [];
let showPermissions = {};
class Groups extends Component {

  constructor(props) {
    super(props);
    this.state = {
      groupData: [],
      permissionData: [],
      myOtherGroupObject: {},
      redirectToReferrer: false,
      redirectToMainMenu: false,
      modal: false,
      editModal: false,
      confirmModal: false,
      viewModal: false,
      selected: [],
      newlySelected: [],
      newGroupData: {
        id: "",
        name: "",
        description: "",
        permissions: []
      },
      permissionsData: {
        id: "",
        action: "",
        description: "",
      },
      singleGroupData: {},
      singlePermissionData: {},
      baseUrl: 'http://localhost:9100/api/',
      addedGroup: {},
      loadedPermissionsData: [],
      newCreatedGroup: {},
      active: [],
      visible: false,
      visibleUpdate: false,
      dropdownOpen: new Array(19).fill(false),
      activeTab: '1',
      formError: "",
      showAction: {
        "display": "none"
      },
      hideField: {
        "display": "none"
      },
      permissionsDeleteList: [],
    };
    this.toggle = this.toggle.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.toggleView = this.toggleView.bind(this);
    this.toggleTab = this.toggleTab.bind(this);
    this.updateValue = this.updateValue.bind(this);
    this.readUpdateValue = this.readUpdateValue.bind(this);
    this.createGroup = this.createGroup.bind(this);
    this.findGroup = this.findGroup.bind(this);
    this.findGroupView = this.findGroupView.bind(this);
    this.fetchGroups = this.fetchGroups.bind(this);
    // this.findPermissionPopulate = this.findPermissionPopulate.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
    this.readUpdatePermissions = this.readUpdatePermissions.bind(this);
    // this.dropDowntoggle = this.dropDowntoggle.bind(this);
  }

  componentDidMount() {

    console.log(this.props);

    this.props.fetchGroups().then(result => {
      this.setState({ groupData: result.data.payload });
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    }
    )

    let permissionUrl = '?size=1000';
    this.props.fetchPermissions(permissionUrl).then(result => {
      this.setState({ permissionData: result.data.payload });
      loadedPermissionsData = [
        result.data.payload.map((item) => {
          return ({ value: item.action, label: item.action })
        })
      ];
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    }
    )

    loggedInUserRole = sessionStorage.getItem("userRole");
    if (loggedInUserRole.toLowerCase().includes("maker")) {
      let makeVisible = {
        "display": "inline-block"
      }
      this.setState({ showAction: makeVisible });
    }

  }

  toggle() {
    this.fetchGroups();
    this.setState({ formError: "" });
    this.setState({
      modal: !this.state.modal,
    });
  }
  toggleEdit() {
    this.fetchGroups();
    this.setState({ formError: "" });
    permissionsToDelete = [];
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    this.fetchGroups();
    this.setState({
      confirmModal: !this.state.confirmModal,
    });
  }

  toggleView() {
    this.fetchGroups();
    this.setState({
      viewModal: !this.state.viewModal,
    });
  }

  onDismiss() {
    this.setState({ visible: false });
  }
  onDismissUpdate() {
    this.setState({ visibleUpdate: false });
  }

  updateValue(field, event) {
    var newGroupInfo = JSON.parse(JSON.stringify(this.state.newGroupData));
    newGroupInfo[field] = event.target.value;
    this.setState({ newGroupData: newGroupInfo });
    //console.log(this.state.newGroupData);
  }

  readUpdateValue(field, event) {
    var newGroupInfo = JSON.parse(JSON.stringify(this.state.singleGroupData));
    newGroupInfo[field] = event.target.value;
    this.setState({ singleGroupData: newGroupInfo });
    //console.log(this.state.singleGroupData);
  }

  readUpdatePermissions(permission) {
    console.log(permission.target.value);
    permissionsToDelete.push(permission.target.value);
    console.log(permissionsToDelete);
  }

  filterPermissions = (filterText) => {
    let filterTextValue = filterText.target.value;
    console.log(filterTextValue);
    console.log(myStaticPermissions);
    let newPermissionsArray = myStaticPermissions.filter(perms => {
      return perms.toLowerCase().includes(filterTextValue.toLowerCase());
    });
    console.log(newPermissionsArray);
    this.setState({ permissionsDeleteList: newPermissionsArray });
  }

  createGroup() {
    let { newGroupData } = this.state;
    if (newGroupData.name
      && newGroupData.description) {

      this.props.createGroup(newGroupData).then(result => {
        this.setState({ addedGroup: newGroupData });
      }, error => {
        console.log(error);
        this.setState({ formError: error });
      }
      ).then(this.setState({ visible: true }))

      // let apiUrl = 'groups';

      // axios.post(this.state.baseUrl + apiUrl,
      //   newGroupData,
      //   {
      //     headers: {
      //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
      //     }
      //   })
      //   .then(response => {
      //     this.setState({ addedGroup: newGroupData });
      //     //console.log(this.state.newGroupData);
      //     //console.log(response);
      //   })
      //   .catch(err => {
      //     // debugger;
      //     console.log("Could not create record: " + err);
      //     let res = JSON.parse(JSON.stringify(err.response.data));
      //     console.log("Server response status: " + res.status);
      //     console.log("Server response message: " + res.message);

      //     let errMessage = '';
      //     switch (res.status) {
      //       case 401:
      //         errMessage = "Unauthorized, login required!";
      //         break;
      //       case 403:
      //         errMessage = "You don't have the permission to access this function!";
      //         break;
      //       case 404:
      //         errMessage = "Sorry Page Not Found!";
      //         break;
      //       case 500:
      //         errMessage = "Something went wrong, please try again.";
      //         break;
      //       default:
      //         errMessage = "Sorry there was an error";
      //     }
      //     this.setState({ formError: errMessage });
      //   })
      // this.setState({ visible: true });

      this.toggle();
    }
    else {
      this.setState({ formError: "The group must have a name and a description." });
    }
  }

  fetchGroups() {
    let apiUrl = 'groups';
    axios.get(this.state.baseUrl + apiUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        // //console.log(response.data);
        // //console.log("I fetched!");
        this.setState({ groupData: response.data.payload });
      }).catch(err => {
        //console.log(err);
      })
  }



  // Get specific group to view group Details

  findGroupView(groupId) {

    this.props.fetchGroup(groupId).then(result => {
      console.log(result);
      this.setState({ singleGroupData: result.data });
      let resPermissions = result.data.permissions;
      let perm = [];
      resPermissions.forEach(permission => {
        perm.push(permission.action);
      })
      this.setState({ selected: perm });
      this.setState({ permissionData: result.data.permissions });
      sessionStorage.setItem("currentSingleGroupData", JSON.stringify(result));
    }, error => {
      console.log(error);
    }
    ).then(this.props.history.push('/groups/group_view'))


    // let apiUrl = 'groups/' + groupId;
    // axios.get(this.state.baseUrl + apiUrl,
    //   {
    //     headers: {
    //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
    //     }
    //   })
    //   .then(response => {
    //     this.setState({ singleGroupData: response.data });
    //     let resPermissions = response.data.permissions;
    //     let perm = [];
    //     resPermissions.forEach(permission => {
    //       perm.push(permission.action);
    //     })
    //     this.setState({ selected: perm });
    //     this.setState({ permissionData: response.data.permissions });

    //   }).then(this.toggleView())
    //   .catch(err => {
    //   })
  }


  // Get specific group details for deleting

  findGroupDelete(groupId) {
    let apiUrl = 'groups/' + groupId;
    axios.get(this.state.baseUrl + apiUrl,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ singleGroupData: response.data });
      }).then(this.toggleConfirm())
      .catch(err => {
        // debugger;
      })
  }


  // Send group id to delete group

  deleteGroup(groupId) {
    if (groupId || groupId === 0) {
      this.props.deleteGroup(groupId).then(result => {
        this.toggleConfirm();
      }, error => {
        console.log(error);
        this.setState({ formError: error });
      }
      );

      // let apiUrl = 'groups/' + groupId;
      // axios.delete(this.state.baseUrl + apiUrl,
      //   {
      //     headers: {
      //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
      //     }
      //   }).then(
      //     this.toggleConfirm()
      //   )
      //   .catch(err => {
      //     // debugger;
      //     console.log("Could not delete record: " + err);
      //     let res = JSON.parse(JSON.stringify(err.response.data));
      //     console.log("Server response status: " + res.status);
      //     console.log("Server response message: " + res.message);

      //     let errMessage = '';
      //     switch (res.status) {
      //       case 401:
      //         errMessage = "Unauthorized, login required!";
      //         break;
      //       case 403:
      //         errMessage = "You don't have the permission to access this function!";
      //         break;
      //       case 404:
      //         errMessage = "Sorry Page Not Found!";
      //         break;
      //       case 500:
      //         errMessage = "Something went wrong, please try again.";
      //         break;
      //       default:
      //         errMessage = "Sorry there was an error";
      //     }
      //     this.setState({ formError: errMessage });
      //   })

    }
  }

  // Function to find specific group by ID

  findGroup(groupId) {
    let apiUrl = 'groups/' + groupId;
    axios.get(this.state.baseUrl + apiUrl,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ singleGroupData: response.data });
        let resPermissions = response.data.permissions;
        let perm = [];

        resPermissions.forEach(permission => {
          perm.push(permission.action);
        })

        this.setState({ selected: perm });
        this.setState({ permissionsDeleteList: perm });
        myCurrentPermissions = perm;
        myStaticPermissions = perm;

        showPermissions = {
          "display": "none"
        };
        if (myStaticPermissions.length > 0) {
          showPermissions.display = "block"
        }

      }).then(this.setState({ visible: false }))
      .then(this.toggleEdit())
      .catch(err => {
        // debugger;
      })
  }

  updateGroup = (flag) => {

    const populateMyPermissions = () => {

      if (myCurrentPermissions) {

        groupDataToUpdate.id = this.state.singleGroupData.id;
        groupDataToUpdate.name = this.state.singleGroupData.name;
        groupDataToUpdate.description = this.state.singleGroupData.description;

        if (flag === 1) {
          groupDataToUpdate.permissions = [];
          let permissionObject = {};

          myCurrentPermissions.forEach(permission => {
            this.state.permissionData.forEach(item => {
              permissionObject = {};
              if (permission === item.action) {
                permissionObject = {
                  'id': item.id,
                  'action': permission,
                  'description': item.description
                };
                groupDataToUpdate.permissions.push(permissionObject);
              }
            });
          });
        } else {
          groupDataToUpdate.permissions = [];
          let permissionObject = {};

          permissionsToDelete.forEach(permission => {
            this.state.permissionData.forEach(item => {
              permissionObject = {};
              if (permission === item.action) {
                permissionObject = {
                  'id': item.id,
                  'action': permission,
                  'description': item.description
                };
                groupDataToUpdate.permissions.push(permissionObject);
              }
            });
          });
          console.log("I got here");
          console.log(groupDataToUpdate.permissions);
        }

        return true;
      }
    }

    const updateTheGroup = () => {

      if (groupDataToUpdate.name) {
        this.props.updateGroup(groupDataToUpdate, flag).then(result => {
          this.setState({ newCreatedGroup: groupDataToUpdate });
          this.setState({ visibleUpdate: true });
        }, error => {
          console.log(error);
          this.setState({ formError: error });
        }
        ).then(this.toggleEdit());

      } else {
        this.setState({ formError: "The group must have a name." });
      }
    }

    const updateMyGroup = async () => {
      try {
        const response = await populateMyPermissions()
        // debugger;
        if (response) {
          updateTheGroup();
        }
      }
      catch (error) {
        //console.log(error);
      }
    }
    updateMyGroup();
  }

  onSearch(text) {
    let searchOptions = []

    const loadedPermissions = [
      this.state.permissionData.map((item) => {
        return ({ value: "'" + item.id + "'", label: item.action })
      })
    ];

    loadedPermissions.map((item) => {
      if (item.indexOf(text) >= 0) {
        searchOptions.push(item)
      }
      return true;
    })
    this.setState({
      searchOptions
    })
  }


  toggleTab(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab,
      });
    }
  }

  render() {
    let checkboxStyle = {
      "overflowY": "scroll",
      "height": "130px"
    }
    const groups = this.state.groupData ? this.state.groupData : {};

    let { singleGroupData } = this.state;
    let { showAction } = this.state;





    return (
      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All Groups
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>Create Group</Button>
                </div>
              </CardHeader>
              <CardBody>
                <Alert color="info" isOpen={this.state.visible} toggle={this.onDismiss}>
                  Group <strong>{this.state.addedGroup.name}</strong> has been created and submitted for activation.
                </Alert>
                <Alert color="info" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  Update request for group <strong>{this.state.newCreatedGroup.name}</strong> has been submitted for authorization.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Description</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>{groups.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.name}</td>
                        <td>{item.description}</td>
                        <td>
                          <Button style={showAction} size="sm" color="primary" onClick={e => this.findGroup(item.id)}><i className="fa fa-dot-circle-o"></i> Update</Button>{' '}
                          <Button style={showAction} size="sm" color="danger" onClick={e => this.findGroupDelete(item.id)}><i className="fa fa-ban"></i> Delete</Button>{' '}
                          <Button size="sm" color="secondary" onClick={e => this.findGroupView(item.id)}><i className="fa fa-note"></i> View</Button>{' '}
                        </td>
                      </tr>
                    )
                  })}
                  </tbody>
                </Table>
              </CardBody>
            </Card>
          </Col>
        </Row>




        {/* Create Group Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>Create User Group</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> Please fill the form below
              </CardHeader>
              <CardBody>
                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="name" name="name" placeholder="Enter Group Name"
                        onChange={this.updateValue.bind(this, 'name')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required
                        onChange={this.updateValue.bind(this, 'description')}
                      />
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.createGroup}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggle}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>






        {/*Modal to update groups*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}>View and Update Group</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> Group details below
              </CardHeader>
              <CardBody>
                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  {/* <Input type="hidden" name="id" value={singleGroupData.id} onChange={this.readUpdateValue.bind(this, 'id')} /> */}
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="name" name="name" placeholder="Enter Group Name"
                        onChange={this.readUpdateValue.bind(this, 'name')} value={singleGroupData.name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singleGroupData.description}
                      />
                    </Col>
                  </FormGroup>
                  <br />
                  <h5>Group Permissions: </h5>

                  <Nav tabs>
                    <NavItem>
                      <NavLink
                        className={classnames({ active: this.state.activeTab === '1' })}
                        onClick={() => { this.toggleTab('1'); }}
                      >
                        Add
                      </NavLink>
                    </NavItem>
                    <NavItem style={showPermissions}>
                      <NavLink
                        className={classnames({ active: this.state.activeTab === '2' })}
                        onClick={() => { this.toggleTab('2'); }}
                      >
                        Delete
                      </NavLink>
                    </NavItem>

                  </Nav>
                  <TabContent activeTab={this.state.activeTab}>
                    <TabPane tabId="1">
                      <DualListBox canFilter
                        options={loadedPermissionsData[0]}
                        selected={this.state.selected}
                        onChange={(selected) => {
                          this.setState({ selected: selected });
                          myCurrentPermissions = selected;
                        }}
                      />
                      <ModalFooter>
                        <Button color="primary" onClick={e => this.updateGroup(1)}>Add Permissions and Update</Button>{' '}
                        <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                      </ModalFooter>
                    </TabPane>
                    <TabPane tabId="2">
                      {/* <DualListBox canFilter
                        options={loadedPermissionsData[0]}
                        selected={this.state.selected}
                        onChange={(selected) => {
                          this.setState({ selected: selected });
                          myCurrentPermissions = selected;
                        }}
                      /> */}
                      <FormGroup row>

                        <Col md="12">
                          <Input type="text" placeholder="Filter Permissions"
                            onChange={this.filterPermissions}
                          /><br />
                          <div style={checkboxStyle}>

                            {this.state.permissionsDeleteList.map((item, key) => {
                              return (

                                <FormGroup check className="checkbox" key={key}>
                                  <Input className="form-check-input" type="checkbox" id="checkbox1" name={item} value={item}
                                    onChange={this.readUpdatePermissions} />
                                  <Label check className="form-check-label" htmlFor="checkbox1">{item}</Label>
                                </FormGroup>
                              )
                            })}
                          </div>
                        </Col>
                      </FormGroup>
                      <ModalFooter>
                        <Button color="primary" onClick={e => this.updateGroup(0)}>Delete Permissions and Update</Button>{' '}
                        <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                      </ModalFooter>
                    </TabPane>
                  </TabContent>
                </Form>
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>







        {/*Modal to view group permissions*/}

        <Modal isOpen={this.state.viewModal} toggle={this.toggleView} className={this.props.className}>
          <ModalHeader toggle={this.toggleView}>View Group Permissions</ModalHeader>
          <ModalBody>

            <Card>
              <CardHeader>
                <strong></strong> Group details below
              </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      {singleGroupData.name}
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      {singleGroupData.description}
                    </Col>
                  </FormGroup>
                  <br />
                  <h5>Group Permissions: {this.state.permissionData.length > 0 ? this.state.permissionData.length : "None"}</h5>

                  <Table hover bordered striped responsive size="sm" style={this.state.permissionData.length > 0 ? {} : this.state.hideField}>
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Action</th>
                        <th>Description</th>
                      </tr>
                    </thead>
                    <div style={checkboxStyle}>
                      <tbody>{this.state.permissionData.map((item, key) => {
                        return (
                          <tr key={key}>
                            <td>{item.id}</td>
                            <td>{item.action}</td>
                            <td>{item.description}</td>

                          </tr>
                        )
                      })}
                      </tbody>
                    </div>
                  </Table>

                  <ModalFooter>
                    <Button color="secondary" onClick={this.toggleView}>Done</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>








        {/*Modal to delete groups*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>Confirm Delete</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>

                <p>Are you sure you want to delete group: {singleGroupData.name}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deleteGroup(singleGroupData.id)}>Delete</Button>{' '}
                  <Button color="secondary" onClick={this.toggleConfirm}>Cancel</Button>
                </ModalFooter>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>

      </div>

    );
  }
}

const mapStateToProps = (state) => {
  console.log('State is ', state)
  return {
    groupData: state.group.data,
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchGroup,
    fetchGroups,
    fetchPermissions,
    createGroup,
    deleteGroup,
    updateGroup
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Groups);
// export default Groups;
