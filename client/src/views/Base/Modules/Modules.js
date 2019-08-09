import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import {
  Form, FormGroup, Input, Label, Alert,
  //  ButtonDropdown, DropdownToggle, DropdownItem, DropdownMenu
  // Nav, NavItem, NavLink, TabContent, TabPane
} from 'reactstrap';
// import axios from 'axios';
import 'react-dual-listbox/lib/react-dual-listbox.css';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchModule, fetchModules, createModule, deleteModule, updateModule, saveModuleInfo } from '../../../actions/action_module';
import { fetchPermissions } from '../../../actions/action_permission';
import { FormattedMessage } from 'react-intl';
import CreateModuleForm from './CreateModuleForm';

let moduleDataToUpdate = {};
moduleDataToUpdate.permissions = [];

let loggedInUserRole = "";
let myCurrentPermissions = [];
let permissionsToDelete = [];
class Modules extends Component {

  constructor(props) {
    super(props);
    this.state = {
      moduleData: [],
      permissionData: [],
      myOtherModuleObject: {},
      redirectToReferrer: false,
      redirectToMainMenu: false,
      modal: false,
      editModal: false,
      confirmModal: false,
      viewModal: false,
      selected: [],
      newlySelected: [],
      newModuleData: {
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
      singleModuleData: {},
      singlePermissionData: {},
      baseUrl: 'http://localhost:9100/api/',
      addedModule: {},
      loadedPermissionsData: [],
      newCreatedModule: {},
      active: [],
      visible: false,
      visibleUpdate: false,
      dropdownOpen: new Array(19).fill(false),
      activeTab: '1',
      formError: "",
      // Change this back to 'none' for role access security
      showAction: {
        "display": "inline-block"
      },
      hideField: {
        "display": "none"
      },
      greyedOut: true,
      permissionsDeleteList: [],
    };
    this.toggle = this.toggle.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.toggleView = this.toggleView.bind(this);
    this.updateValue = this.updateValue.bind(this);
    this.readUpdateValue = this.readUpdateValue.bind(this);
    this.createModule = this.createModule.bind(this);
    this.findModule = this.findModule.bind(this);
    this.findModuleView = this.findModuleView.bind(this);
    this.fetchModules = this.fetchModules.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
  }

  componentDidMount() {

    console.log(this.props);
    this.props.fetchModules().then(result => {
      this.setState({ moduleData: result.data.payload });
    }, error => {
      this.setState({ formError: error });
    }
    )

    // this.props.fetchPermissions('?size=1000').then(result => {
    //   this.setState({ permissionData: result.data.payload });
    //   loadedPermissionsData = [
    //     result.data.payload.map((item) => {
    //       return ({ value: item.action, label: item.action })
    //     })
    //   ];
    // }, error => {
    //   // console.log(error);
    //   this.setState({ formError: error });
    // }
    // )

    loggedInUserRole = sessionStorage.getItem("userRole");
    if (loggedInUserRole.toLowerCase().includes("admin")) {
      let makeVisible = {
        "display": "inline-block"
      }
      this.setState({ showAction: makeVisible });
    }
  }

  toggle() {
    this.fetchModules();
    this.setState({ formError: "" });
    this.setState({
      modal: !this.state.modal,
    }, this.fetchModules());
  }
  toggleEdit() {
    this.fetchModules();
    this.setState({ formError: "" });
    this.setState({ greyedOut: true });
    permissionsToDelete = [];
    this.setState({
      editModal: !this.state.editModal,
    });
  }
  toggleConfirm() {
    this.fetchModules();
    this.setState({
      confirmModal: !this.state.confirmModal,
    });
  }

  toggleView() {
    this.fetchModules();
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
    var newModuleInfo = JSON.parse(JSON.stringify(this.state.newModuleData));
    newModuleInfo[field] = event.target.value;
    this.setState({ newModuleData: newModuleInfo });
    //// console.log(this.state.newModuleData);
  }

  readUpdateValue(field, event) {
    this.setState({ greyedOut: false });
    var newModuleInfo = JSON.parse(JSON.stringify(this.state.singleModuleData));
    newModuleInfo[field] = event.target.value;
    this.setState({ singleModuleData: newModuleInfo });
    //// console.log(this.state.singleModuleData);
  }

  createModule() {
    let { newModuleData } = this.state;
    if (newModuleData.name
      && newModuleData.description) {

      this.props.createModule(newModuleData).then(result => {
        this.setState({ addedModule: newModuleData });
      }, error => {
        // console.log(error);
        this.setState({ formError: error });
      }
      ).then(this.setState({ visible: true }))
      this.toggle();
    }
    else {
      this.setState({ formError: "The module must have a name and a description." });
    }
  }

  createNewModule = (newModuleData) => {
    this.props.createModule(newModuleData).then(result => {
      this.setState({ addedModule: newModuleData });
      let newList = this.state.moduleData;
      newList.push(newModuleData);
      this.setState({ moduleData: newList });
    }, error => {
      this.setState({ formError: error });
    }
    ).then(this.setState({ visible: true }))
    this.toggle();
  }

  // Hot reload page
  fetchModules() {
    this.props.fetchModules().then((response) => {
      this.setState({ moduleData: response.data.payload });
    }).catch(err => {
      console.log(err);
    })
  }

  // Get specific module to view module Details
  findModuleView(moduleId) {
    this.props.fetchModule(moduleId).then(result => {
      // console.log(result);
      this.setState({ singleModuleData: result.data });
      let resPermissions = result.data.permissions;
      let perm = [];
      resPermissions.forEach(permission => {
        perm.push(permission.action);
      })
      this.setState({ selected: perm });
      this.setState({ permissionData: result.data.permissions });
      this.props.saveModuleInfo(result);
      console.log(this.props);
    }, error => {
      // console.log(error);
    }
    ).then(console.log(this.props)
    ).then(this.props.history.push('/apps/module_view'))
  }

  // Get specific module for deleting
  findModuleDelete = (moduleId) => {
    this.props.fetchModule(moduleId)
      .then(response => {
        this.setState({ singleModuleData: response.data });
      }).then(this.toggleConfirm())
      .catch(err => {
        // debugger;
      })
  }

  // Send module id to delete module
  deleteModule(moduleId) {
    if (moduleId || moduleId === 0) {
      this.props.deleteModule(moduleId).then(result => {
        this.toggleConfirm();
      }, error => {
        // console.log(error);
        this.setState({ formError: error });
      }
      );
    }
  }

  // Function to find specific module by ID
  findModule(moduleId) {
    this.props.fetchModule(moduleId)
      .then(response => {
        this.setState({ singleModuleData: response.data });
      }).then(this.setState({ visible: false }, this.toggleEdit()))
      .catch(err => {
        this.setState({ formError: err });
      })
  }

  updateModule = (flag) => {

    const populateMyPermissions = () => {

      if (myCurrentPermissions) {

        moduleDataToUpdate.id = this.state.singleModuleData.id;
        moduleDataToUpdate.name = this.state.singleModuleData.name;
        moduleDataToUpdate.description = this.state.singleModuleData.description;

        if (flag === 1) {
          moduleDataToUpdate.permissions = [];
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
                moduleDataToUpdate.permissions.push(permissionObject);
              }
            });
          });
        } else {
          moduleDataToUpdate.permissions = [];
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
                moduleDataToUpdate.permissions.push(permissionObject);
              }
            });
          });
        }
        return true;
      }
    }

    const updateTheModule = () => {

      if (moduleDataToUpdate.name) {
        this.props.updateModule(moduleDataToUpdate, flag).then(result => {
          this.setState({ newCreatedModule: moduleDataToUpdate });
          this.setState({ visibleUpdate: true });
        }, error => {
          // console.log(error);
          this.setState({ formError: error });
        }
        ).then(this.toggleEdit());

      } else {
        this.setState({ formError: "The module must have a name." });
      }
    }

    const updateMyModule = async () => {
      try {
        const response = await populateMyPermissions()
        // debugger;
        if (response) {
          updateTheModule();
        }
      }
      catch (error) {
        //// console.log(error);
      }
    }
    updateMyModule();
  }



  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }





  render() {
    let checkboxStyle = {
      "overflowY": "scroll",
      "height": "130px"
    }
    const modules = this.state.moduleData ? this.state.moduleData : {};

    let { singleModuleData } = this.state;
    let { showAction } = this.state;


    return (
      <div className="animated fadeIn">
        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> {' '}{this.translate("All Apps")}
                <div className="pull-right">
                  <Button onClick={this.toggle} className="mr-1" style={showAction}>
                    {this.translate("Create App")}
                  </Button>
                </div>
              </CardHeader>
              <CardBody>
                <Alert color="info" isOpen={this.state.visible} toggle={this.onDismiss}>
                  {this.translate("App")} <strong> {this.state.addedModule.name} </strong> {' '}{this.translate("has been created")}.
                </Alert>
                <Alert color="info" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
                  {this.translate("Update request for module")} <strong> {this.state.newCreatedModule.name} </strong> {' '}{this.translate("has been submitted for authorization")}.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      {/* <th><FormattedMessage id="tableId" defaultMessage="ID" /></th> */}
                      <th><FormattedMessage id="Name" defaultMessage="Name" /></th>
                      <th><FormattedMessage id="Code" defaultMessage="Code" /></th>
                      <th><FormattedMessage id="Description" defaultMessage="Description" /></th>
                      <th><FormattedMessage id="Action" defaultMessage="Action" /></th>
                    </tr>
                  </thead>
                  <tbody>{modules.map((item, key) => {
                    return (
                      <tr key={key}>
                        {/* <td>{item.id}</td> */}
                        <td>{item.name}</td>
                        <td>{item.code}</td>
                        <td>{item.description}</td>
                        <td>
                          <Button style={showAction} size="sm" color="primary" onClick={e => this.findModule(item.id)}><i className="fa fa-dot-circle-o"></i>
                            {' '}<FormattedMessage id="Update" defaultMessage="Update" />
                          </Button>{' '}
                          <Button style={showAction} size="sm" color="danger" onClick={e => this.findModuleDelete(item.id)}><i className="fa fa-ban"></i>
                            {' '}<FormattedMessage id="Delete" defaultMessage="Delete" />
                          </Button>{' '}
                          <Button size="sm" color="secondary" onClick={e => this.findModuleView(item.id)}><i className="fa fa-note"></i>
                            {' '}<FormattedMessage id="View" defaultMessage="View" />
                          </Button>{' '}
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




        {/* Create Module Modal */}
        <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
          <ModalHeader toggle={this.toggle}>Create User Module</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> Please fill the form below
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>


                <CreateModuleForm onSubmit={this.createNewModule} toggle={this.toggle} />


              </CardBody>
            </Card>
          </ModalBody>
        </Modal>






        {/*Modal to update modules*/}

        <Modal isOpen={this.state.editModal} toggle={this.toggleEdit} className={this.props.className}>
          <ModalHeader toggle={this.toggleEdit}>View and Update Module</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> Module details below
              </CardHeader>
              <CardBody>
                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  {/* <Input type="hidden" name="id" value={singleModuleData.id} onChange={this.readUpdateValue.bind(this, 'id')} /> */}
                  <p style={{ color: 'red' }}>{this.state.formError}</p>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="name" name="name" placeholder="Enter Module Name"
                        onChange={this.readUpdateValue.bind(this, 'name')} value={singleModuleData.name}
                      />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Code</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="code" name="code" placeholder="Enter Code" required
                        onChange={this.readUpdateValue.bind(this, 'code')} value={singleModuleData.code}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singleModuleData.description}
                      />
                    </Col>
                  </FormGroup>

                  <ModalFooter>
                    <Button color="primary" onClick={e => this.updateModule} disabled={this.state.greyedOut ? true : false}>Update Module</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>Cancel</Button>
                  </ModalFooter>

                </Form>
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>







        {/*Modal to view module permissions*/}

        <Modal isOpen={this.state.viewModal} toggle={this.toggleView} className={this.props.className}>
          <ModalHeader toggle={this.toggleView}>View Module Permissions</ModalHeader>
          <ModalBody>

            <Card>
              <CardHeader>
                <strong></strong> Module details below
              </CardHeader>
              <CardBody>

                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal" >
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">Name</Label>
                    </Col>
                    <Col xs="12" md="9">
                      {singleModuleData.name}
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">Description</Label>
                    </Col>
                    <Col xs="12" md="9">
                      {singleModuleData.description}
                    </Col>
                  </FormGroup>
                  <br />
                  <h5>Module Permissions: {this.state.permissionData.length > 0 ? this.state.permissionData.length : "None"}</h5>

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








        {/*Modal to delete modules*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>Confirm Delete</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>

                <p>Are you sure you want to delete module: {singleModuleData.name}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deleteModule(singleModuleData.id)}>Delete</Button>{' '}
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
    moduleData: state.module,
    // singleModuleData: state
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchModule,
    fetchModules,
    fetchPermissions,
    createModule,
    deleteModule,
    updateModule,
    saveModuleInfo
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Modules);
// export default Modules;
