import React from "react";
import { Field, reduxForm } from 'redux-form';
import {
  Button,
  Col,
  Input,
  // FormText,
  Label,
  ModalFooter,
  FormFeedback,
  FormGroup,
  // Form
} from "reactstrap";
import { FormattedMessage } from "react-intl";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const required = value => value ? undefined : 'Required';
// const minLength = min => value => {
//   return value && value.length < min ? `Must be ${min} characters or more` : undefined
// }
// const password = minLength(6);
const email = value => {
  return value && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value) ? 'Invalid email address' : undefined
}

const renderField = ({ input, label, type, staffValue, meta: { touched, error, warning } }) => (
  <div>
    <Input
      type={type}
      placeholder={label}
      value={staffValue}
      {...input}
    />
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

const renderDateField = ({ input, currentDate, handleChange, meta: { touched, error, warning } }) => (
  <div>
    <DatePicker
      selected={currentDate}
      onChange={handleChange}
      className="form-control"
      placeholderText="Select Date"
      dateFormat="MM/dd/yyyy"
    />
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

const renderGroupDropdown = ({ input, type, staffGroup, groupValue, meta: { touched, error, warning } }) => (
  <div>
    <Input type={type} id="select" {...input}>
      {staffGroup.map(function (item, key) {
        if (item.id === groupValue) {
          return (
            <option value={groupValue} defaultValue>{item.name}</option>
          )
        } else {
          return (
            <option value={item.id} key={key}>{item.name}</option>
          )
        }
      })}
    </Input>
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

const renderRoleDropdown = ({ input, roleValue, label, type, userRole, id, meta: { touched, error, warning } }) => (
  <div>
    <Input type={type} id={id} {...input}>
      <option value={roleValue}>{roleValue}</option>
      <option value="MAKER">Initiator</option>
      <option value="CHECKER">Authorizer</option>
      <option value="NONE">None</option>
    </Input>
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

let UpdateStaffForm = (props) => {
  const { handleSubmit, singleStaff, staffGroup, toggle, submitting, currentDate, handleChange } = props;
  console.log(singleStaff);
  return (
    <form onSubmit={handleSubmit} className="form-horizontal">
      <Field name="id" type="hidden"
        component={renderField}
        staffValue={singleStaff.id}
      />
      {/* <Input type="hidden" name="id" value={singleStaff.id} onChange={this.readUpdateValue.bind(this, 'id')} />
        <p style={{ color: 'red' }}>{this.state.formError}</p> */}
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="firstName">First Name <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="first_name" type="text"
            component={renderField}
            label="First Name"
            staffValue={singleStaff.first_name}
            validate={[required]}
          />
          {/* <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
              onChange={this.readUpdateValue.bind(this, 'first_name')} value={singleStaff.first_name}
            /> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="lastName">Last Name</Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="last_name" type="text"
            component={renderField}
            label="Last Name"
            staffValue={singleStaff.last_name}
          />
          {/* <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
              onChange={this.readUpdateValue.bind(this, 'last_name')} value={singleStaff.last_name}
            /> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="email-input">Email <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="email" type="email"
            component={renderField}
            label="Email"
            staffValue={singleStaff.email}
            validate={[required, email]}
          />
          {/* <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
              onChange={this.readUpdateValue.bind(this, 'email')} value={singleStaff.email}
            /> */}
        </Col>
      </FormGroup>
      {/* <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password">Password <span style={{ color: 'red' }}>*</span></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="password" id="password" name="password" placeholder="Your Password" autoComplete="email"
                        onChange={this.readUpdateValue.bind(this, 'password')} value={singleStaff.password}
                      />
                    </Col>
                  </FormGroup> */}
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="phone">Phone</Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="phone" type="number"
            component={renderField}
            label="Phone Number"
            staffValue={singleStaff.phone}
            validate={[required]}
          />
          {/* <Input type="text" id="phone" name="phone" placeholder="Enter Phone Number"
              onChange={this.readUpdateValue.bind(this, 'phone')} value={singleStaff.phone}
            /> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="date-hire">Hire Date <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="hire_date"
            component={renderDateField}
            currentDate={currentDate}
            handleChange={handleChange}
            staffValue={singleStaff.hire_date}
          />
          {/* <DatePicker
              selected={this.state.startDate}
              onChange={this.handleDateChange}
              className="form-control" placeholderText="Select Date"
              // dateFormat="LL"
              dateFormat="MM/dd/yyyy"
              value={singleStaff.hire_date}
            /> */}
          {/* <Input type="text" id="date-hire" name="hire_date" placeholder="Date MM/dd/yyyy"
                        onChange={this.readUpdateValue.bind(this, 'hire_date')} value={singleStaff.hire_date}
                      /> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="select">Group  <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="group_id" type="select"
            component={renderGroupDropdown}
            label="Group"
            validate={[required]}
            staffGroup={staffGroup}
            groupValue={singleStaff.group_id}
          />
          {/* <Input type="select" name="group_id" id="group_id"
              onChange={this.readUpdateValue.bind(this, 'group_id')}
            >
              {staffGroup.map(function (groupItem, groupKey) {
                if (groupItem.id === singleStaff.group_id) {
                  return (
                    <option value={singleStaff.group_id} defaultValue>{groupItem.name}</option>
                  )
                } else {
                  return (
                    <option value={groupItem.id} key={groupKey}>{groupItem.name}</option>
                  )
                }
              })}
            </Input> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label>Role <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col md="9">
          <Field name="maker_checker" type="select"
            component={renderRoleDropdown}
            roleValue={singleStaff.maker_checker}
            id="maker_checker"
          />
          {/* <Input type="select" name="maker_checker" id="maker_checker"
              onChange={this.readUpdateValue.bind(this, 'maker_checker')}
            >
              <option value={singleStaff.maker_checker}>{singleStaff.maker_checker}</option>
              <option value="MAKER">Initiator</option>
              <option value="CHECKER">Authorizer</option>
              <option value="NONE">None</option>
            </Input> */}
        </Col>
      </FormGroup>

      <ModalFooter>
        <Button type="submit" color="primary" disabled={submitting}>
          <FormattedMessage id="Submit" defaultMessage="Submit" />
        </Button>
        {' '}
        <Button color="secondary" onClick={toggle}>
          <FormattedMessage id="Cancel" defaultMessage="Cancel" />
        </Button>
      </ModalFooter>
    </form>
  )
}

UpdateStaffForm = reduxForm({
  form: 'UpdateStaff',
  destroyOnUnmount: false
})(UpdateStaffForm);
export default UpdateStaffForm;
