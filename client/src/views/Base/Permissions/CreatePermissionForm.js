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

const permissionName = minLength(4);

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

let CreatePermissionForm = (props) => {
    const { handleSubmit, toggle, submitting } = props
    return (
        <form onSubmit={handleSubmit} className="form-horizontal">
            <FormGroup row>
                <Col md="3">
                    <Label htmlFor="action">{translate("Action")}{' '} <span style={{ color: 'red' }}>*</span></Label>
                </Col>
                <Col xs="12" md="9">
                    <Field name="action" type="text"
                        component={renderField}
                        label="Name"
                        validate={[required, permissionName]}
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

CreatePermissionForm = reduxForm({
    form: 'CreatePermission',
    destroyOnUnmount: false
})(CreatePermissionForm);
export default CreatePermissionForm;
