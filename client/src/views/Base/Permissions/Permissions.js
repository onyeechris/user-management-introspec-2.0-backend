import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Form, FormGroup, Input, Label, Alert } from 'reactstrap';
import axios from 'axios';
import Pagination2 from "react-js-pagination";

let loggedInUser = "";

class Permissions extends Component {

  constructor(props) {
    super(props);
    this.state = {
      permissionData: [],
      permissionTableData: {},
      itemsPerPage: 10,
      activePage: 1,
      modal: false,
      editModal: false,
      confirmModal: false,
      newPermissionData: {
        id: "",
        action: "",
        description: "",
      },
      groupData: [],
      singlePermissionData: {},
      newCreatedPermission: {},
      visible: false,
      visibleUpdate: false,
      baseUrl: 'http://localhost:9100/api/',
      formError: "",
      showAction: {
        "display": "none"
      },
    };
    this.toggle = this.toggle.bind(this);
    this.updateValue = this.updateValue.bind(this);
    this.readUpdateValue = this.readUpdateValue.bind(this);
    this.createPermission = this.createPermission.bind(this);
    this.findPermission = this.findPermission.bind(this);
    this.findPermissionDelete = this.findPermissionDelete.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.updatePermission = this.updatePermission.bind(this);
    this.fetchPermissions = this.fetchPermissions.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
    this.changePageItem = this.changePageItem.bind(this);
  }

  componentDidMount() {
    if (typeof this.props.permission === 'undefined') {

      console.log(JSON.parse(sessionStorage.getItem("userData")).token);
      let { baseUrl } = this.state;
      let permissionUrl = 'permissions?size=' + this.state.itemsPerPage;
      axios.get(baseUrl + permissionUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {
        console.log(response.data)
        this.setState({ permissionData: response.data.payload });
        this.setState({ permissionTableData: response.data.meta });
      }).catch(err => {
        //debugger;
      })

      loggedInUser = sessionStorage.getItem("loggedInUser");
      console.log(loggedInUser);
      if (loggedInUser.toLowerCase().includes("sysdev")) {
        let visible = {
          "display": "block"
        }
        this.setState({ showAction: visible });
      }
    }
  }


  handlePageChange = (pageNumber) => {
    let pageNumberParam = pageNumber - 1;
    this.fetchPermissionsPage(pageNumberParam);
    this.setState({ activePage: pageNumber });
    // this.props.fetchOffices(pageNumber);
    // this.setState({activePage: pageNumber});
  }


  toggle() {
    this.setState({ formError: "" });
    this.fetchPermissions();
    this.setState({
      modal: !this.state.modal,
    });
  }

  toggleEdit() {
    this.setState({ formError: "" });
    this.fetchPermissions();
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    this.fetchPermissions();
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
    var newPermissionInfo = JSON.parse(JSON.stringify(this.state.newPermissionData));
    newPermissionInfo[field] = event.target.value;
    this.setState({ newPermissionData: newPermissionInfo });
    console.log(this.state.newPermissionData);
  }

  readUpdateValue(field, event) {
    var newPermissionInfo = JSON.parse(JSON.stringify(this.state.singlePermissionData));
    newPermissionInfo[field] = event.target.value;
    this.setState({ singlePermissionData: newPermissionInfo });
    console.log(this.state.singlePermissionData);
  }

