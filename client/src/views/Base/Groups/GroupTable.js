import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import {
    Alert,
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

class GroupTable extends Component {
    render() {
        console.log(this.props);
        const groups = this.state.groupData ? this.state.groupData : {};

        let { showAction } = this.state;
        return (
            <div className="container">
                <Row>
                    <Col>
                        <Card>
                            <CardHeader>
                                <i className="fa fa-align-justify"></i> All Groups
                                <div className="pull-right">
                                    <Button onClick={this.toggle} className="mr-1" style={showAction}>Create Group</Button>
                                </div>
                            </CardHeader>
                            <CardBody>
                                <Alert color="info" isOpen={this.state.visible} toggle={this.onDismiss}>
                                    Group <strong>{this.state.addedGroup.name}</strong> has been created and submitted for activation.
                                </Alert>
                                <Alert color="info" isOpen={this.state.visibleUpdate} toggle={this.onDismissUpdate}>
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

            </div>
        );
    }
}

const mapStateToProps = (state) => {
    console.log('State is ', state)
    return {

    }
}

const mapDispatchToProps = (dispatch) => {
    return bindActionCreators({

    }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupTable);

// export default GroupTable;