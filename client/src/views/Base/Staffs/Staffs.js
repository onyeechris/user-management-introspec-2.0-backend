import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import {
  // Form, FormGroup, Input, Label, 
  Alert
} from 'reactstrap';
// import DatePicker from "react-datepicker";
// import Flatpickr from "react-flatpickr";
import 'flatpickr/dist/themes/material_green.css'
import "react-datepicker/dist/react-datepicker.css";
import { Redirect } from "react-router-dom";
import axios from 'axios';
// var DataTable = require('react-data-components').DataTable;
import Pagination2 from "react-js-pagination";

// import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchStaffs, fetchStaff, deleteStaff, createStaff, updateStaff } from '../../../actions/action_staff';
import { fetchGroups } from '../../../actions/action_group';
import { FormattedMessage } from "react-intl";
import CreateStaffForm from './CreateStaffForm';
import UpdateStaffForm from './UpdateStaffForm';

let loggedInUserRole = "";
class Staffs extends Component {

  constructor(props) {
    super(props);
    this.state = {
      staffData: [],
      staffTableData: {},
      itemsPerPage: 20,
      activePage: 1,
      redirectToReferrer: false,
      redirectToMainMenu: false,
      modal: false,
      editModal: false,
      confirmModal: false,
      greyedOut: true,
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
      // Change this back to 'none' for role access security
      showAction: {
        "display": "block"
      },
      startDate: '',
      currentError: ''
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
    this.changePageItem = this.changePageItem.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  componentDidMount() {

    if (sessionStorage.getItem("userData")) {
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

      // let groupUrl = 'groups';
      // axios.get(this.state.baseUrl + groupUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      this.props.fetchGroups()
        .then((response) => {
          this.setState({ groupData: response.data.payload });
        }).then(err => {
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


  handlePageChange = (pageNumber) => {
    let pageNumberParam = pageNumber - 1;
    this.fetchStaffsPage(pageNumberParam);
    this.setState({ activePage: pageNumber });
  }

  handleChange = (hireDate) => {
    this.setState({ startDate: hireDate });
    let formatted_date = (("0" + (hireDate.getMonth() + 1)).slice(-2)) + "/" + ("0" + hireDate.getDate()).slice(-2) + "/" + hireDate.getFullYear()
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.newStaffData));
    newStaffInfo.hire_date = formatted_date;
    this.setState({ newStaffData: newStaffInfo });
    console.log(newStaffInfo);
  }

  handleDateChange = (hireDate) => {
    this.setState({ greyedOut: false });
    this.setState({ startDate: hireDate });
    let formatted_date = (("0" + (hireDate.getMonth() + 1)).slice(-2)) + "/" + ("0" + hireDate.getDate()).slice(-2) + "/" + hireDate.getFullYear()
    var singleStaffInfo = JSON.parse(JSON.stringify(this.state.singleStaffData));
    singleStaffInfo.hire_date = formatted_date;
    this.setState({ singleStaffData: singleStaffInfo });
    console.log(singleStaffInfo);
  }

  toggle() {
    this.setState({ formError: "" });
    this.setState({ startDate: "" });
    // this.setState({ newCreatedStaff: {} });
    // this.setState({ newStaffData: {} });
    // this.setState({ newStaffInfo: {} });
    this.setState({
      modal: !this.state.modal,
    });
  }

  toggleEdit() {
    this.setState({ formError: "" });
    this.setState({ startDate: this.state.singleStaffData.hire_date });
    this.setState({ greyedOut: true });
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    this.setState({ formError: "" });
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

  updateValue(field, event) {
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.newStaffData));
    newStaffInfo[field] = event.target.value;
    this.setState({ newStaffData: newStaffInfo });
  }

  readUpdateValue(field, event) {
    this.setState({ greyedOut: false });
    var newStaffInfo = JSON.parse(JSON.stringify(this.state.singleStaffData));
    newStaffInfo[field] = event.target.value;
    this.setState({ singleStaffData: newStaffInfo });
  }

  createStaff() {
    let { newStaffData } = this.state;
    //validation
    if (newStaffData.first_name
      && newStaffData.password
      && newStaffData.email
      && newStaffData.group_id
      && newStaffData.hire_date
      && newStaffData.maker_checker) {

      this.props.createStaff(newStaffData).then(result => {
        this.setState({ newCreatedStaff: newStaffData });
        this.setState({ visible: true });
        this.toggle();
      }, error => {
        console.log(error);
        this.setState({ formError: error });
      }
      )
    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  createNewStaff = (newStaffData) => {
    console.log(newStaffData);
    // let hireDate = newStaffData.hire_date;
    // this.setState({ startDate: hireDate });
    // let formatted_date = (("0" + (hireDate.getMonth() + 1)).slice(-2)) + "/" + ("0" + hireDate.getDate()).slice(-2) + "/" + hireDate.getFullYear()

    // var newStaffInfo = JSON.parse(JSON.stringify(this.state.newStaffData));
    // newStaffInfo.hire_date = formatted_date;
    // this.setState({ newStaffData: newStaffInfo });
    // console.log(newStaffInfo);

    // newStaffData.hire_date = formatted_date;
    newStaffData.hire_date = this.state.newStaffData.hire_date;
    newStaffData.maker_checker = "MAKER";
    // console.log(newStaffData.hire_date);
    this.setState({ newCreatedStaff: newStaffData });

    this.props.createStaff(newStaffData).then(result => {
      this.setState({ visible: true });
      this.toggle();
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    }
    )

  }

  findStaff(staffId) {
    this.props.fetchStaff(staffId).then(result => {
      this.setState({ singleStaffData: result.data });
    }, error => {
      this.setState({ formError: error });
    }).then(this.toggleEdit());

    // let apiUrl = 'staffs/' + staffId;
    // axios.get(this.state.baseUrl + apiUrl,
    //   {
    //     headers: {
    //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
    //     }
    //   })
    //   .then(response => {
    //     this.setState({ singleStaffData: response.data });

    //   }).then(this.toggleEdit())
    //   .catch(err => {
    //     // debugger;
    //     //console.log("Couldn't find single staff: " + err);
    //   })
  }

  findStaffDelete(staffId) {
    let apiUrl = 'staffs/' + staffId;
    //console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    //console.log(this.state.baseUrl + apiUrl);
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
        //console.log("Couldn't find single staff: " + err);
      })
  }

  updateStaff() {
    let { singleStaffData } = this.state;
    console.log('Something happened here');
    console.log(singleStaffData);
    //validation
    if (singleStaffData.first_name
      // && singleStaffData.password
      && singleStaffData.email
      // && singleStaffData.group_id
      && singleStaffData.hire_date
      && singleStaffData.maker_checker) {

      console.log('Something eventually happened here');
      this.props.updateStaff(this.state.singleStaffData).then(result => {
        this.setState({ newCreatedStaff: this.state.singleStaffData });
        this.setState({ visibleUpdate: true });
        this.toggleEdit();
      }, error => {
        this.setState({ formError: error });
      }
      )


      // let apiUrl = 'staffs';
      // axios.put(this.state.baseUrl + apiUrl,
      //   this.state.singleStaffData,
      //   {
      //     headers: {
      //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
      //     }
      //   })
      //   .then(response => {
      //     this.setState({ newCreatedStaff: this.state.singleStaffData });
      //     this.fetchStaffs();
      //     this.setState({ visibleUpdate: true });
      //     this.toggleEdit();
      //   })
      //   .catch(err => {
      //     console.log("Could not update record: " + err);
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
      //     // debugger;
      //   })


    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  updateNewStaff(staffUpdateData) {
    console.log(staffUpdateData);
    this.setState({ newCreatedStaff: this.state.singleStaffData });
    this.props.updateStaff(staffUpdateData).then(result => {
      this.setState({ visibleUpdate: true });
      this.toggleEdit();
    }, error => {
      this.setState({ formError: error });
    }
    )
  }

  deleteStaff(staffId) {
    if (staffId > 0) {
      this.props.deleteStaff(staffId).then(result => {
        this.toggleConfirm()
        console.log(result);
      }, error => {
        console.log("Could not delete staff record: " + error);
        this.setState({ formError: error });
      });

      if (this.props.staffData.staffDeleteError) {
        console.log(this.props.staffData.staffDeleteError);
      }

      // axios.delete(this.state.baseUrl + apiUrl,
      //   {
      //     headers: {
      //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
      //     }
      //   })
      //   .then(response => {
      //     this.toggleConfirm()
      //     console.log(response);
      //   })
      //   .catch(err => {
      // console.log("Could not delete staff record: " + err);
      // let res = JSON.parse(JSON.stringify(err.response.data));
      // console.log("Server response status: " + res.status);
      // console.log("Server response message: " + res.message);

      //     let errMessage = '';
      //     switch (res.status) {
      //       case 400:
      //         errMessage = "Error, bad request";
      //         break;
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
      //     // debugger;
      //   })

    }
  }

  fetchStaffs() {
    let staffUrl = 'staffs?size=' + this.state.itemsPerPage;
    axios.get(this.state.baseUrl + staffUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        this.setState({ staffData: response.data.payload });
      }).catch(err => {
        //debugger;
      })
  }

  fetchStaffsPage(pageNumber) {
    let staffUrl = 'staffs?size=' + this.state.itemsPerPage + '&page=' + pageNumber;
    //console.log(this.state.baseUrl + staffUrl);
    axios.get(this.state.baseUrl + staffUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        //console.log(response.data.payload)
        this.setState({ staffData: response.data.payload });
      }).catch(err => {
        //debugger;
        //console.log("Error fetching staffs - " + err);
      })
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

  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }


  render() {

    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }

    let { groupData } = this.state;
    let { showAction } = this.state;

    const showGroup = (groupID) => {
      let itemGroupName = "";
      groupData.forEach(group => {
        if (groupID === group.id) {
          //console.log("There was a match!");
          itemGroupName = group.name;
        }
      })
      return itemGroupName;
    }

    let staff = this.state.staffData;
    const staffGroup = this.state.groupData;
    const singleStaff = this.state.singleStaffData;

    // console.log(this.props);

    return (

      <div className="animated fadeIn">
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
                <p style={{ color: 'red' }}>{this.state.currentError}</p>
                <Alert color="warning" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("User")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been created and submitted for activation")}.
                </Alert>
                <Alert color="warning" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  {this.translate("Update request for user")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been submitted for authorization")}.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th><FormattedMessage id="tableId" defaultMessage="ID" /></th>
                      <th><FormattedMessage id="tableFirstName" defaultMessage="First Name" /></th>
                      <th><FormattedMessage id="tableEmail" defaultMessage="Email" /></th>
                      <th><FormattedMessage id="tableRoleName" defaultMessage="Role" /></th>
                      <th><FormattedMessage id="tableGroupName" defaultMessage="Group" /></th>
                      <th style={showAction}><FormattedMessage id="tableAction" defaultMessage="Action" /></th>
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
                          <Button size="sm" color="primary" onClick={e => this.findStaff(item.id)}><i className="fa fa-dot-circle-o"></i>
                            {' '}{this.translate("Update")}</Button>{' '}
                          <Button size="sm" color="danger" onClick={e => this.findStaffDelete(item.id)}><i className="fa fa-ban"></i>
                            {' '}{this.translate("Delete")}</Button>
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





        {/* Create Staff Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}><FormattedMessage id="Create User" defaultMessage="Create User" /></ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> <FormattedMessage id="Please fill the form below" defaultMessage="Please fill the form below" />
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>
                {/* <Form action="" method="post" className="form-horizontal">
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
                      <Input type="number" size="11" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.updateValue.bind(this, 'phone')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="date-hire">Hire Date <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">

                      <DatePicker
                        selected={this.state.startDate}
                        onChange={this.handleChange}
                        className="form-control" placeholderText="Select Date"
                        // dateFormat="LL"
                        dateFormat="MM/dd/yyyy"
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="select">Group  <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="select" name="group_id" id="select" onChange={this.updateValue.bind(this, 'group_id')}>
                        <option value="">Please select</option>
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
                </Form> */}
                <CreateStaffForm onSubmit={this.createNewStaff} staffGroup={staffGroup} toggle={this.toggle} currentDate={this.state.startDate} handleChange={this.handleChange} />
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
                {/* <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
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
                      <DatePicker
                        selected={this.state.startDate}
                        onChange={this.handleDateChange}
                        className="form-control" placeholderText="Select Date"
                        dateFormat="MM/dd/yyyy"
                        value={singleStaff.hire_date}
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
                    <Button color="primary" onClick={this.updateStaff} disabled={this.state.greyedOut ? true : false}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                  </ModalFooter>
                </Form> */}
                <UpdateStaffForm onSubmit={this.updateNewStaff} singleStaff={singleStaff} staffGroup={staffGroup} toggle={this.toggleEdit} currentDate={this.state.startDate} handleChange={this.handleChange} />
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
                <p style={{ color: 'red' }}>{this.state.formError}</p>

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

const mapStateToProps = (state) => {
  // console.log('State is ', state)
  return {
    staffData: state.staff,
    groupData: state.group
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchStaffs,
    fetchStaff,
    deleteStaff,
    createStaff,
    updateStaff,
    fetchGroups
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Staffs);
// export default Staffs;
