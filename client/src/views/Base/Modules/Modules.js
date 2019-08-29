import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import { Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import {
  Form, FormGroup, Input, Label, Alert,
  //  ButtonDropdown, DropdownToggle, DropdownItem, DropdownMenu
  // Nav, NavItem, NavLink, TabContent, TabPane
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchModule, fetchModules, createModule, deleteModule, updateModule, saveModuleInfo } from '../../../actions/action_module';
import { fetchPermissions } from '../../../actions/action_permission';
import { FormattedMessage } from 'react-intl';
import CreateModuleForm from './CreateModuleForm';


let loggedInUserRole = "";
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
      singleModuleData: {},
      updatedModuleData: {},
      singlePermissionData: {},
      addedModule: {},
      active: [],
      visible: false,
      visibleUpdate: false,
      visibleDelete: false,
      formError: "",
      // Change this back to 'none' for role access security
      showAction: {
        "display": "inline-block"
      },
      hideField: {
        "display": "none"
      },
      greyedOut: true,
    };
    this.toggle = this.toggle.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.toggleConfirm = this.toggleConfirm.bind(this);
    this.updateValue = this.updateValue.bind(this);
    this.readUpdateValue = this.readUpdateValue.bind(this);
    this.createNewModule = this.createNewModule.bind(this);
    this.findModule = this.findModule.bind(this);
    this.findModuleView = this.findModuleView.bind(this);
    this.fetchModules = this.fetchModules.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.onDismissUpdate = this.onDismissUpdate.bind(this);
    this.onDismissDelete = this.onDismissDelete.bind(this);
  }



  componentDidMount() {

    console.log(this.props);
    this.props.fetchModules().then(result => {
      this.setState({ moduleData: result.data.payload });
    }, error => {
      this.setState({ formError: error });
    }
    )

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
    this.setState({ formError: "" }, this.dismissAllAlerts());
    this.setState({
      modal: !this.state.modal,
    }, this.fetchModules());
  }

  toggleEdit() {
    this.fetchModules();
    this.setState({ formError: "" });
    this.setState({ greyedOut: true });
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

  onDismiss() {
    this.setState({ visible: false });
  }
  onDismissUpdate() {
    this.setState({ visibleUpdate: false });
  }
  onDismissDelete() {
    this.setState({ visibleDelete: false });
  }

  dismissAllAlerts() {
    this.setState({ visible: false });
    this.setState({ visibleUpdate: false });
    this.setState({ visibleDelete: false });
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

  // Create a new App Module
  createNewModule = (newModuleData) => {
    this.props.createModule(newModuleData).then(result => {
      this.setState({ addedModule: newModuleData });
      let newList = this.state.moduleData;
      newList.push(newModuleData);
      this.setState({ moduleData: newList }, this.setState({ visible: true }, this.toggle()));
    }, error => {
      this.setState({ formError: error });
    })
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
  findModuleView = (moduleCode) => {
    this.props.fetchModule(moduleCode).then(result => {
      console.log(result);
      this.props.saveModuleInfo(result);
      sessionStorage.setItem("userModule", moduleCode);
      sessionStorage.setItem("moduleData", JSON.stringify(result.data));
      this.setState({ singleModuleData: result.data }, this.props.history.push('/apps/module_view'));
    }, error => {
      this.setState({ formError: error });
    });
  }

  // Get specific module for deleting
  findModuleDelete = (moduleCode) => {
    this.props.fetchModule(moduleCode).then(result => {
      this.dismissAllAlerts();
      this.setState({ singleModuleData: result.data }, this.toggleConfirm());
    }, error => {
      this.setState({ formError: error });
    });
  }

  // Send module code to delete module
  deleteModule(moduleCode) {
    if (moduleCode || moduleCode === 0) {
      this.props.deleteModule(moduleCode).then(result => {
        this.toggleConfirm();
      }, error => {
        console.log(error);
        this.setState({ formError: error });
      }
      ).then(this.setState({ visibleDelete: true }));
    }
  }

  // Function to find specific module by code for editing
  findModule(moduleCode) {
    this.props.fetchModule(moduleCode)
      .then(result => {
        this.dismissAllAlerts();
        console.log(result);
        this.setState({ singleModuleData: result.data });
        this.setState({ updatedModuleData: result.data }, this.toggleEdit());
      }, error => {
        this.setState({ formError: error });
      });
  }

  // Function to update App Modules
  updateModule = () => {
    console.log(this.state.singleModuleData);
    this.props.updateModule(this.state.singleModuleData).then(result => {
      this.setState({ visibleUpdate: true }, this.toggleEdit());
    }, error => {
      console.log(error);
      this.setState({ formError: error });
    }
    )
  }

  // Function for internationalization FormattedMessage
  translate = (pageString) => {
    return (
      <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
  }





  render() {

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
                  {this.translate("Update on module")} <strong> {this.state.updatedModuleData.name} </strong> {' '}{this.translate("successful")}.
                </Alert>
                <Alert color="info" isOpen={this.state.visibleDelete} toggle={this.onDismissDelete}>
                  {this.translate("Module")} <strong> {this.state.singleModuleData.name} </strong> {' '}{this.translate("has been deleted successfully")}.
                </Alert>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      {/* <th>{this.translate("tableId" defaultMessage="ID" /></th> */}
                      <th>{this.translate("Name")}</th>
                      <th>{this.translate("Code")}</th>
                      <th>{this.translate("Description")}</th>
                      <th>{this.translate("Action")}</th>
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
                          <Button style={showAction} size="sm" color="primary" onClick={e => this.findModule(item.code)}><i className="fa fa-dot-circle-o"></i>
                            {' '}{this.translate("Update")}
                          </Button>{' '}
                          <Button style={showAction} size="sm" color="danger" onClick={e => this.findModuleDelete(item.code)}><i className="fa fa-ban"></i>
                            {' '}{this.translate("Delete")}
                          </Button>{' '}
                          <Button size="sm" color="secondary" onClick={e => this.findModuleView(item.code)}><i className="fa fa-note"></i>
                            {' '}{this.translate("View")}
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
          <ModalHeader toggle={this.toggle}>{this.translate("Create User Module")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Please fill the form below")}
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
          <ModalHeader toggle={this.toggleEdit}>{this.translate("View and Update Module")}</ModalHeader>
          <ModalBody>
            <Card>
              <CardHeader>
                <strong></strong> {this.translate("Module details below")}
              </CardHeader>
              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>
                <Form action="" method="post" className="form-horizontal" >
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="name">{this.translate("Name")} <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="name" name="name" placeholder="Enter Module Name"
                        onChange={this.readUpdateValue.bind(this, 'name')} value={singleModuleData.name}
                      />
                    </Col>
                  </FormGroup>

                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="description">{this.translate("Description")}</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="text" id="description" name="description" placeholder="Enter Description" required
                        onChange={this.readUpdateValue.bind(this, 'description')} value={singleModuleData.description}
                      />
                    </Col>
                  </FormGroup>

                  <ModalFooter>
                    <Button color="primary" onClick={e => this.updateModule()} disabled={this.state.greyedOut ? true : false}>{this.translate("Update Module")}</Button>{' '}
                    <Button color="secondary" onClick={this.toggleEdit}>{this.translate("Cancel")}</Button>
                  </ModalFooter>

                </Form>
              </CardBody>
            </Card>
          </ModalBody>
        </Modal>







        {/*Modal to delete modules*/}

        <Modal isOpen={this.state.confirmModal} toggle={this.toggleConfirm} className={this.props.className}>
          <ModalHeader toggle={this.toggleConfirm}>{this.translate("Confirm Delete")}</ModalHeader>
          <ModalBody>

            <Card>

              <CardBody>
                <p style={{ color: 'red' }}>{this.state.formError}</p>

                <p>{this.translate("Are you sure you want to delete module")}: {singleModuleData.name}?</p>

                <ModalFooter>
                  <Button color="primary" onClick={e => this.deleteModule(singleModuleData.code)}>{this.translate("Delete")}</Button>{' '}
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
