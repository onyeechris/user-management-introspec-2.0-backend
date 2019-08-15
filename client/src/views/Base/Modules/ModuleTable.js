import React, { Component } from 'react';
import {
    // FormGroup, Label, Card, CardBody, CardHeader, Col, Row, Form, 
    Table
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';

class ModuleTable extends Component {

    render() {
        const { hideField, initialPermissionData, activePage, itemsPerPage, changePageItem } = this.props
        let permData = initialPermissionData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);

        const handlePageChange = (pageNumber) => {
            console.log(pageNumber);
            let permDataNew = [];
            const updateStateVariable = () => {
                return true;
            }
            const reloadTable = async () => {
                try {
                    const response = await updateStateVariable();
                    if (response) {
                        permData = permDataNew;
                        console.log(permData);
                    }
                }
                catch (error) {
                    console.log(error);
                }
            }
            reloadTable();
        }

        return (
            <div className="container">
                <h5>
                    <FormattedMessage id="Module Permissions" defaultMessage="Module Permissions" />: &nbsp;

                    {initialPermissionData.length > 0 ? initialPermissionData.length : "None"}</h5>

                <Table hover bordered striped responsive size="sm" style={permData.length > 0 ? {} : hideField}>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Action</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>{permData.map((item, key) => {
                        return (
                            <tr key={key}>
                                <td>{item.id}</td>
                                <td>{item.action}</td>
                                <td>{item.description}</td>

                            </tr>
                        )
                    })}
                    </tbody>
                    <nav>
                        <Pagination2
                            activePage={activePage}
                            itemsCountPerPage={itemsPerPage}
                            totalItemsCount={initialPermissionData ? initialPermissionData.length : null}
                            pageRangeDisplayed={5}
                            onChange={handlePageChange}
                        />
                    </nav>
                    <div className="pull-left">
                        <select onChange={changePageItem.bind(this)} className="form-control">
                            <option value="10">No of Items: 10</option>
                            <option value="5">5</option>
                            <option value="10">10</option>
                            <option value="20">20</option>
                            <option value="50">50</option>
                        </select>
                    </div>
                </Table>
            </div>
        );
    }
}

export default ModuleTable;