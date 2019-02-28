import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Row, Table, Form, Alert } from 'reactstrap';
import { Input, FormGroup, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import axios from 'axios';

let loggedInUserRole = "";
class ToDos extends Component {

  constructor(props) {
    super(props);
    this.state = {
      toDoData: [],
      smallGroupData: [],
      redirectToReferrer: false,
      redirectToMainMenu: false,
      staffEditModal: false,
      groupEditModal: false,
      singleStaffData: {},
      newStaffData: {},
      singleGroupData: {},
      entityData: {},
      currentRecord: {},
      baseUrl: 'http://localhost:9100/api/',
      visible: false,
      showAction: {
        "display": "none"
      },
    };
    this.toggleEditStaff = this.toggleEditStaff.bind(this);
    this.toggleEditGroup = this.toggleEditGroup.bind(this);
    this.findStaff = this.findStaff.bind(this);
    this.fetchStaff = this.fetchStaff.bind(this);
    this.fetchGroup = this.fetchGroup.bind(this);
    this.fetchToDos = this.fetchToDos.bind(this);
    this.readUpdateStaffValue = this.readUpdateStaffValue.bind(this);
    this.readUpdateGroupValue = this.readUpdateGroupValue.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
  }

  componentDidMount() {
    if (typeof this.props.toDo === 'undefined') {

      console.log(JSON.parse(sessionStorage.getItem("userData")).token);
      let baseUrl = 'http://localhost:9100/api/todos';
      axios.get(baseUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {

        console.log(response.data);
        this.setState({ toDoData: response.data.payload });
        console.log(JSON.parse(response.data.payload[0].payload));

      }).catch(err => {
        console.log("Couldn't fetch todo data, " + err);
      })


      let groupUrl = 'http://localhost:9100/api/groups';
      axios.get(groupUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {
        console.log(response.data)
        this.setState({ smallGroupData: response.data.payload });
      }).catch(err => {
        console.log(err);
      })


      loggedInUserRole = sessionStorage.getItem("userRole");
      console.log(loggedInUserRole);
      if (loggedInUserRole.toLowerCase().includes("checker")) {
        let makeVisible = {
          "display": "block"
        }
        this.setState({ showAction: makeVisible });
      }
    }
  }

  toggleEditStaff() {
    this.fetchToDos();
    this.setState({
      staffEditModal: !this.state.staffEditModal,
    });
  }

  toggleEditGroup() {
    this.fetchToDos();
    this.setState({
      groupEditModal: !this.state.groupEditModal,
    });
  }

  onDismiss() {
    this.setState({ visible: false });
  }

  readUpdateStaffValue(field, event) {
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.singleStaffData));
    newStaffInfo[field] = event.target.value;
    this.setState({ singleStaffData: newStaffInfo });
    console.log(this.state.singleStaffData);
  }

  readUpdateGroupValue(field, event) {
    var newGroupInfo = JSON.parse(JSON.stringify(this.state.singleGroupData));
    newGroupInfo[field] = event.target.value;
    this.setState({ singleGroupData: newGroupInfo });
    console.log(this.state.singleGroupData);
  }

  findStaff(staffId) {
    let apiUrl = 'staffs/' + staffId;
    console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    console.log(this.state.baseUrl + apiUrl);
    axios.get(this.state.baseUrl + apiUrl,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ singleStaffData: response.data });

      }).then(this.toggleEditStaff())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single staff: " + err);
      })
  }

  fetchStaff(fieldData) {
    var payloadData = JSON.parse(fieldData);
    this.setState({ singleStaffData: payloadData });
    this.setState({ currentRecord: payloadData });
    console.log(this.state.singleStaffData);
    this.toggleEditStaff();
  }

  updateStaff(singleStaff) {
    let currentStaffData = JSON.parse(singleStaff);
    console.log(currentStaffData);
    let apiUrl = 'staffs';
    var newStaffInfo = JSON.parse(JSON.stringify(currentStaffData));
    console.log(newStaffInfo.activated);
    this.setState({ singleStaffData: newStaffInfo });
    console.log(this.state.singleStaffData);
    console.log(newStaffInfo);
    axios.put(this.state.baseUrl + apiUrl,
      newStaffInfo,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        console.log(response);
        this.setState({ entityData: newStaffInfo });
        this.setState({ visible: true });
        this.fetchToDos();
      })
      .catch(err => {
        let res = JSON.parse(JSON.stringify(err.response.data));
        console.log("Server response status: " + res.status);
        console.log("Server response message: " + res.message);
        console.log("Could not update staff record: " + JSON.stringify(err.response.data));
        // debugger;
      })
    this.toggleEditStaff();
  }



  fetchGroup(fieldData) {
    var payloadData = JSON.parse(fieldData);
    this.setState({ singleGroupData: payloadData });
    this.setState({ currentRecord: payloadData });
    console.log(this.state.singleGroupData);
    this.toggleEditGroup();
  }

  updateGroup(singleGroup) {
    let currentGroupData = JSON.parse(singleGroup);
    console.log(currentGroupData);
    let apiUrl = 'groups';
    var newGroupInfo = JSON.parse(JSON.stringify(currentGroupData));
    console.log(newGroupInfo.activated);
    this.setState({ singleGroupData: newGroupInfo });
    console.log(this.state.singleGroupData);
    console.log(newGroupInfo);
    axios.put(this.state.baseUrl + apiUrl + "/1",
      newGroupInfo,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ entityData: newGroupInfo });
        this.setState({ visible: true });
        this.fetchToDos();
      })
      .catch(err => {
        console.log("Could not update group record " + err.status);
        console.log("Could not update group record " + err.message);
        let res = JSON.parse(JSON.stringify(err.response.data));
        console.log("Server response status: " + res.status);
        console.log("Server response message: " + res.message);
        console.log("Could not update group record: " + JSON.stringify(err.response.data));
        // debugger;
      })
    this.toggleEditGroup();
  }

  fetchToDos() {
    let baseUrl = 'http://localhost:9100/api/todos';
    axios.get(baseUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {

      console.log(response.data);
      this.setState({ toDoData: response.data.payload });
      console.log(JSON.parse(response.data.payload[0].payload));

    }).catch(err => {
      console.log("Couldn't fetch todo data, " + err);
      //debugger;
    })
  }

  render() {
    let { showAction } = this.state;
    let toDos = this.state.toDoData ? this.state.toDoData : [];
    let { singleStaffData } = this.state;
    let singleStaff = {};
    if (singleStaffData.payload) {
      let currentStaffData = JSON.parse(singleStaffData.payload);
      singleStaff.activated = true;
      singleStaff.group_id = currentStaffData.group.id;
      singleStaff.group_name = "";
      this.state.smallGroupData.forEach(group => {
        if (currentStaffData.group.id === group.id) {
          singleStaff.group_name = group.name;
        }
      })
      singleStaff.redis_key = singleStaffData.id;
      singleStaff.first_name = currentStaffData.firstName;
      singleStaff.last_name = currentStaffData.lastName;
      singleStaff.email = currentStaffData.email;
      singleStaff.maker_checker = currentStaffData.makerChecker;
      singleStaff.phone = currentStaffData.phone;
      singleStaff.password = currentStaffData.password;
      singleStaff.id = 1;
      // singleStaff.hire_date = currentStaffData.hireDate.monthValue + "/" + currentStaffData.hireDate.dayOfMonth + "/" + currentStaffData.hireDate.year;
      singleStaff.hire_date = currentStaffData.hireDate;
      console.log(currentStaffData.hireDate);
      console.log(currentStaffData.password);
      console.log(singleStaff);
    }

    let { singleGroupData } = this.state;
    let singleGroup = {};
    let showPermissions = {
      "display": "none"
    };
    singleGroup.permissions = [];
    if (singleGroupData.payload) {
      let currentGroupData = JSON.parse(singleGroupData.payload);
      singleGroup.activated = true;
      singleGroup.redis_key = singleGroupData.id;
      singleGroup.permissions = currentGroupData.permissions;
      singleGroup.name = currentGroupData.name;
      singleGroup.id = 1;
      singleGroup.description = currentGroupData.description;
      console.log(singleGroup);
    }

    if (singleGroup.permissions.length > 0) {
      showPermissions.display = "block"
    }
    return (
      <div className="animated fadeIn">

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All To Dos
              </CardHeader>
              <CardBody>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.state.currentRecord.action} <strong> {this.state.entityData.first_name ? this.state.entityData.first_name : this.state.entityData.name}</strong> successfully authorized.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Action</th>
                      <th>Payload</th>
                      <th>Created By</th>
                      <th>Time</th>
                      <th style={showAction}>Action</th>
                    </tr>
                  </thead>
                  <tbody>{toDos ? toDos.map((item, key) => {
                    var dataPayload = JSON.parse(item.payload);
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.action}</td>
                        <td>{item.action.toLowerCase().includes("staff") ? dataPayload.firstName : dataPayload.name}</td>
                        <td>{item.maker}</td>
                        <td>{item.at}</td>
                        <td style={showAction}>
                          <Button size="sm" color="primary"
                            onClick={e => item.action.toLowerCase().includes("staff") ? this.fetchStaff(JSON.stringify(item)) : this.fetchGroup(JSON.stringify(item))}>
                            <i className="fa fa-dot-circle-o"></i> Authorize
                          </Button>
                        </td>
                      </tr>
                    )
                  }) : "List is Empty"}
                  </tbody>
                </Table>
                {/* <nav>
                  <Pagination>
                    <PaginationItem><PaginationLink previous tag="button">Prev</PaginationLink></PaginationItem>
                    <PaginationItem active>
                      <PaginationLink tag="button">1</PaginationLink>
                    </PaginationItem>
                    <PaginationItem><PaginationLink tag="button">2</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink tag="button">3</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink tag="button">4</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink next tag="button">Next</PaginationLink></PaginationItem>
                  </Pagination>
                </nav> */}
              </CardBody>
            </Card>
          </Col>
        </Row>




        {/* Modal to activate Staffs */}

        <Modal isOpen={this.state.staffEditModal} toggle={this.toggleEditStaff} className={this.props.className}>
          <ModalHeader toggle={this.toggleEditStaff}>View and Authorize Staff</ModalHeader>
          <ModalBody>

            <Card>
              <CardHeader>
                <strong></strong> Staff details below
            </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  {/* <Input type="hidden" name="id" value={singleStaff.id} onChange={this.readUpdateStaffValue.bind(this, 'id')} /> */}
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="firstName">First Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
                        onChange={this.readUpdateStaffValue.bind(this, 'first_name')} value={singleStaff.first_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="lastName">Last Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
                        onChange={this.readUpdateStaffValue.bind(this, 'last_name')} value={singleStaff.last_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="email-input">Email</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
                        onChange={this.readUpdateStaffValue.bind(this, 'email')} value={singleStaff.email}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="phone">Phone</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.readUpdateStaffValue.bind(this, 'phone')} value={singleStaff.phone}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="date-hire">Hire Date</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="date-hire" name="hire_date" placeholder="Date MM/dd/yyyy"
                        onChange={this.readUpdateStaffValue.bind(this, 'hire_date')}
                        value={singleStaff.hire_date}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="group-id">Group</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="group-id" name="group_id" placeholder="Staff Group"
                        onChange={this.readUpdateStaffValue.bind(this, 'group_id')} value={singleStaff.group_name}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="maker-checker">Role</Label>
                    </Col>
                    <Col md="9">
                      <Input type="text" id="maker-checker" name="maker_checker" placeholder="Role"
                        onChange={this.readUpdateStaffValue.bind(this, 'maker_checker')} value={singleStaff.maker_checker}
                      />

                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={e => this.updateStaff(JSON.stringify(singleStaff))}>Activate</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEditStaff}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>





        {/*Modal to activate groups*/}

        <Modal isOpen={this.state.groupEditModal} toggle={this.toggleEditGroup} className={this.props.className}>
          <ModalHeader toggle={this.toggleEditGroup}>View and Authorize Group</ModalHeader>
          <ModalBody>

            <Card>
              <CardHeader>
                <strong></strong> Group details below
              </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  {/* <Input type="hidden" name="id" value={singleGroup.id} onChange={this.readUpdateGroupValue.bind(this, 'id')} /> */}
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="name" name="name" placeholder="Enter Group Name"
                        onChange={this.readUpdateGroupValue.bind(this, 'name')} value={singleGroup.name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required
                        onChange={this.readUpdateGroupValue.bind(this, 'description')} value={singleGroup.description}
                      />
                    </Col>
                  </FormGroup>


                  <div style={showPermissions}>
                    <br />
                    <h5>Permissions: </h5>

                    <Table hover bordered striped responsive size="sm">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Action</th>
                          <th>Description</th>
                        </tr>
                      </thead>
                      <tbody>{singleGroup.permissions.length > 0 ? singleGroup.permissions.map((item, key) => {
                        return (
                          <tr key={key}>
                            <td>{item.id}</td>
                            <td>{item.action}</td>
                            <td>{item.description}</td>

                          </tr>
                        )
                      }) : ""}
                      </tbody>
                    </Table>
                  </div>


                  {/* <h5>Group Permissions: </h5>
                  <br />
                  <DualListBox
                    options={loadedPermissionsData[0]}
                    selected={this.state.selected}
                    onChange={(selected) => {
                      this.setState({ selected });
                      this.setState({ newlySelected: selected });
                    }}
                  /> */}



                  <ModalFooter>
                    <Button color="primary" onClick={e => this.updateGroup(JSON.stringify(singleGroup))}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEditGroup}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>


      </div>

    );
  }
}

export default ToDos;
