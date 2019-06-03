import React from "react";
import { Field, reduxForm } from 'redux-form';
import {
  Button,
  Col,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row
} from "reactstrap";
import { FormattedMessage } from "react-intl";

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

const renderField = ({ input, label, type, meta: { touched, error, warning } }) => (
  <div>
    <Input
      type={type}
      placeholder={label}
      {...input}
    />
    {/* <input {...input} placeholder={label} type={type} /> */}
    {touched && ((error && <span>{error}</span>) || (warning && <span>{warning}</span>))}
  </div>
)

let LoginForm = (props) => {
  const { handleSubmit, pristine, reset, submitting } = props
  return (
    <form onSubmit={handleSubmit}>
      <InputGroup className="mb-3">
        <InputGroupAddon addonType="prepend">
          <InputGroupText>
            <i className="icon-user" />
          </InputGroupText>
        </InputGroupAddon>
        <Field name="username" type="text"
          component={renderField}
          label="Username"
          validate={[required, email]}
        />
      </InputGroup>
      <InputGroup className="mb-4">
        <InputGroupAddon addonType="prepend">
          <InputGroupText>
            <i className="icon-lock" />
          </InputGroupText>
        </InputGroupAddon>
        <Field name="password" type="password"
          component={renderField} label="Password"
          validate={password}
        />
      </InputGroup>
      <Row>
        <Col xs="6">
          <Button
            type="submit"
            color="primary"
            className="px-4"
            disabled={submitting}
          >
            <FormattedMessage id="Login" defaultMessage="Login" />
          </Button>
        </Col>
        <Col xs="6">
          <Button
            type="submit"
            color="default"
            className="px-4"
            disabled={pristine || submitting} onClick={reset}
          >
            <FormattedMessage id="Clear Values" defaultMessage="Clear Values" />
          </Button>
        </Col>
      </Row>
      {/* <div>
        <button type="submit" disabled={submitting}>Submit</button>
        <button type="button" disabled={pristine || submitting} onClick={reset}>Clear Values</button>
      </div> */}
    </form>
  )
}

LoginForm = reduxForm({
  form: 'login',
  destroyOnUnmount: false
})(LoginForm);
export default LoginForm;
