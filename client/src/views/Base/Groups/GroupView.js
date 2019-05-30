import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Table } from 'reactstrap';
import { ModalFooter } from 'reactstrap';
import {
    Form, FormGroup, Label,
    //  ButtonDropdown, DropdownToggle, DropdownItem, DropdownMenu
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';

class GroupView extends Component {
    render() {
        let { singleGroupData } = this.props;
        console.log(this.state);
        return (
            <div className="animated fadeIn">
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


            </div>

        );
    }
}

export default GroupView;
