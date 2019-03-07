import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Form, FormGroup, FormText, Input, Label, Alert } from 'reactstrap';
// import DatePicker from "react-datepicker";
// import Flatpickr from "react-flatpickr";
import 'flatpickr/dist/themes/material_green.css'
import "react-datepicker/dist/react-datepicker.css";
import { Redirect } from "react-router-dom";
import axios from 'axios';

let loggedInUserRole = "";
class Staffs extends Component {

  constructor(props) {
    super(props);
    this.state = {
      staffData: [],
      redirectToReferrer: false,
      redirectToMainMenu: false,
      modal: false,
      editModal: false,
      confirmModal: false,
      newStaffData: {
        id: "",
        activated: false,
        email: "",
        first_name: "",
        group_id: "",
        hire_date: "",
        last_name: "",
        maker_checker: "",
        password: "",
        phone: "",
      },
      groupData: [],
      singleStaffData: {},
      newCreatedStaff: {},
      visible: false,
      visibleUpdate: false,
      baseUrl: 'http://localhost:9100/api/',
      newDate: '',
      formError: "",
      userRole: "",
      showAction: {
        "display": "none"
      },
    };
    this.toggle = this.toggle.bind(this);
    this.updateValue = this.updateValue.bind(this);
    this.readUpdateValue = this.readUpdateValue.bind(this);
    this.createStaff = this.createStaff.bind(this);
    this.findStaff = this.findStaff.bind(this);
    this.findStaffDelete = this.findStaffDelete.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.updateStaff = this.updateStaff.bind(this);
    this.fetchStaffs = this.fetchStaffs.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
  }

  componentDidMount() {

    if (sessionStorage.getItem("userData")) {
      console.log(JSON.parse(sessionStorage.getItem("userData")).token);

      let staffUrl = 'staffs';
      console.log(this.state.baseUrl + staffUrl);
      axios.get(this.state.baseUrl + staffUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
        .then((response) => {
          console.log(response.data.payload)
          this.setState({ staffData: response.data.payload });
        }).catch(err => {
          //debugger;
          console.log("Error fetching staffs");
        })

      let groupUrl = 'groups';
      axios.get(this.state.baseUrl + groupUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
        .then((response) => {
          console.log(response.data);
          console.log(response.data.payload);
          this.setState({ groupData: response.data.payload });
        }).then(err => {
          //debugger;
        })

    } else {
      this.setState({ redirectToReferrer: true });
    }

    loggedInUserRole = sessionStorage.getItem("userRole");
    console.log(loggedInUserRole);
    if (loggedInUserRole.toLowerCase().includes("maker")) {
      let makeVisible = {
        "display": "block"
      }
      this.setState({ showAction: makeVisible });
    }
  }

  toggle() {
    this.setState({ formError: "" });
    this.setState({
      modal: !this.state.modal,
    });
  }

  toggleEdit() {
    this.setState({ formError: "" });
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    this.setState({
      confirmModal: !this.state.confirmModal,
    });
  }

  onDismiss() {
    this.setState({ visible: false });
  }
  onDismissUpdate() {
    this.setState({ visibleUpdate: false });
  }

  dateUpdate = (field) => {
    var dateField = JSON.parse(JSON.stringify(this.state.newStaffData));
    dateField.hire_date = field;
    this.setState({ newStaffData: dateField });
    console.log(this.state.newStaffData);
  }

  updateValue(field, event) {
    console.log('Field is ', field);
    console.log('Event is ', event);
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.newStaffData));
    newStaffInfo[field] = event.target.value;
    this.setState({ newStaffData: newStaffInfo });
    console.log(this.state.newStaffData);
  }

