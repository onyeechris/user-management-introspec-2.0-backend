import React from "react";
import { Field, reduxForm } from 'redux-form';
import {
  Button,
  Col,
  Input, FormText, Label,
  ModalFooter,
  FormFeedback,
  FormGroup,
} from "reactstrap";
import { FormattedMessage } from "react-intl";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

// let startDate = '';
// let formatted_date = '';

// const handleChange = (props, hireDate) => {
//   console.log(props.currentDate);
//   startDate = hireDate;
//   formatted_date = (("0" + (hireDate.getMonth() + 1)).slice(-2)) + "/" + ("0" + hireDate.getDate()).slice(-2) + "/" + hireDate.getFullYear();
//   // startDate = formatted_date;
//   console.log(startDate);
//   console.log(formatted_date);
//   props.currentDate = hireDate;
// }

const required = value => value ? undefined : 'Required';
// const maxLength = max => value =>
//   value && value.length > max ? `Must be ${max} characters or less` : undefined
const minLength = min => value => {
  return value && value.length < min ? `Must be ${min} characters or more` : undefined
}
// const maxLength15 = maxLength(15)
const password = minLength(6);
// const number = value => value && isNaN(Number(value)) ? 'Must be a number' : undefined
// const minValue = min => value =>
//   value && value < min ? `Must be at least ${min}` : undefined
// const minValue18 = minValue(18)
const email = value => {
  return value && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value) ? 'Invalid email address' : undefined
}
// const tooOld = value =>
//   value && value > 65 ? 'You might be too old for this' : undefined
// const aol = value =>
//   value && /.+@aol\.com/.test(value) ?
//     'Really? You still use AOL for your email?' : undefined

// const errorStyle = {
//   'color': 'red',
//   'fontSize': '20px'
// }

