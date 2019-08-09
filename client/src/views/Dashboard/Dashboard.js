// import React, { Component, lazy, Suspense } from "react";
import React, { Component } from "react";
// import axios from 'axios';
import { Bar, Line } from "react-chartjs-2";
import { Link } from "react-router-dom";
import {
  // Badge,
  // Button,
  // ButtonDropdown,
  // ButtonGroup,
  // ButtonToolbar,
  Card,
  CardBody,
  // CardFooter,
  // CardHeader,
  // CardTitle,
  Col,
  // Dropdown,
  // DropdownItem,
  // DropdownMenu,
  // DropdownToggle,
  // Progress,
  Row
  // Table
} from "reactstrap";
import { CustomTooltips } from "@coreui/coreui-plugin-chartjs-custom-tooltips";
import {
  getStyle,
  // hexToRgba 
} from "@coreui/coreui/dist/js/coreui-utilities";

import { FormattedMessage } from "react-intl";

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchTodos } from '../../actions/action_todo';
import { fetchStaffs } from '../../actions/action_staff';
import { fetchPermissions } from '../../actions/action_permission';
import { fetchGroups } from '../../actions/action_group';

import { Redirect } from "react-router-dom";

// const Widget03 = lazy(() => import("../../views/Widgets/Widget03"));

const brandPrimary = getStyle("--primary");
// const brandSuccess = getStyle("--success");
const brandInfo = getStyle("--info");
// const brandWarning = getStyle("--warning");
// const brandDanger = getStyle("--danger");

// Card Chart 1
const cardChartData1 = {
  labels: ["January", "February", "March", "April", "May", "June", "July"],
  datasets: [
    {
      label: "My First dataset",
      backgroundColor: brandPrimary,
      borderColor: "rgba(255,255,255,.55)",
      data: [65, 59, 84, 84, 51, 55, 40]
    }
  ]
};

const cardChartOpts1 = {
  tooltips: {
    enabled: false,
    custom: CustomTooltips
  },
  maintainAspectRatio: false,
  legend: {
    display: false
  },
  scales: {
    xAxes: [
      {
        gridLines: {
          color: "transparent",
          zeroLineColor: "transparent"
        },
        ticks: {
          fontSize: 2,
          fontColor: "transparent"
        }
      }
    ],
    yAxes: [
      {
        display: false,
        ticks: {
          display: false,
          min: Math.min.apply(Math, cardChartData1.datasets[0].data) - 5,
          max: Math.max.apply(Math, cardChartData1.datasets[0].data) + 5
        }
      }
    ]
  },
  elements: {
    line: {
      borderWidth: 1
    },
    point: {
      radius: 4,
      hitRadius: 10,
      hoverRadius: 4
    }
  }
};

// Card Chart 2
const cardChartData2 = {
  labels: ["January", "February", "March", "April", "May", "June", "July"],
  datasets: [
    {
      label: "My First dataset",
      backgroundColor: brandInfo,
      borderColor: "rgba(255,255,255,.55)",
      data: [1, 18, 9, 17, 34, 22, 11]
    }
  ]
};

const cardChartOpts2 = {
  tooltips: {
    enabled: false,
    custom: CustomTooltips
  },
  maintainAspectRatio: false,
  legend: {
    display: false
  },
  scales: {
    xAxes: [
      {
        gridLines: {
          color: "transparent",
          zeroLineColor: "transparent"
        },
        ticks: {
          fontSize: 2,
          fontColor: "transparent"
        }
      }
    ],
    yAxes: [
      {
        display: false,
        ticks: {
          display: false,
          min: Math.min.apply(Math, cardChartData2.datasets[0].data) - 5,
          max: Math.max.apply(Math, cardChartData2.datasets[0].data) + 5
        }
      }
    ]
  },
  elements: {
    line: {
      tension: 0.00001,
      borderWidth: 1
    },
    point: {
      radius: 4,
      hitRadius: 10,
      hoverRadius: 4
    }
  }
};

// Card Chart 3
const cardChartData3 = {
  labels: ["January", "February", "March", "April", "May", "June", "July"],
  datasets: [
    {
      label: "My First dataset",
      backgroundColor: "rgba(255,255,255,.2)",
      borderColor: "rgba(255,255,255,.55)",
      data: [78, 81, 80, 45, 34, 12, 40]
    }
  ]
};

const cardChartOpts3 = {
  tooltips: {
    enabled: false,
    custom: CustomTooltips
  },
  maintainAspectRatio: false,
  legend: {
    display: false
  },
  scales: {
    xAxes: [
      {
        display: false
      }
    ],
    yAxes: [
      {
        display: false
      }
    ]
  },
  elements: {
    line: {
      borderWidth: 2
    },
    point: {
      radius: 0,
      hitRadius: 10,
      hoverRadius: 4
    }
  }
};

// Card Chart 4
const cardChartData4 = {
  labels: ["", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""],
  datasets: [
    {
      label: "My First dataset",
      backgroundColor: "rgba(255,255,255,.3)",
      borderColor: "transparent",
      data: [78, 81, 80, 45, 34, 12, 40, 75, 34, 89, 32, 68, 54, 72, 18, 98]
    }
  ]
};

