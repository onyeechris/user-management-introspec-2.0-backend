import React from "react";
import { Field, reduxForm } from 'redux-form';
import {
    Button,
    Col,
    Input,
    Label,
    ModalFooter,
    FormFeedback,
    FormGroup,
} from "reactstrap";
import { FormattedMessage } from "react-intl";

const required = value => value ? undefined : 'Required';

const minLength = min => value => {
    return value && value.length < min ? `Must be ${min} characters or more` : undefined
}

const groupName = minLength(6);

const translate = (pageString) => {
    return (
        <FormattedMessage id={pageString} defaultMessage={pageString} />
    )
}

const renderField = ({ input, label, type, meta: { touched, error, warning } }) => (
    <div>
        <Input
            type={type}
            placeholder={label}
            {...input}
        />
        {touched && error && <span><FormFeedback className="help-block" style={{ 'display': 'block' }}>{error}</FormFeedback></span>}
    </div>
)

let CreateModuleForm = (props) => {
    const { handleSubmit, toggle, submitting } = props
    return (
        <form onSubmit={handleSubmit} className="form-horizontal">

            <FormGroup row>
                <Col md="3">
                    <Label htmlFor="name">{translate("Name")} <span style={{ color: 'red' }}>*</span></Label>
                </Col>
                <Col xs="12" md="9">
                    <Field name="name" type="text"
                        component={renderField}
                        label="Name"
                        validate={[required, groupName]}
                    />
                </Col>
            </FormGroup>

            <FormGroup row>
                <Col md="3">
                    <Label htmlFor="code">{translate("Code")}  <span style={{ color: 'red' }}>*</span></Label>
                </Col>
                <Col xs="12" md="9">
                    <Field name="code" type="text"
                        component={renderField}
                        label="Code"
                        validate={[required]}
                    />
                </Col>
            </FormGroup>

            <FormGroup row>
                <Col md="3">
                    <Label htmlFor="description">{translate("Description")} </Label>
                </Col>
                <Col xs="12" md="9">
                    <Field name="description" type="text"
                        component={renderField}
                        label="Description"
                    />
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

CreateModuleForm = reduxForm({
    form: 'CreateModule',
    destroyOnUnmount: false
})(CreateModuleForm);
export default CreateModuleForm;