  createPermission() {
    console.log(this.state.newPermissionData);
    let { newPermissionData } = this.state;

    if (newPermissionData.action && newPermissionData.description) {

      let permissionUrl = 'permissions';
      axios.post(this.state.baseUrl + permissionUrl,
        this.state.newPermissionData,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          this.setState({ newCreatedPermission: response.data });
          this.fetchPermissions();
          this.setState({ visible: true });
        })
        .catch(err => {
          console.log(err);
          // debugger;
        })

      this.toggle();
    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  findPermission(permissionId) {
    let apiUrl = 'permissions/' + permissionId;
    console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    console.log(this.state.baseUrl + apiUrl);
    axios.get(this.state.baseUrl + apiUrl,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ singlePermissionData: response.data });

      }).then(this.toggleEdit())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single permission: " + err);
      })
  }

  findPermissionDelete(permissionId) {
    let apiUrl = 'permissions/' + permissionId;
    console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    console.log(this.state.baseUrl + apiUrl);
    axios.get(this.state.baseUrl + apiUrl,
      {
        headers: {
          'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
        }
      })
      .then(response => {
        this.setState({ singlePermissionData: response.data });

      }).then(this.toggleConfirm())
      .catch(err => {
        // debugger;
        console.log("Couldn't find single permission: " + err);
      })
  }

  updatePermission() {
    console.log(this.state.singlePermissionData);
    let { singlePermissionData } = this.state;

    if (singlePermissionData.action && singlePermissionData.description) {
      let apiUrl = 'permissions';
      axios.put(this.state.baseUrl + apiUrl,
        this.state.singlePermissionData,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          this.setState({ newCreatedPermission: response });
          this.setState({ visibleUpdate: true });
        })
        .catch(err => {
          console.log("Could not update permission record ");
          // debugger;
        })
      this.fetchPermissions();
      this.toggleEdit();
    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  deletePermission(permissionId) {
    if (permissionId > 0) {
      let apiUrl = 'permissions/' + permissionId;
      console.log(this.state.baseUrl + apiUrl);
      axios.delete(this.state.baseUrl + apiUrl,
        {
          headers: {
            'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
          }
        })
        .then(response => {
          console.log(response);
          this.fetchPermissions();
        })
        .catch(err => {
          console.log("Could not delete permission record: " + err);
          // debugger;
        })
      this.toggleConfirm();
    }
  }

  fetchPermissions() {
    let permissionUrl = 'permissions?size=' + this.state.itemsPerPage;
    console.log(this.state.baseUrl + permissionUrl);
    axios.get(this.state.baseUrl + permissionUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        console.log(response.data.payload);
        console.log("I fetched!");
        this.setState({ permissionData: response.data.payload });
      }).catch(err => {
        //debugger;
        console.log("Error fetching permissions");
      })
  }
  fetchPermissionsPage(pageNumber) {
    let permissionUrl = 'permissions?size=' + this.state.itemsPerPage + '&page=' + pageNumber;
    console.log(this.state.baseUrl + permissionUrl);
    axios.get(this.state.baseUrl + permissionUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
      .then((response) => {
        console.log(response.data.payload);
        console.log("I fetched!");
        this.setState({ permissionData: response.data.payload });
      }).catch(err => {
        //debugger;
        console.log("Error fetching permissions");
      })
  }


  changePageItem(numberOfItems) {
    console.log("Your items per page: " + numberOfItems.target.value);

    const updateStateVariable = () => {
      this.setState({ itemsPerPage: numberOfItems.target.value });
      return true;
    }

    //using an asynchronous function

    const reloadTable = async () => {

      try {
        const response = await updateStateVariable();
        if (response) {
          this.fetchPermissions();
        }
      }
      catch (error) {
        console.log(error);
      }
    }

    reloadTable();
  }






  render() {
    let { showAction } = this.state;
    const permissions = this.state.permissionData;
    const singlePermission = this.state.singlePermissionData;
    return (
      <div className="animated fadeIn">

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All Permissions
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>Create New Permission</Button>
                </div>
                <div className="pull-right" style={showAction}>
                  &nbsp; &nbsp;
                </div>
                <div className="pull-right">
                  <select onChange={this.changePageItem.bind(this)} className="form-control">
                    <option value="10">No of Items: 10</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="50">50</option>
                  </select>
                </div>
              </CardHeader>
              <CardBody>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  Permission <strong>{this.state.newCreatedPermission.action}</strong> has been created.
                </Alert>
                <Alert color="success" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  Permission <strong>{this.state.newCreatedPermission.action}</strong> has been updated.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Action</th>
                      <th>Description</th>
                      <th style={showAction}>Actions</th>
                    </tr>
                  </thead>
                  <tbody>{permissions.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.action}</td>
                        <td>{item.description}</td>
                        <td style={showAction}>
                          <Button size="sm" color="primary" onClick={e => this.findPermission(item.id)}><i className="fa fa-dot-circle-o"></i> Update</Button>{' '}
                          <Button size="sm" color="danger" onClick={e => this.findPermissionDelete(item.id)}><i className="fa fa-ban"></i> Delete</Button>
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
                    totalItemsCount={this.state.permissionTableData ? this.state.permissionTableData.totalElements : null}
                    pageRangeDisplayed={5}
                    onChange={this.handlePageChange}
                  />
                </nav>
              </CardBody>
            </Card>
          </Col>
        </Row>




        {/* Create Permission Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>Create Permission</ModalHeader>
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
                      <Label htmlFor="action">Action <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="action" name="action" placeholder="Enter The Action"
                        onChange={this.updateValue.bind(this, 'action')}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter a Description" required
                        onChange={this.updateValue.bind(this, 'description')}
                      />
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.createPermission}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggle}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>



        {/*Modal to update permissions*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}>View and Update Permission</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> Permission details below
      </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <Input type="hidden" name="id" value={singlePermission.id} onChange={this.readUpdateValue.bind(this, 'id')} />
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="action">Action <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="action" name="action" placeholder="Enter The Action"
                        onChange={this.readUpdateValue.bind(this, 'action')} value={singlePermission.action}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter a Description" required
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singlePermission.description}
                      />
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.updatePermission}>Submit</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>






        {/*Modal to delete permissions*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>Confirm Delete</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>

                <p>Are you sure you want to delete permission: {singlePermission.action}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deletePermission(singlePermission.id)}>Yes</Button>{' '}
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

export default Permissions;