const cardChartOpts4 = {
  tooltips: {
    enabled: false,
    custom: CustomTooltips
  },
  maintainAspectRatio: false,
  legend: {
    display: false
  },
  scales: {
    xAxes: [
      {
        display: false,
        barPercentage: 0.6
      }
    ],
    yAxes: [
      {
        display: false
      }
    ]
  }
};

// Main Chart

//Random Numbers
function random(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

var elements = 27;
var data1 = [];
var data2 = [];
var data3 = [];

for (var i = 0; i <= elements; i++) {
  data1.push(random(50, 200));
  data2.push(random(80, 100));
  data3.push(65);
}


class Dashboard extends Component {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.onRadioBtnClick = this.onRadioBtnClick.bind(this);

    this.state = {
      dropdownOpen: false,
      radioSelected: 2,
      groupData: [],
      permissionData: [],
      staffData: [],
      todoData: [],
    };
  }


  componentWillMount() {
    if (sessionStorage.getItem("userData")) {

      //Group Data
      this.props.fetchGroups()
        .then((response) => {
          this.setState({ groupData: response.data.payload });
        }).catch(err => {
          console.log(err);
        });

      //Permission Data
      this.props.fetchPermissions("?size=1000")
        .then((response) => {
          this.setState({ permissionData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

      //ToDo Data
      this.props.fetchTodos()
        .then(result => {
          this.setState({ todoData: result.data.payload });
        }, error => {
          console.log(error);
          this.setState({ currentError: error });
        }
        )

      //Staff Data
      this.props.fetchStaffs("?size=1000")
        .then((response) => {
          this.setState({ staffData: response.data.payload });
        }).catch(err => {
          console.log(err);
        })

    } else {
      this.setState({ redirectToReferrer: true });
    }

  }

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  onRadioBtnClick(radioSelected) {
    this.setState({
      radioSelected: radioSelected
    });
  }

  loading = () => (
    <div className="animated fadeIn pt-1 text-center">Loading...</div>
  );

  render() {
    if (this.state.redirectToReferrer) {
      return <Redirect to={"/login"} />;
    }
    return (
      <div className="animated fadeIn">
        <Row>
          <Col xs="12" sm="6" lg="6">
            <Link to='/staffs'>
              <Card className="text-white bg-secondary">
                <CardBody className="pb-0">
                  <div className="text-value">{this.state.staffData ? this.state.staffData.length : "0"}</div>
                  <div><FormattedMessage id="Authorized Staffs" defaultMessage="Authorized Staffs" /></div>
                </CardBody>
                <div className="chart-wrapper mx-3" style={{ height: "70px" }}>
                  <Line
                    data={cardChartData2}
                    options={cardChartOpts2}
                    height={70}
                  />
                </div>
              </Card>
            </Link>
          </Col>

          <Col xs="12" sm="6" lg="6">
            <Link to='/groups'>
              <Card className="text-white bg-primary">
                <CardBody className="pb-0">
                  <div className="text-value">{this.state.groupData ? this.state.groupData.length : "0"}</div>
                  <div><FormattedMessage id="Staff Groups" defaultMessage="Staff Groups" /></div>
                </CardBody>
                <div className="chart-wrapper mx-3" style={{ height: "70px" }}>
                  <Line
                    data={cardChartData1}
                    options={cardChartOpts1}
                    height={70}
                  />
                </div>
              </Card>
            </Link>
          </Col>

          <Col xs="12" sm="6" lg="6">
            <Link to='/permissions'>
              <Card className="text-white bg-secondary">
                <CardBody className="pb-0">
                  <div className="text-value">{this.state.permissionData ? this.state.permissionData.length : "0"}</div>
                  <div><FormattedMessage id="App Permissions" defaultMessage="App Permissions" /></div>
                </CardBody>
                <div className="chart-wrapper" style={{ height: "70px" }}>
                  <Line
                    data={cardChartData3}
                    options={cardChartOpts3}
                    height={70}
                  />
                </div>
              </Card>
            </Link>
          </Col>

          <Col xs="12" sm="6" lg="6">
            <Link to='/todos'>
              <Card className="text-white bg-danger">
                <CardBody className="pb-0">
                  <div className="text-value">{this.state.todoData ? this.state.todoData.length : "0"}</div>
                  <div><FormattedMessage id="Pending Authorizations" defaultMessage="Pending Authorizations" /></div>
                </CardBody>
                <div className="chart-wrapper mx-3" style={{ height: "70px" }}>
                  <Bar
                    data={cardChartData4}
                    options={cardChartOpts4}
                    height={70}
                  />
                </div>
              </Card>
            </Link>
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  console.log('State is ', state)
  return {
    todo: state.todo,
    staff: state.staff,
    group: state.group,
    permission: state.permission,
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchTodos,
    fetchGroups,
    fetchPermissions,
    fetchStaffs
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
// export default Dashboard;
