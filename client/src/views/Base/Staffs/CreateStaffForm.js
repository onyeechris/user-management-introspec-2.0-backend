import React from "react";
import { Field, reduxForm } from 'redux-form';
import {
  Button,
  Col,
  Input, Label,
  ModalFooter,
  FormFeedback,
  FormGroup,
} from "reactstrap";
import { FormattedMessage } from "react-intl";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const translate = (pageString) => {
  return (
    <FormattedMessage id={pageString} defaultMessage={pageString} />
  )
}

let passKey = '';

const updatePass = (value) => {
  console.log("got here");
  if (value) {
    console.log(value);
    passKey = value;
  }
}

const required = value => value ? undefined : 'Required';
// const maxLength = max => value =>
//   value && value.length > max ? `Must be ${max} characters or less` : undefined
const minLength = min => value => {
  return value && value.length < min ? `Must be ${min} characters or more` : undefined
}

const passwordCheck = value => {
  return value !== passKey ? `Password does not match!` : undefined
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

const renderPasswordField = ({ input, label, type, meta: { touched, error, warning } }) => (
  <div>
    <Input
      type={type}
      placeholder={label}
      // onChange={e => updatePass(e)}
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
    // {...input}
    />
    {/* <Input type='hidden' value={currentDate} /> */}
    {/* <input {...input} placeholder={label} type={type} /> */}
    {/* {touched && ((error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>) || (warning && <span>{warning}</span>))} */}
    {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

  </div>
)

// const renderGroupDropdown = ({ input, type, staffGroup, meta: { touched, error, warning } }) => (
//   <div>
//     <Input type={type} id="select" {...input}>
//       {/* <option value="0">Introspec Default</option> */}
//       {staffGroup.map(function (item, key) {
//         return (
//           <option value={item.id} key={key}>{item.name}</option>
//         )
//       })}
//     </Input>
//     {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}

//   </div>
// )

// const renderRoleRadio = ({ input, label, name, type, userRole, id, meta: { touched, error, warning } }) => (
//   <div>
//     {/* <Input className="form-check-input" type={type} name={name} id={id} value={userRole} /> */}
//     {/* <Label check className="form-check-label" htmlFor={id}>{label}</Label> */}
//     <label className="form-check-input">
//       <input type="radio" value={userRole} name="user_type" {...input} /> <span> {label}</span>
//     </label>
//     {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}
//     <br />
//   </div>
// )

let CreateStaffForm = (props) => {
  const { handleSubmit,
    // staffGroup, 
    toggle, submitting, currentDate, handleChange, updateValue } = props
  return (
    <form onSubmit={handleSubmit} className="form-horizontal">
      <FormGroup row>
        <Col md="3">
          <Label htmlFor="firstName">{translate("First Name")} <span style={{ color: 'red' }}>*</span></Label>
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
          <Label htmlFor="lastName">{translate("Last Name")}</Label>
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
          <Label htmlFor="email-input">{translate("Email")} <span style={{ color: 'red' }}>*</span></Label>
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
          <Label htmlFor="password_check">{translate("Password")} <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          {/* <Input type="password" id="password" name="password" placeholder="Password" required
                        onChange={this.updateValue.bind(this, 'password')}
                      /> */}
          <Field name="password_check" type="password"
            component={renderPasswordField}
            label="Password"
            validate={[password, updatePass]}
          />
          {/* <FormText className="help-block">Choose a strong password</FormText> */}
        </Col>
      </FormGroup>

      <FormGroup row>
        <Col md="3">
          <Label htmlFor="password">{translate("Confirm Password")} <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="password" type="password"
            component={renderField}
            label="Confirm Password"
            validate={[required, passwordCheck]}
          />
        </Col>
      </FormGroup>

      <FormGroup row>
        <Col md="3">
          <Label htmlFor="phone">{translate("Phone")} </Label>
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
          <Label htmlFor="date-hire">{translate("Hire Date")}</Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="hire_date"
            component={renderDateField}
            currentDate={currentDate}
            handleChange={handleChange}
          // validate={[required]}
          />
        </Col>
      </FormGroup>

      {/* <FormGroup row>
        <Col md="3">
          <Label htmlFor="select">{translate("Group")}  <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col xs="12" md="9">
          <Field name="group_id" type="select"
            component={renderGroupDropdown}
            label="Group"
            validate={[required]}
            staffGroup={staffGroup}
          />
        </Col>
      </FormGroup> */}

      {/* <FormGroup row>
        <Col md="3">
          <Label>{translate("Role")} <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col md="9">
          <FormGroup check className="radio">
            <Field type="radio"
              name="user_type"
              // component="input"
              component={renderRoleRadio}
              id="radio1"
              label="Admin"
              userRole="ADMIN"
            />
          </FormGroup>
          <FormGroup check className="radio">
            <Field type="radio"
              name="user_type"
              // component="input"
              component={renderRoleRadio}
              id="radio2"
              label="User"
              userRole="USER"
            // checked={true}
            />
          </FormGroup>
        </Col>
      </FormGroup> */}


      <FormGroup row>
        <Col md="3">
          <Label>Role <span style={{ color: 'red' }}>*</span></Label>
        </Col>
        <Col md="9">
          <FormGroup check className="radio">
            <Input className="form-check-input" type="radio" id="radio1" name="user_type" value="ADMIN" onChange={updateValue.bind(this, 'user_type')} />
            <Label check className="form-check-label" htmlFor="radio1">Admin</Label>
          </FormGroup>
          <FormGroup check className="radio">
            <Input className="form-check-input" type="radio" id="radio2" name="user_type" value="USER" onChange={updateValue.bind(this, 'user_type')} />
            <Label check className="form-check-label" htmlFor="radio2">User</Label>
          </FormGroup>
          <FormGroup check className="radio">
            <Input className="form-check-input" type="radio" id="radio3" name="user_type" value="DEV" onChange={updateValue.bind(this, 'user_type')} />
            <Label check className="form-check-label" htmlFor="radio3">Dev</Label>
          </FormGroup>
        </Col>
      </FormGroup>
      <ModalFooter>
        <Button
          type="submit"
          color="primary"
          disabled={submitting}
        >
          {translate("Submit")}
        </Button>
        {' '}
        <Button color="secondary" onClick={toggle}>
          {translate("Cancel")}
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