  readUpdateValue(field, event) {
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.singleStaffData));
    newStaffInfo[field] = event.target.value;
    this.setState({ singleStaffData: newStaffInfo });
    console.log(this.state.singleStaffData);
  }

  createStaff() {
    console.log(this.state.newStaffData);
    let { newStaffData } = this.state;
    //validation
    if (newStaffData.first_name
      && newStaffData.password
      && newStaffData.email
      && newStaffData.group_id
      && newStaffData.hire_date
      && newStaffData.maker_checker) {

      let staffUrl = 'staffs';
      axios.post(this.state.baseUrl + staffUrl,
        this.state.newStaffData,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          this.setState({ newCreatedStaff: this.state.newStaffData });
          console.log("Successfully created: " + this.state.newCreatedStaff);
        })
        .catch(err => {
          console.log(err);
        })

      this.fetchStaffs();
      this.setState({ visible: true });
      this.toggle();
    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
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

      }).then(this.toggleEdit())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single staff: " + err);
      })
  }

  findStaffDelete(staffId) {
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

      }).then(this.toggleConfirm())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single staff: " + err);
      })
  }

  updateStaff() {
    console.log(this.state.singleStaffData);
    let { singleStaffData } = this.state;
    //validation
    if (singleStaffData.first_name
      && singleStaffData.password
      && singleStaffData.email
      && singleStaffData.group_id
      && singleStaffData.hire_date
      && singleStaffData.maker_checker) {

      let apiUrl = 'staffs';
      axios.put(this.state.baseUrl + apiUrl,
        this.state.singleStaffData,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          this.setState({ newCreatedStaff: this.state.singleStaffData });
        })
        .catch(err => {
          console.log("Could not update staff record ");
          // debugger;
        })

      this.fetchStaffs();
      this.setState({ visibleUpdate: true });
      this.toggleEdit();
    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  deleteStaff(staffId) {
    if (staffId > 0) {
      let apiUrl = 'staffs/' + staffId;
      console.log(this.state.baseUrl + apiUrl);
      axios.delete(this.state.baseUrl + apiUrl,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          console.log(response);
        })
        .catch(err => {
          console.log("Could not delete staff record: " + err);
          let res = JSON.parse(JSON.stringify(err.response.data));
          console.log("Server response status: " + res.status);
          console.log("Server response message: " + res.message);
          // debugger;
        })
      this.toggleConfirm()
    }
  }

  fetchStaffs() {
    let staffUrl = 'staffs';
    console.log(this.state.baseUrl + staffUrl);
    axios.get(this.state.baseUrl + staffUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        console.log(response.data.payload)
        this.setState({ staffData: response.data.payload });
      }).catch(err => {
        //debugger;
        console.log("Error fetching staffs");
      })
  }



  render() {
    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }

    let { groupData } = this.state;
    let { showAction } = this.state;

    const showGroup = (groupID) => {
      console.log("I ran! ");
      let itemGroupName = "";
      groupData.forEach(group => {
        if (groupID === group.id) {
          console.log("There was a match!");
          itemGroupName = group.name;
        }
      })
      return itemGroupName;
    }

    const staff = this.state.staffData;
    const staffGroup = this.state.groupData;
    const singleStaff = this.state.singleStaffData;
    // if (singleStaff.first_name) {
    //   console.log(singleStaff.first_name);
    //   console.log(singleStaff);
    // }
    return (

      <div className="animated fadeIn">

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All Staffs
              </CardHeader>
              <CardBody>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  User <strong>{this.state.newCreatedStaff.first_name}</strong> has been created and submitted for activation.
                </Alert>
                <Alert color="success" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  Update request for user <strong>{this.state.newCreatedStaff.first_name}</strong> has been submitted for authorization.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>First Name</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Group</th>
                      <th style={showAction}>Action</th>
                    </tr>
                  </thead>
                  <tbody>{staff.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.first_name}</td>
                        <td>{item.email}</td>
                        <td>{item.maker_checker}</td>
                        <td>{showGroup(item.group_id)}</td>
                        <td style={showAction}>
                          <Button size="sm" color="primary" onClick={e => this.findStaff(item.id)}><i className="fa fa-dot-circle-o"></i> Update</Button>{' '}
                          <Button size="sm" color="danger" onClick={e => this.findStaffDelete(item.id)}><i className="fa fa-ban"></i> Delete</Button>

                        </td>
                      </tr>
                    )
                  })}
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

        <Button onClick={this.toggle} className="mr-1" style={showAction}>Create New Staff</Button>

        {/* Create Staff Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>Create Staff</ModalHeader>
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
                      <Label htmlFor="firstName">First Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
                        onChange={this.updateValue.bind(this, 'first_name')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="lastName">Last Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
                        onChange={this.updateValue.bind(this, 'last_name')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="email-input">Email <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
                        onChange={this.updateValue.bind(this, 'email')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password">Password <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="password" id="password" name="password" placeholder="Password" required
                        onChange={this.updateValue.bind(this, 'password')}
                      />
                      <FormText className="help-block">Choose a strong password</FormText>
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="phone">Phone</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.updateValue.bind(this, 'phone')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="date-hire">Hire Date <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      {/* <DatePicker
                        placeholderText="Click to select a date"
                        todayButton={"Go To Today"}
                        onChange={this.dateUpdate}
                        dateFormat="yyyy/MM/dd"
                      /> */}
                      {/* <Input type="hidden" id="date-hire" name="hire_date" placeholder="Enter Date"
                        onChange={this.updateValue.bind(this, 'hire_date')} value={this.state.newDate}
                      />
                      <Flatpickr
                        options={{ dateFormat: "m-d-Y" }}
                        onChange={this.dateUpdate} /> */}
                      <Input type="text" id="hire-date" name="hire_date" placeholder="Date MM/dd/yyyy"
                        onChange={this.updateValue.bind(this, 'hire_date')}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="select">Group  <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="select" name="group_id" id="select" onChange={this.updateValue.bind(this, 'group_id')}>
                        <option value="0">Please select</option>
                        {staffGroup.map(function (item, key) {
                          return (
                            <option value={item.id} key={key}>{item.name}</option>
                          )
                        })}

                      </Input>
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label>Role <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col md="9">
                      <FormGroup check className="radio">
                        <Input className="form-check-input" type="radio" id="radio1" name="maker_checker" value="MAKER" onChange={this.updateValue.bind(this, 'maker_checker')} />
                        <Label check className="form-check-label" htmlFor="radio1">Initiator</Label>
                      </FormGroup>
                      <FormGroup check className="radio">
                        <Input className="form-check-input" type="radio" id="radio2" name="maker_checker" value="CHECKER" onChange={this.updateValue.bind(this, 'maker_checker')} />
                        <Label check className="form-check-label" htmlFor="radio2">Authorizer</Label>
                      </FormGroup>
                      <FormGroup check className="radio">
                        <Input className="form-check-input" type="radio" id="radio3" name="maker_checker" value="NONE" onChange={this.updateValue.bind(this, 'maker_checker')} />
                        <Label check className="form-check-label" htmlFor="radio3">None</Label>
                      </FormGroup>
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.createStaff}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggle}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>



        {/*Modal to update staffs*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}>View and Update Staff</ModalHeader>
          <ModalBody>

            <Card>
              <CardHeader>
                <strong></strong> Staff details below
              </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  <Input type="hidden" name="id" value={singleStaff.id} onChange={this.readUpdateValue.bind(this, 'id')} />
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="firstName">First Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
                        onChange={this.readUpdateValue.bind(this, 'first_name')} value={singleStaff.first_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="lastName">Last Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
                        onChange={this.readUpdateValue.bind(this, 'last_name')} value={singleStaff.last_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="email-input">Email <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
                        onChange={this.readUpdateValue.bind(this, 'email')} value={singleStaff.email}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password">Password <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="password" id="password" name="password" placeholder="Your Password" autoComplete="email"
                        onChange={this.readUpdateValue.bind(this, 'password')} value={singleStaff.password}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="phone">Phone</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.readUpdateValue.bind(this, 'phone')} value={singleStaff.phone}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="date-hire">Hire Date <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="date-hire" name="hire_date" placeholder="Date MM/dd/yyyy"
                        onChange={this.readUpdateValue.bind(this, 'hire_date')} value={singleStaff.hire_date}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="select">Group  <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="select" name="group_id" id="group_id"
                        onChange={this.readUpdateValue.bind(this, 'group_id')}
                      >
                        {staffGroup.map(function (groupItem, groupKey) {
                          if (groupItem.id === singleStaff.group_id) {
                            return (

                              <option value={singleStaff.group_id} defaultValue>{groupItem.name}</option>
                            )
                          } else {
                            return (
                              <option value={groupItem.id} key={groupKey}>{groupItem.name}</option>
                            )
                          }
                        })}

                      </Input>
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label>Role <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col md="9">
                      <Input type="select" name="maker_checker" id="maker_checker"
                        onChange={this.readUpdateValue.bind(this, 'maker_checker')}
                      >
                        <option value={singleStaff.maker_checker}>{singleStaff.maker_checker}</option>
                        <option value="MAKER">Initiator</option>
                        <option value="CHECKER">Authorizer</option>
                      </Input>
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.updateStaff}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>






        {/*Modal to delete staffs*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>Confirm Delete</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>

                <p>Are you sure you want to delete staff: {singleStaff.first_name}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deleteStaff(singleStaff.id)}>Delete</Button>{' '}
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

export default Staffs;
