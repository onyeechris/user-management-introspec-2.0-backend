import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import {
  Form, Input,
  FormGroup, Label,
  Alert
} from 'reactstrap';

// import DatePicker from "react-datepicker";

// import Flatpickr from "react-flatpickr";
import 'flatpickr/dist/themes/material_green.css'
import "react-datepicker/dist/react-datepicker.css";
// import { Redirect } from "react-router-dom";
// import axios from 'axios';
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
// import UpdateStaffForm from './UpdateStaffForm';

class Staffs extends Component {

  constructor(props) {
    super(props);
    this.state = {
      staffData: [],
      staffTableData: {},
      itemsPerPage: 20,
      activePage: 1,
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
        user_type: ""
      },
      groupData: [],
      singleStaffData: {},
      newCreatedStaff: {},
      visible: false,
      visibleUpdate: false,
      newDate: '',
      formError: "",
      userRole: "",
      // Change this back to 'none' for role access security
      showAction: {
        "display": "none"
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
    this.toggleUserGroup = this.toggleUserGroup.bind(this);
    this.updateStaff = this.updateStaff.bind(this);
    this.fetchStaffs = this.fetchStaffs.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
    this.changePageItem = this.changePageItem.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.findStaffView = this.findStaffView.bind(this);
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

      this.props.fetchGroups()
        .then((response) => {
          this.setState({ groupData: response.data.payload });
        }).then(err => {
        })
    } else {
      this.setState({ redirectToReferrer: true });
    }

    let loggedInUserRole = sessionStorage.getItem("userRole");
    if (loggedInUserRole.toLowerCase().includes("admin")) {
      let makeVisible = {
        "display": "inline-block"
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
    this.setState({ formError: "" },
      this.setState({ greyedOut: true },
        this.setState({ startDate: this.state.singleStaffData.hire_date },
          this.setState({
            editModal: !this.state.editModal,
          }))));
  }
  toggleConfirm() {
    console.log("hello");
    this.setState({ formError: "" });
    this.setState({
      confirmModal: !this.state.confirmModal,
    });
  }
  toggleUserGroup() {
    this.setState({ formError: "" });
    this.setState({
      groupModal: !this.state.groupModal,
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
    console.log(this.state.newStaffData);
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
    // newStaffData.maker_checker = "MAKER";
    newStaffData.user_type = this.state.newStaffData.user_type;
    newStaffData.hire_date = this.state.newStaffData.hire_date;
    newStaffData.activated = true;
    this.setState({ newCreatedStaff: newStaffData });

    this.props.createStaff(newStaffData).then(result => {
      let staffList = this.state.staffData;
      staffList.push(result.data);
      this.setState({ staffData: staffList }, this.setState({ visible: true }, this.toggle()));
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    }
    )

  }

  findStaff(staffId) {
    // this.setState({ singleStaffData: {} });
    this.props.fetchStaff(staffId).then(result => {
      this.setState({ singleStaffData: result.data },
        this.toggleEdit());
    }, error => {
      this.setState({ formError: error });
    })
  }

  findStaffDelete(staffId) {
    this.props.fetchStaff(staffId).then(result => {
      this.setState({ singleStaffData: result.data }, this.toggleConfirm());
    }, error => {
      this.setState({ formError: error });
    })
  }

  updateStaff() {
    let { singleStaffData } = this.state;
    console.log('Something happened here');
    console.log(singleStaffData);
    //validation
    if (singleStaffData.first_name
      && singleStaffData.email
      && singleStaffData.user_type) {

      console.log('Something eventually happened here');
      this.props.updateStaff(singleStaffData).then(result => {
        this.setState({ newCreatedStaff: singleStaffData });
        this.setState({ visibleUpdate: true });
        // live update the table
        let allStaffs = this.state.staffData;
        allStaffs.forEach((staff, index) => {
          console.log("Index:: " + index, staff)
          if (staff.id === singleStaffData.id) {
            allStaffs[index] = singleStaffData;
          }
        });
        this.setState({ staffData: allStaffs }, this.toggleEdit());
      }, error => {
        this.setState({ formError: error });
      })
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
    }
  }

  // fetch staff to view groups
  findStaffView(staffId) {
    this.props.fetchStaff(staffId).then(result => {
      console.log(result);
      this.setState({ singleStaffData: result.data }, this.toggleUserGroup());
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    })
  }

  fetchStaffs() {
    let staffUrl = '?size=' + this.state.itemsPerPage;
    this.props.fetchStaffs(staffUrl).then((response) => {
      this.setState({ staffData: response.data.payload });
    }, error => {
      this.setState({ formError: error });
    })
  }

  fetchStaffsPage(pageNumber) {
    let staffUrl = '?size=' + this.state.itemsPerPage + '&page=' + pageNumber;
    this.props.fetchStaffs(staffUrl).then(result => {
      console.log(result.data.payload)
      this.setState({ staffData: result.data.payload });
    }, error => {
      this.setState({ formError: error });
    })
  }


  // Select number of items to display on table
  changePageItem(numberOfItems) {
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
        this.setState({ formError: error });
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
    // let { groupData } = this.state;
    let { showAction } = this.state;

    // const showGroup = (groupID) => {
    //   let itemGroupName = "";
    //   groupData.forEach(group => {
    //     if (groupID === group.id) {
    //       itemGroupName = group.name;
    //     }
    //   })
    //   return itemGroupName;
    // }

    let staff = this.state.staffData;
    const staffGroup = this.state.groupData;
    const singleStaff = this.state.singleStaffData;

    return (

      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> {this.translate("AllStaffs")}
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>{this.translate("Create New Staff")}</Button>
                </div>
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.currentError}</p>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("User")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been created successfully")}.
                </Alert>
                <Alert color="success" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  {this.translate("User")} <strong>{this.state.newCreatedStaff.first_name}</strong> {' '}{this.translate("has been updated successfully")}.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>{this.translate("ID")}</th>
                      <th>{this.translate("First Name")}</th>
                      <th>{this.translate("Email")}</th>
                      <th>{this.translate("Role")}</th>
                      {/* <th>{this.translate("tableGroupName")}</th> */}
                      <th>{this.translate("tableAction")}</th>
                    </tr>
                  </thead>
                  <tbody>{staff.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.first_name}</td>
                        <td>{item.email}</td>
                        <td>{item.user_type}</td>
                        {/* <td>{showGroup(item.group_id)}</td> */}
                        <td>
                          <Button size="sm" color="primary" onClick={e => this.findStaff(item.id)} style={showAction}><i className="fa fa-edit"></i>
                            {' '}{this.translate("Update")}</Button>{' '}
                          <Button size="sm" color="danger" onClick={e => this.findStaffDelete(item.id)} style={showAction}><i className="fa fa-trash-o"></i>
                            {' '}{this.translate("Delete")}</Button>{' '}
                          <Button size="sm" onClick={e => this.findStaffView(item.id)}><i className="fa fa-eye"></i>
                            {' '}{this.translate("View Groups")}</Button>
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
          <ModalHeader toggle={this.toggle}>{this.translate("Create User")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Please fill the form below")}
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
                <CreateStaffForm onSubmit={this.createNewStaff} staffGroup={staffGroup} toggle={this.toggle} currentDate={this.state.startDate} handleChange={this.handleChange} updateValue={this.updateValue} />
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>



        {/*Modal to update staffs*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}> {this.translate("View and Update User")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong> </strong> {this.translate("User details below")}
              </CardHeader>
              <CardBody>
                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="firstName">{this.translate("First Name")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
                        onChange={this.readUpdateValue.bind(this, 'first_name')} value={singleStaff.first_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="lastName">{this.translate("Last Name")}</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
                        onChange={this.readUpdateValue.bind(this, 'last_name')} value={singleStaff.last_name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="email-input">{this.translate("Email")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
                        onChange={this.readUpdateValue.bind(this, 'email')} value={singleStaff.email}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="phone">{this.translate("Phone")}</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.readUpdateValue.bind(this, 'phone')} value={singleStaff.phone}
                      />
                    </Col>
                  </FormGroup>
                  {/* <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="date-hire">{this.translate("Hire Date")} <span style={{ color: 'red' }}>*</span></Label>
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
                  </FormGroup> */}
                  {/* <FormGroup row>
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
                  </FormGroup> */}
                  <FormGroup row>
                    <Col md="3">
                      <Label>{this.translate("Role")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col md="9">
                      <Input type="select" name="user_type" id="user_type"
                        onChange={this.readUpdateValue.bind(this, 'user_type')}
                      >
                        <option value={singleStaff.user_type}>{singleStaff.user_type}</option>
                        <option value="ADMIN">Admin</option>
                        <option value="USER">User</option>
                        <option value="DEV">Dev</option>
                      </Input>
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.updateStaff} disabled={this.state.greyedOut ? true : false}>{this.translate("Submit")}</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>{this.translate("Cancel")}</Button>
                  </ModalFooter>
                </Form>
                {/* <UpdateStaffForm onSubmit={this.updateNewStaff} singleStaff={singleStaff} staffGroup={staffGroup} toggle={this.toggleEdit} currentDate={this.state.startDate} handleChange={this.handleChange} /> */}
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>






        {/*Modal to delete staffs*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>{this.translate("Confirm Delete")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>

                <p>{this.translate("Are you sure you want to delete user")}: {singleStaff.first_name}?</p>
                <ModalFooter>
                  <Button color="primary" onClick={e => this.deleteStaff(singleStaff.id)}>{this.translate("Delete")}</Button>{' '}
                  <Button color="secondary" onClick={this.toggleConfirm}>{this.translate("Cancel")}</Button>
                </ModalFooter>
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>





        {/** View User Groups Modal */}

        <Modal isOpen={this.state.groupModal} toggle={this.toggleUserGroup} className={this.props.className}>
          <ModalHeader toggle={this.toggleUserGroup}>{this.translate("View")} {" "} {this.state.singleStaffData.first_name}{"'s "} {this.translate("Groups")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardBody>
                <FormGroup row>
                  <Col md="3">
                    <Label htmlFor="name"><strong>
                      {this.translate("User Name")}
                    </strong></Label>
                  </Col>
                  <Col xs="12" md="9">
                    {this.state.singleStaffData.first_name} {' '} {this.state.singleStaffData.last_name}
                  </Col>
                </FormGroup>
                {/* 
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
                </FormGroup> */}

                <Row>
                  <Col md="12"> {this.state.singleStaffData.groups ?

                    this.state.singleStaffData.groups.length > 0 ?

                      <Table hover bordered striped responsive size="sm">
                        <thead>
                          <tr>
                            {/* <th>{this.translate("tableId")}</th> */}
                            <th>{this.translate("App Module")}</th>
                            <th>{this.translate("Group Name")}</th>
                          </tr>
                        </thead>
                        <tbody>{
                          this.state.singleStaffData.groups ?

                            this.state.singleStaffData.groups.map((item, key) => {
                              return (
                                <tr key={key}>
                                  {/* <td>{item.id}</td> */}
                                  <td>{item.mod}</td>
                                  <td>{item.name}</td>
                                </tr>
                              )
                            })
                            : ""}
                        </tbody>
                      </Table>
                      : this.translate("Not assigned to any group")
                    : this.translate("Not assigned to any group")}
                  </Col>
                </Row>

                <ModalFooter>
                  <Button color="secondary" onClick={this.toggleUserGroup}>{this.translate("Done")}</Button>
                </ModalFooter>

              </CardBody>
            </Card>
          </ModalBody>
        </Modal >


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