const renderField = ({ input, label, type, meta: { touched, error, warning } }) => (
  <div>
    <Input
      type={type}
      placeholder={label}
      {...input}
    />
    {/* <input {...input} placeholder={label} type={type} /> */}
    {/* {touched && ((error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>) || (warning && <span>{warning}</span>))} */}
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
    // {...input}
    />
    {/* <Input type='hidden' value={currentDate} /> */}
    {/* <input {...input} placeholder={label} type={type} /> */}
    {/* {touched && ((error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>) || (warning && <span>{warning}</span>))} */}
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

const renderGroupDropdown = ({ input, type, staffGroup, meta: { touched, error, warning } }) => (
  <div>
    <Input type={type} id="select" {...input}>
      {/* <option value="0">Introspec Default</option> */}
      {staffGroup.map(function (item, key) {
        return (
          <option value={item.id} key={key}>{item.name}</option>
        )
      })}
    </Input>
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

const renderRoleRadio = ({ input, label, type, userRole, id, meta: { touched, error, warning } }) => (
  <div>
    <Input className="form-check-input" type={type} id={id} name="maker_checker" value={userRole} {...input} />
    <Label check className="form-check-label" htmlFor={id}>{label}</Label>
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

let CreateStaffForm = (props) => {
  const { handleSubmit, staffGroup, toggle, submitting, currentDate, handleChange } = props
  return (
    <form onSubmit={handleSubmit} className="form-horizontal">
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="firstName"><FormattedMessage id="First Name" defaultMessage="First Name " /> <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="text" id="firstName" name="first_name" placeholder="Enter First Name"
                        onChange={this.updateValue.bind(this, 'first_name')}
                      /> */}
          <Field name="first_name" type="text"
            component={renderField}
            label="First Name"
            validate={[required]}
          />
        </Col>
      </FormGroup>

      <FormGroup row>
        <Col md="3">
          <Label htmlFor="lastName"><FormattedMessage id="Last Name" defaultMessage="Last Name " /></Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="text" id="lastName" name="last_name" placeholder="Enter Last Name" required
                        onChange={this.updateValue.bind(this, 'last_name')}
                      /> */}
          <Field name="last_name" type="text"
            component={renderField}
            label="Last Name"
            validate={[required]}
          />
        </Col>
      </FormGroup>

      <FormGroup row>
        <Col md="3">
          <Label htmlFor="email-input"><FormattedMessage id="Email" defaultMessage="Email " /> <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="email" id="email-input" name="email" placeholder="Enter Email" autoComplete="email"
                        onChange={this.updateValue.bind(this, 'email')}
                      /> */}
          <Field name="email" type="text"
            component={renderField}
            label="Email"
            validate={[required, email]}
          />
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="password"><FormattedMessage id="Password" defaultMessage="Password " /> <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="password" id="password" name="password" placeholder="Password" required
                        onChange={this.updateValue.bind(this, 'password')}
                      /> */}
          <Field name="password" type="password"
            component={renderField}
            label="Password"
            validate={[required, password]}
          />
          <FormText className="help-block">Choose a strong password</FormText>
        </Col>
      </FormGroup>

      <FormGroup row>
        <Col md="3">
          <Label htmlFor="phone"><FormattedMessage id="Phone" defaultMessage="Phone " /> </Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="number" size="11" id="phone" name="phone" placeholder="Enter Phone Number"
                        onChange={this.updateValue.bind(this, 'phone')}
                      /> */}
          <Field name="phone" type="number"
            component={renderField}
            label="Phone"
          // validate={[required]}
          />
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="date-hire"><FormattedMessage id="Hire Date" defaultMessage="Hire Date " /> <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="hire_date"
            component={renderDateField}
            currentDate={currentDate}
            handleChange={handleChange}
          // validate={[required]}
          />
          {/* <DatePicker
            selected={currentDate}
            onChange={handleChange}
            className="form-control"
            placeholderText="Select Date"
            dateFormat="MM/dd/yyyy"
            name="hire_date"
          /> */}
          {/* <DatePicker
                selected={this.state.startDate}
                onChange={this.handleChange}
                className="form-control" placeholderText="Select Date"
                dateFormat="MM/dd/yyyy"
              /> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="select"><FormattedMessage id="Group" defaultMessage="Group " />   <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="group_id" type="select"
            component={renderGroupDropdown}
            label="Group"
            validate={[required]}
            staffGroup={staffGroup}
          />
          {/* <Input type="select" name="group_id" id="select" {...input}>
            <option value="0">Introspec Default</option>
            {staffGroup.map(function (item, key) {
              return (
                <option value={item.id} key={key}>{item.name}</option>
              )
            })}
          </Input> */}
        </Col>
      </FormGroup>
      <FormGroup row>
        <Col md="3">
          <Label><FormattedMessage id="Role" defaultMessage="Role " /> <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col md="9">
          <FormGroup check className="radio">
            {/* <Input className="form-check-input" type="radio" id="radio1" name="maker_checker" value="MAKER" onChange={this.updateValue.bind(this, 'maker_checker')} /> */}
            {/* <Input className="form-check-input" type="radio" id="radio1" name="maker_checker" value="MAKER" />
            <Label check className="form-check-label" htmlFor="radio1">Initiator</Label> */}
            <Field name="maker_checker" type="radio"
              component={renderRoleRadio}
              id="radio1"
              label="Initiator"
              userRole="MAKER"
            />
          </FormGroup>
          <FormGroup check className="radio">
            {/* <Input className="form-check-input" type="radio" id="radio2" name="maker_checker" value="CHECKER" onChange={this.updateValue.bind(this, 'maker_checker')} /> */}
            {/* <Input className="form-check-input" type="radio" id="radio2" name="maker_checker" value="CHECKER" />
            <Label check className="form-check-label" htmlFor="radio2">Authorizer</Label> */}
            <Field name="maker_checker" type="radio"
              component={renderRoleRadio}
              id="radio2"
              label="Authorizer"
              userRole="CHECKER"
            />
          </FormGroup>
          {/* <FormGroup check className="radio"> */}
          {/* <Input className="form-check-input" type="radio" id="radio3" name="maker_checker" value="NONE" onChange={this.updateValue.bind(this, 'maker_checker')} /> */}
          {/* <Input className="form-check-input" type="radio" id="radio3" name="maker_checker" value="NONE" selected />
            <Label check className="form-check-label" htmlFor="radio3">None</Label> */}
          {/* <Field name="maker_checker" type="radio"
              component={renderRoleRadio}
              id="radio3"
              label="None"
              userRole="NONE"
            />
          </FormGroup> */}
        </Col>
      </FormGroup>
      <ModalFooter>
        {/* <Button color="primary" onClick={this.createStaff}>Submit</Button> */}
        <Button
          type="submit"
          color="primary"
          disabled={submitting}
        >
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

CreateStaffForm = reduxForm({
  form: 'CreateStaff',
  destroyOnUnmount: false
})(CreateStaffForm);
export default CreateStaffForm;
