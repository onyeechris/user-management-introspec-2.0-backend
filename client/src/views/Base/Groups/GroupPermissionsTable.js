import React, { Component } from 'react';
import {
    // FormGroup, Label, Card, CardBody, CardHeader, Col, Row, Form, 
    Table
} from 'reactstrap';
import 'react-dual-listbox/lib/react-dual-listbox.css';
// import { connect } from 'react-redux';
// import { bindActionCreators } from 'redux';
// import { fetchGroup } from '../../../actions/action_group';
// import { Link } from "react-router-dom";
import Pagination2 from "react-js-pagination";
import { FormattedMessage } from 'react-intl';

class GroupPermissionsTable extends Component {

    // componentDidMount(){
    //     console.log(this.props);
    //     const { hideField, initialPermissionData, activePage, itemsPerPage, changePageItem } = this.props
    //     let permData = initialPermissionData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);
    // }

    // handlePageChange = (pageNumber) => {
    //     const { hideField, initialPermissionData, activePage, itemsPerPage, changePageItem } = this.props
    //     let permData = initialPermissionData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);


    //     console.log(pageNumber);
    //     let permDataNew = [];
    //     const updateStateVariable = () => {
    //       firstPermissionsData = { ...initialPermissionData };
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
        const { hideField, initialPermissionData, activePage, itemsPerPage, changePageItem } = this.props
        let permData = initialPermissionData ? initialPermissionData.slice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage) : [];

        const handlePageChange = (pageNumber) => {
            console.log(pageNumber);
            let permDataNew = [];
            const updateStateVariable = () => {
                //   firstPermissionsData = { ...initialPermissionData };
                //   permDataNew = firstPermissionsData.splice((itemsPerPage * activePage) - itemsPerPage, itemsPerPage);
                // permDataNew = numbers.filter(function (x) { return x > 3; });
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

        const translate = (pageString) => {
            return (
                <FormattedMessage id={pageString} defaultMessage={pageString} />
            )
        }

        return (
            <div>
                <h5>
                    {translate("Group Permissions")}: &nbsp;
                        {initialPermissionData ?
                        initialPermissionData.length > 0 ? initialPermissionData.length : "None"
                        : ""}
                </h5>

                <Table hover bordered striped responsive size="sm" style={permData.length > 0 ? {} : hideField}>
                    <thead>
                        <tr>
                            <th>{translate("ID")}</th>
                            <th>{translate("Action")}</th>
                            <th>{translate("Description")}</th>
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

// const mapStateToProps = (state) => {
//     console.log('State is ', state)
//     return {

//     }
// }

// const mapDispatchToProps = (dispatch) => {
//     return bindActionCreators({

//     }, dispatch)
// }

// export default connect(mapStateToProps, mapDispatchToProps)(GroupPermissionsTable);

export default GroupPermissionsTable;