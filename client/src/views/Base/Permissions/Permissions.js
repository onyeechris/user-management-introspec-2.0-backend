import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Form, FormGroup, Input, Label, Alert } from 'reactstrap';
// import axios from 'axios';
import Pagination2 from "react-js-pagination";
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchPermissions, fetchPermission, deletePermission, createPermission, updatePermission } from '../../../actions/action_permission';
import { FormattedMessage } from 'react-intl';
import CreatePermissionForm from "./CreatePermissionForm";
// import { Link } from "react-router-dom";

let loggedInUser = "";
let permissionsToLoad = [];
class Permissions extends Component {

  constructor(props) {
    super(props);
    this.state = {
      sessionModuleData: JSON.parse(sessionStorage.getItem("moduleData")),
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
      // baseUrl: 'http://localhost:9100/api/',
      formError: "",
      showAction: {
        "display": "block"
      },
      greyedOut: true,
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
    // console.log(JSON.parse(sessionStorage.getItem("userData")).token);
    let permissionUrl = '?size=' + this.state.itemsPerPage;
    this.props.fetchPermissions(permissionUrl).then(result => {
      this.setState({ permissionData: result.data.payload });
      this.setState({ permissionTableData: result.data.meta });
    }, error => {
      this.setState({ formError: error });
    })

    if (this.props.permissionData.data) {
      console.log(this.props.permissionData.data.payload);
      permissionsToLoad = this.props.permissionData.data.payload;
    }

    loggedInUser = sessionStorage.getItem("loggedInUser");
    if (loggedInUser.toLowerCase().includes("sysdev")) {
      let visible = {
        "display": "block"
      }
      this.setState({ showAction: visible });
    }
  }


  handlePageChange = (pageNumber) => {
    let pageNumberParam = pageNumber - 1;
    this.fetchPermissionsPage(pageNumberParam);
    this.setState({ activePage: pageNumber });
  }


  toggle() {
    this.setState({ formError: "" });
    // this.fetchPermissions();
    this.setState({
      modal: !this.state.modal,
    });
  }

  toggleEdit() {
    this.setState({ formError: "" });
    // this.fetchPermissions();
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    // this.fetchPermissions();
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
    //console.log(this.state.newPermissionData);
  }

  readUpdateValue(field, event) {
    this.setState({ greyedOut: false });
    var newPermissionInfo = JSON.parse(JSON.stringify(this.state.singlePermissionData));
    newPermissionInfo[field] = event.target.value;
    this.setState({ singlePermissionData: newPermissionInfo });
    //console.log(this.state.singlePermissionData);
  }

  createPermission() {
    //console.log(this.state.newPermissionData);
    let { newPermissionData } = this.state;

    if (newPermissionData.action && newPermissionData.description) {

      // let permissionUrl = 'permissions';
      // axios.post(this.state.baseUrl + permissionUrl,
      //   this.state.newPermissionData,
      //   {
      //     headers: {
      //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
      //     }
      //   })
      this.props.createPermission(this.state.newPermissionData)
        .then(response => {
          this.setState({ newCreatedPermission: response.data });
          console.log(this.state.newPermissionData);
          console.log(this.state.permissionData);
          this.state.permissionData.push(this.state.newPermissionData);
          console.log(this.state.permissionData);
          this.setState({ visible: true });
          this.toggle();
        })
        .catch(err => {
          this.setState({ formError: err.response.data.message + "" })
        })

    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  createNewPermission = (newPermissionData) => {
    console.log(newPermissionData);
    this.props.createPermission(newPermissionData)
      .then(response => {
        console.log(response);
        this.setState({ newCreatedPermission: response.data });
        let newPermission = this.state.permissionData
        newPermission.push(response.data);
        this.setState({ newPermission: newPermission }, this.setState({ visible: true }, this.toggle()));
      })
      .catch(err => {
        this.setState({ formError: err + "" })
      })
  }

  findPermission(permissionId) {
    this.props.fetchPermission(permissionId).then(result => {
      this.setState({ singlePermissionData: result.data });
    }, error => {
      this.setState({ formError: error })
    }).then(this.toggleEdit);
  }

  findPermissionDelete(permissionId) {
    // let apiUrl = 'permissions/' + permissionId;
    // axios.get(this.state.baseUrl + apiUrl,
    //   {
    //     headers: {
    //       'Authorization': JSON.parse(sessionStorage.getItem("userData")).token
    //     }
    //   })
    this.props.fetchPermission(permissionId)
      .then(result => {
        this.setState({ singlePermissionData: result.data }, this.toggleConfirm());
      }, error => {
        console.log(error);
      })
  }

  updatePermission() {
    let { singlePermissionData } = this.state;

    if (singlePermissionData.action && singlePermissionData.description) {

      this.props.updatePermission(singlePermissionData).then(result => {
        for (var item in permissionsToLoad) {
          if (permissionsToLoad[item].id === singlePermissionData.id) {
            permissionsToLoad[item] = singlePermissionData;
            console.log("got here");
            console.log(singlePermissionData);
            console.log(permissionsToLoad[item]);
            console.log(permissionsToLoad);
          }
        }
        this.setState({ newCreatedPermission: singlePermissionData });
        this.setState({ visibleUpdate: true });
      }, error => {
        this.setState({ formError: error });
        console.log(error);
      }).then(this.toggleEdit())

    }
    else {
      this.setState({ formError: "Please fill all fields marked with (*)" });
    }
  }

  deletePermission(permissionId) {
    if (permissionId > 0) {

      this.props.deletePermission(permissionId).then(result => {
        for (var i = 0; i < permissionsToLoad.length; i++) {
          if (permissionsToLoad[i].id === permissionId) {
            permissionsToLoad.splice(i, 1);
          }
        }
        this.setState({ permissionData: permissionsToLoad });
      }, error => {
        console.log(error);
        this.setState({ formError: error })
      }).then(this.toggleConfirm());

      if (this.props.permissionData.permissionDeleted) {
        console.log(this.props.permissionData.permissionDeleted.data);

      } else if (this.props.permissionData.permissionDeleteError) {
        console.log('There was an error');
        console.log(this.props.permissionData.permissionDeleteError);
      }
    }
  }

  fetchPermissions() {
    let permissionUrl = '?size=' + this.state.itemsPerPage;
    // axios.get(this.state.baseUrl + permissionUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } })
    this.props.fetchPermissions(permissionUrl)
      .then((response) => {
        this.setState({ permissionData: response.data.payload });
      }).catch(err => {
      })
  }

  fetchPermissionsPage(pageNumber) {
    let permissionUrl = '?size=' + this.state.itemsPerPage + '&page=' + pageNumber;
    this.props.fetchPermissions(permissionUrl);
  }

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
          this.fetchPermissions();
        }
      }
      catch (error) {
        console.log(error);
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
    let { showAction } = this.state;
    // let permissions = this.state.permissionData;
    const singlePermission = this.state.singlePermissionData;
    // console.log(this.props);
    if (this.props.permissionData.permissionsFetched) {
      console.log(this.props.permissionData.permissionsFetched.data.payload);
      permissionsToLoad = this.props.permissionData.permissionsFetched.data.payload;
    }
    return (
      <div className="animated fadeIn">
        {/* <Link to='/apps/module_view'> */}
        <Button onClick={this.props.history.goBack}>
          <i className="fa fa-arrow-left"></i> {' '}
          {this.translate("Back")}
        </Button>
        {/* </Link> */}
        <br />
        <br />

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i>{' '} <strong style={{ fontSize: "20px" }}> {this.state.sessionModuleData.name} </strong> {' | '} {this.translate("All Permissions")}
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>{this.translate("Create New Permission")}</Button>
                </div>
                {/* <div className="pull-right" style={showAction}>
                  &nbsp; &nbsp;
                </div> */}

              </CardHeader>
              <CardBody>
                <Alert color="success" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("Permission")}{' '}
                  <strong>{this.state.newCreatedPermission.name}</strong> {this.translate("has been created")}.
                </Alert>
                <Alert color="success" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  {this.translate("Permission")}{' '}
                  <strong>{this.state.newCreatedPermission.action}</strong> {this.translate("has been updated")}.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>{this.translate("tableId")}</th>
                      <th>{this.translate("Name")}</th>
                      <th>{this.translate("Description")}</th>
                      <th style={showAction}>
                        {this.translate("tableAction")}</th>
                    </tr>
                  </thead>
                  <tbody>{permissionsToLoad.map((item, key) => {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.action}</td>
                        <td>{item.description}</td>
                        <td style={showAction}>
                          <Button size="sm" color="primary" onClick={e => this.findPermission(item.id)}><i className="fa fa-dot-circle-o"></i>
                            {' '}{this.translate("Update")}</Button>{' '}
                          <Button size="sm" color="danger" onClick={e => this.findPermissionDelete(item.id)}><i className="fa fa-ban"></i>
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
                    totalItemsCount={this.state.permissionTableData ? this.state.permissionTableData.totalElements : null}
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
              </CardBody>
            </Card>
          </Col>
        </Row>




        {/* Create Permission Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>{this.translate("Create Permission")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Please fill the form below")}
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{JSON.stringify(this.props.permissionData.permissionCreateError)}</p>
                {/* <p style={{ color: 'red' }}>{this.state.formError}</p> */}
                {/* <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
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
                </Form> */}
                <CreatePermissionForm onSubmit={this.createNewPermission} toggle={this.toggle} />
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>



        {/*Modal to update permissions*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}>{this.translate("View and Update Permission")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Permission details below")}
              </CardHeader>
              <CardBody>

                <Form action="" method="post" className="form-horizontal" >
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <Input type="hidden" name="id" value={singlePermission.id} onChange={this.readUpdateValue.bind(this, 'id')} />
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="action">{this.translate("Action")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="action" name="action" placeholder="Enter The Action"
                        onChange={this.readUpdateValue.bind(this, 'action')} value={singlePermission.action}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">{this.translate("Description")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter a Description" required
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singlePermission.description}
                      />
                    </Col>
                  </FormGroup>
                  <ModalFooter>
                    <Button color="primary" onClick={this.updatePermission}>{this.translate("Submit")}</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>{this.translate("Cancel")}</Button>
                  </ModalFooter>
                </Form>
              </CardBody>
            </Card>

          </ModalBody>

        </Modal>






        {/*Modal to delete permissions*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>{this.translate("Confirm Delete")}</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>

                <p>{this.translate("Are you sure you want to delete permission")}: {singlePermission.action}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deletePermission(singlePermission.id)}>{this.translate("Yes")}</Button>{' '}
                  <Button color="secondary" onClick={this.toggleConfirm}>{this.translate("Cancel")}</Button>
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
    permissionData: { ...state.permission },
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchPermissions,
    fetchPermission,
    deletePermission,
    createPermission,
    updatePermission
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Permissions);
// export default Permissions;
