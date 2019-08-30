import React, { Component } from 'react';
import {
    // FormGroup, Label, Card, CardBody, CardHeader, Col, Row, Form, 
    Table
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';

class GroupStaffsTable extends Component {

    // componentDidMount(){
    //     console.log(this.props);
    //     const { hideField, staffsData, activePage, itemsPerPage, changePageItem } = this.props
    //     let permData = staffsData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);
    // }

    // handlePageChange = (pageNumber) => {
    //     const { hideField, staffsData, activePage, itemsPerPage, changePageItem } = this.props
    //     let permData = staffsData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);


    //     console.log(pageNumber);
    //     let permDataNew = [];
    //     const updateStateVariable = () => {
    //       firstPermissionsData = { ...staffsData };
    //       permDataNew = firstPermissionsData.splice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);
    //       return true;
    //     }
    //     const reloadTable = async () => {
    //       try {
    //         const response = await updateStateVariable();
    //         if (response) {
    //           permData = permDataNew;
    //           console.log(permData);
    //         }
    //       }
    //       catch (error) {
    //         console.log(error);
    //       }
    //     }
    //     reloadTable();
    //   }


    render() {
        const { hideField, staffsData, activePage, itemsPerPage, changePageItem } = this.props

        const translate = (pageString) => {
            return (
                <FormattedMessage id={pageString} defaultMessage={pageString} />
            )
        }


        return (
            <div>
                <h5>
                    {translate("Group Users")}: &nbsp;

                    {staffsData.length > 0 ? staffsData.length : "None"}</h5>

                <Table hover bordered striped responsive size="sm" style={staffsData.length > 0 ? {} : hideField}>
                    <thead>
                        <tr>
                            <th>{translate("ID")}</th>
                            <th>{translate("First Name")}</th>
                            <th>{translate("Email")}</th>
                        </tr>
                    </thead>
                    <tbody>{staffsData.map((item, key) => {
                        return (
                            <tr key={key}>
                                <td>{item.id}</td>
                                <td>{item.first_name}</td>
                                <td>{item.email}</td>
                            </tr>
                        )
                    })}
                    </tbody>
                    <nav>
                        <Pagination2
                            activePage={activePage}
                            itemsCountPerPage={itemsPerPage}
                            totalItemsCount={staffsData ? staffsData.length : null}
                            pageRangeDisplayed={5}
                        // onChange={handlePageChange}
                        />
                    </nav>
                    <div className="pull-left">
                        <select onChange={changePageItem.bind(this)} className="form-control">
                            <option value="20">No of Items</option>
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


export default GroupStaffsTable;