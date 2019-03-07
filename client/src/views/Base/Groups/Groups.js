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
    this.dropDowntoggle = this.dropDowntoggle.bind(this);
  }

  componentDidMount() {
    if (typeof this.props.group === 'undefined') {

      console.log(JSON.parse(sessionStorage.getItem("userData")).token);
      let baseUrl = 'http://localhost:9100/api/groups';
      axios.get(baseUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {
        console.log(response.data)
        this.setState({ groupData: response.data.payload });
      }).catch(err => {
        console.log(err);
      })


      let permissionUrl = 'http://localhost:9100/api/permissions';
      axios.get(permissionUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {
        console.log(response.data)
        this.setState({ permissionData: response.data.payload });
        // loadedPermissionsData = [
        //   response.data.payload.map((item) => {
        //     console.log(JSON.stringify(item.id));
        //     return ({ value: JSON.stringify(item.id), label: item.action })
        //   })
        // ];
        loadedPermissionsData = [
          response.data.payload.map((item) => {
            return ({ value: item.action, label: item.action })
          })
        ];
      }).catch(err => {
        console.log(err);
      })


      loggedInUserRole = sessionStorage.getItem("userRole");
      console.log(loggedInUserRole);
      if (loggedInUserRole.toLowerCase().includes("maker")) {
        let makeVisible = {
          "display": "inline-block"
        }
        this.setState({ showAction: makeVisible });
      }
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
    console.log(this.state.newGroupData);
  }

  readUpdateValue(field, event) {
    var newGroupInfo = JSON.parse(JSON.stringify(this.state.singleGroupData));
    newGroupInfo[field] = event.target.value;
    this.setState({ singleGroupData: newGroupInfo });
    console.log(this.state.singleGroupData);
  }

  createGroup() {
    console.log(this.state.newGroupData);
    let { newGroupData } = this.state;
    if (newGroupData.name
      && newGroupData.description) {


      let apiUrl = 'groups';

      axios.post(this.state.baseUrl + apiUrl,
        this.state.newGroupData,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          this.setState({ addedGroup: this.state.newGroupData });
          console.log(this.state.newGroupData);
          console.log(response);
        })
        .catch(err => {
          // debugger;
        })
      this.setState({ visible: true });
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
        // console.log(response.data);
        // console.log("I fetched!");
        this.setState({ groupData: response.data.payload });
      }).catch(err => {
        console.log(err);
      })
  }



  findGroupView(groupId) {
    let apiUrl = 'groups/' + groupId;
    console.log(this.state.baseUrl + apiUrl);
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
        console.log(perm);
        this.setState({ selected: perm });
        this.setState({ permissionData: response.data.permissions });

        console.log(response.data);
        console.log(this.state.selected);

        // groupDataToUpdate = response.data;
      }).then(this.toggleView())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single group data: " + err);
      })
  }

  findGroupDelete(groupId) {
    let apiUrl = 'groups/' + groupId;
    console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    console.log(this.state.baseUrl + apiUrl);
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
        console.log("Couldn't find single group data: " + err);
      })
  }




  deleteGroup(groupId) {
    if (groupId) {
      let apiUrl = 'groups/' + groupId;
      console.log(this.state.baseUrl + apiUrl);
      axios.delete(this.state.baseUrl + apiUrl,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          console.log(response);
        }).then(
          this.toggleConfirm()
        )
        .catch(err => {
          console.log("Could not delete group record: " + err);
          // debugger;
        })

    }
  }


  // findPermissionPopulate(permissionId) {
  //   let apiUrl = 'permissions/' + permissionId;
  //   console.log(JSON.parse(sessionStorage.getItem("userData")).token);
  //   console.log(this.state.baseUrl + apiUrl);


  //   const findPermission = () => {
  //     axios.get(this.state.baseUrl + apiUrl,
  //       {
  //         headers: {
  //           'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
  //         }
  //       })
  //       .then(response => {
  //         this.setState({ singlePermissionData: response.data });
  //         // this.state.newGroupData.permissions.push(response.data);
  //         // this.state.singleGroupData.permissions.push(response.data);
  //         groupDataToUpdate.permissions.push(response.data);
  //         console.log(groupDataToUpdate);
  //         // this.setState({ singleGroupData: groupDataToUpdate }, e => { console.log(this.state.singleGroupData) });
  //         this.setState({ myOtherGroupObject: groupDataToUpdate }, e => { console.log(this.state.myOtherGroupObject); });
  //         return response.data;
  //       })
  //       .catch(err => {
  //         console.log("Couldn't find single permission data, " + err);
  //       })


  //   }

  //   const getPermission = async () => {

  //     try {
  //       const response = await findPermission()
  //       // debugger;
  //       if (response) {
  //         console.log(groupDataToUpdate);
  //       }
  //     }
  //     catch (error) {
  //       console.log(error);
  //     }
  //   }

  //   getPermission();


  //   // this.setState({ singleGroupData: groupDataToUpdate }, e => { console.log(this.state.singleGroupData) });
  // }


  findGroup(groupId) {
    let apiUrl = 'groups/' + groupId;
    // console.log(this.state.baseUrl + apiUrl);
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
        console.log(perm);
        console.log(this.state.singleGroupData);
        this.setState({ selected: perm });
        myCurrentPermissions = perm;

      }).then(this.toggleEdit())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single group data: " + err);
      })
  }

  updateGroup = (flag) => {
    console.log(this.state.singleGroupData);
    console.log(groupDataToUpdate);


    // const populateObject = () => {
    //   console.log(groupDataToUpdate.permissions);
    //   console.log(myCurrentPermissions);
    //   myCurrentPermissions.forEach(permission => {
    //     this.findPermissionPopulate(permission);
    //   });
    // }

    // debugger;

    const populateMyPermissions = () => {

      if (myCurrentPermissions) {
        groupDataToUpdate.permissions = [];
        console.log(groupDataToUpdate.permissions);
        let permissionObject = {};
        // myCurrentPermissions.forEach(permission => {
        //   this.state.permissionData.forEach(item => {
        //     permissionObject = {};
        //     if (permission === item.action) {
        //       permissionObject = {
        //         'id': item.id,
        //         'action': permission,
        //         'description': item.description
        //       };
        //       groupDataToUpdate.permissions.push(permissionObject);
        //     }
        //     console.log(groupDataToUpdate.permissions);

        //   });
        // });
        groupDataToUpdate.id = this.state.singleGroupData.id;
        groupDataToUpdate.name = this.state.singleGroupData.name;
        groupDataToUpdate.description = this.state.singleGroupData.description;

        return true;
      }

      console.log(groupDataToUpdate.permissions);






      // console.log(groupDataToUpdate.permissions);

    }
    // console.log(groupDataToUpdate);
    // console.log(this.state.singleGroupData);
    // console.log(anotherGroupObject);
    // console.log(this.state.myOtherGroupObject);


    const updateTheGroup = () => {

      if (groupDataToUpdate.name) {

        let apiUrl = 'groups/';

        console.log(this.state.baseUrl + apiUrl + flag);
        console.log(groupDataToUpdate);
        // debugger;
        axios.put(this.state.baseUrl + apiUrl + flag,
          // this.state.singleGroupData,
          groupDataToUpdate,
          {
            headers: {
              'Authorization': JSON.parse(sessionStorage.getItem("userData")).token,
              'content-type': 'application/json'
            }
          })
          .then(response => {
            console.log(response);
            console.log(groupDataToUpdate);
            this.setState({ newCreatedGroup: groupDataToUpdate });
          })
          .catch(err => {
            console.log("Could not update group record- " + err);
            // debugger;
          })
        this.setState({ visibleUpdate: true });
        this.toggleEdit();
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
        console.log(error);
      }
    }

    updateMyGroup();

  }

  onSearch(text) {
    let searchOptions = []
    // any filter function and return array of options or get array of options from API

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


  dropDowntoggle(i) {
    const newArray = this.state.dropdownOpen.map((element, index) => { return (index === i ? !element : false); });
    this.setState({
      dropdownOpen: newArray,
    });
  }

  toggleTab(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab,
      });
    }
  }

  render() {
    const groups = this.state.groupData ? this.state.groupData : {};

    console.log(myCurrentPermissions);
    // console.log(this.state.selected);

    let { singleGroupData } = this.state;
    let { showAction } = this.state;

    let permissionObject = {};
    groupDataToUpdate.permissions = [];
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
        console.log(groupDataToUpdate.permissions);

      });
    });

    // console.log("THIS IS THE CURRENT VERSION : UPDATED 10PM.");

    // if (this.state.selected) {
    //   groupDataToUpdate.permissions = [];
    //   console.log(groupDataToUpdate.permissions);
    //   let permissionObject = {};
    //   myCurrentPermissions.forEach(permission => {
    //     this.state.permissionData.forEach(item => {
    //       permissionObject = {};
    //       if (permission === item.action) {
    //         permissionObject = {
    //           'id': item.id,
    //           'action': permission,
    //           'description': item.description
    //         };
    //         groupDataToUpdate.permissions.push(permissionObject);
    //       }
    //       console.log(groupDataToUpdate.permissions);
    //     });
    //   });
    // }


    return (
      <div className="animated fadeIn">

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All Groups
              </CardHeader>
              <CardBody>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  Group <strong>{this.state.addedGroup.name}</strong> has been created and submitted for activation.
                </Alert>
                <Alert color="success" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
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
                          {/* <ButtonDropdown className="mr-1" isOpen={this.state.dropdownOpen[key]} toggle={() => { this.dropDowntoggle(key); }}>
                            <DropdownToggle caret color="secondary">
                              Permissions
                            </DropdownToggle>
                            <DropdownMenu>
                              <DropdownItem onClick={e => this.findGroupView(item.id)}>View</DropdownItem>
                              <DropdownItem onClick={e => this.findGroup(item.id)}>Add</DropdownItem>
                              <DropdownItem onClick={e => this.findGroup(item.id)}>Delete</DropdownItem>
                            </DropdownMenu>
                          </ButtonDropdown> */}
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
        <Button onClick={this.toggle} className="mr-1" style={showAction}>Create Group</Button>



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
                      <Input type="text" id="name" name="name" placeholder="Enter First Name"
                        onChange={this.updateValue.bind(this, 'name')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Last Name" required
                        onChange={this.updateValue.bind(this, 'description')}
                      />
                    </Col>
                  </FormGroup>
                  {/* <br />

                  <h5>Assign Permissions to Group: </h5>
                  <br />
                  <DualListBox
                    options={loadedPermissionsData[0]}
                    selected={this.state.selected}
                    onChange={(selected) => {
                      this.setState({ selected });
                    }}
                  /> */}
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
                    <NavItem>
                      <NavLink
                        className={classnames({ active: this.state.activeTab === '2' })}
                        onClick={() => { this.toggleTab('2'); }}
                      >
                        Delete
                </NavLink>
                    </NavItem>
                    {/* <NavItem>
                      <NavLink
                        className={classnames({ active: this.state.activeTab === '3' })}
                        onClick={() => { this.toggleTab('3'); }}
                      >
                        Messages
                </NavLink>
                    </NavItem> */}
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
                      <DualListBox canFilter
                        options={loadedPermissionsData[0]}
                        selected={this.state.selected}
                        onChange={(selected) => {
                          this.setState({ selected: selected });
                          myCurrentPermissions = selected;
                        }}
                      />
                      <ModalFooter>
                        <Button color="primary" onClick={e => this.updateGroup(0)}>Delete Permissions and Update</Button>{' '}
                        <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                      </ModalFooter>
                    </TabPane>
                    {/* <TabPane tabId="3">
                      <DualListBox
                        options={loadedPermissionsData[0]}
                        selected={this.state.selected}
                        onChange={(selected) => {
                          this.setState({ selected });
                          this.setState({ newlySelected: selected });
                        }}
                      />
                      <ModalFooter>
                        <Button color="primary" onClick={e => this.updateGroup(1)}>Submit</Button>{' '}
                        <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                      </ModalFooter>
                    </TabPane> */}
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
                      <Input type="text" id="name" name="name" placeholder="Enter Group Name" readOnly
                        onChange={this.readUpdateValue.bind(this, 'name')} value={singleGroupData.name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required readOnly
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singleGroupData.description}
                      />
                    </Col>
                  </FormGroup>
                  <br />
                  <h5>Group Permissions: {this.state.permissionData.length > 0 ? this.state.permissionData.length : "None"}</h5>

                  <Table hover bordered striped responsive size="sm">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Action</th>
                        <th>Description</th>
                      </tr>
                    </thead>
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

export default Groups;
