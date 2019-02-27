import React, { Component } from 'react';
import { Card, CardBody, CardHeader, Col, Row, Table } from 'reactstrap';
import axios from 'axios';
class Accounts extends Component {

  constructor(props) {
    super(props);
    this.state = {
      accountData: [],
      redirectToReferrer: false,
      redirectToMainMenu: false
    };
  }

  componentDidMount() {
    if (typeof this.props.staff === 'undefined') {

      console.log(JSON.parse(sessionStorage.getItem("userData")).token);
      let baseUrl = 'http://localhost:9100/api/account';
      //let header = new Headers({ "Content-Type": "application/json", "Authorization": JSON.parse(sessionStorage.getItem("userData")).token });
      axios.get(baseUrl, { headers: { 'Authorization': JSON.parse(sessionStorage.getItem("userData")).token } }).then((response) => {
        console.log(response.data)
        //this.setState({ accountData: response.data });
      }).then(err => {
        //debugger;
      })
    }
  }

  render() {
    const accounts = this.state.accountData;
    return (
      <div className="animated fadeIn">

        <Row>
          <Col>
            <Card>
              <CardHeader>
                <i className="fa fa-align-justify"></i> All Accounts
              </CardHeader>
              <CardBody>
                <Table hover bordered striped responsive size="sm">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Task</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>{accounts.map(function (item, key) {
                    return (
                      <tr key={key}>
                        <td>{item.id}</td>
                        <td>{item.firstName}</td>
                        <td>{item.email}</td>
                      </tr>
                    )
                  })}
                  </tbody>
                </Table>
                {/* <nav>
                  <Pagination>
                    <PaginationItem><PaginationLink previous tag="button">Prev</PaginationLink></PaginationItem>
                    <PaginationItem active>
                      <PaginationLink tag="button">1</PaginationLink>
                    </PaginationItem>
                    <PaginationItem><PaginationLink tag="button">2</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink tag="button">3</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink tag="button">4</PaginationLink></PaginationItem>
                    <PaginationItem><PaginationLink next tag="button">Next</PaginationLink></PaginationItem>
                  </Pagination>
                </nav> */}
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>

    );
  }
}

export default Accounts;
