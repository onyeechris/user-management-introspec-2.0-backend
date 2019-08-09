import React, { Component } from 'react';
import {
  Card,
  CardBody,
  CardHeader,
  Col,
  Form,
  FormGroup,
  FormText,
  Label,
  Row,
} from 'reactstrap';
// import { AppSwitch } from '@coreui/react';
import { setLocale } from '../../../actions/action_locale';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  FormattedMessage,
  // FormattedDate, FormattedTime
} from 'react-intl';

class Settings extends Component {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.toggleFade = this.toggleFade.bind(this);
    this.state = {
      collapse: true,
      fadeIn: true,
      timeout: 300,
      value: 'en'
    };
  }
  storedLanguage = ''

  componentWillMount() {
    // console.log('LocalStorage in settings form',localStorage.se8lementLang);
    this.storedLanguage = localStorage.se8lementLang;
    this.setState({ value: localStorage.se8lementLang });
  }

  toggle() {
    this.setState({ collapse: !this.state.collapse });
  }

  toggleFade() {
    this.setState((prevState) => { return { fadeIn: !prevState } });
  }

  change(event) {
    this.setState({
      value: event.target.value
    })
    this.props.setLocale(event.target.value);
  }

  render() {
    console.log('Language is ', this.storedLanguage);
    return (
      <div className="animated fadeIn">
        <Row>
          <Col xs="12" md="12">
            <Card>
              <CardHeader>
                <strong><FormattedMessage id="AppSettings" defaultMessage="App Settings" /></strong>
              </CardHeader>
              <CardBody>
                <Form action="" method="post" className="form-horizontal">
                  {/* <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="hf-email"><FormattedMessage id="AutoPost Journal" defaultMessage="AutoPost Journal" /></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} defaultChecked label dataOn={'\u2713'} dataOff={'\u2715'} />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="hf-email"><FormattedMessage id="AutoPost Weekend" defaultMessage="AutoPost Weekend" /></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} defaultChecked label dataOn={'\u2713'} dataOff={'\u2715'} />
                    </Col>
                  </FormGroup>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="hf-email"><FormattedMessage id="Don't AutoPost on Conflict" defaultMessage="Don't AutoPost on Conflict" /></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} defaultChecked label dataOn={'\u2713'} dataOff={'\u2715'} />
                    </Col>
                  </FormGroup> */}
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="language"><FormattedMessage id="Language" defaultMessage="Language" /></Label>
                    </Col>
                    <Col xs="12" md="9">
                      <select type="select" className="form-control" name="select" id="language" onChange={this.change.bind(this)} value={this.state.value}>
                        <option value="en" >English</option>
                        <option value="fr" >Français</option>
                        <option value="pt" >Português</option>
                        <option value="es" >Español</option>
                      </select>
                      <FormText className="help-block"><FormattedMessage id="LanguageInfo" defaultMessage="This changes the language of the site. It won't change any text entered by users." /></FormText>
                    </Col>
                  </FormGroup>
                  {/* <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="hf-password">Password</Label>
                    </Col>
                    <Col xs="12" md="9">
                    <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} checked  />
                    <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} checked outline={'alt'} />
                    <AppSwitch className={'mx-1'} variant={'3d'} color={'primary'} defaultChecked label dataOn={'\u2713'} dataOff={'\u2715'} />
                      <Input type="password" id="hf-password" name="hf-password" placeholder="Enter Password..." autoComplete="current-password"/>
                      <FormText className="help-block">Please enter your password</FormText>
                    </Col>
                  </FormGroup> */}
                  {/* <Button color="success">Submit</Button> */}
                </Form>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  console.log('State is ', state)
  return {
    lang: state.locale.lang
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    setLocale
  }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Settings);

