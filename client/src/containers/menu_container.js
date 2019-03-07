import { connect } from 'react-redux';
import { fetchUser } from '../actions/action_login';
import { bindActionCreators } from 'redux';

import LoginComponent from '../views/Pages/Login/Login';

const mapStateToProps = state => {
    return {
        profile: state.profile
    };
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        actions: bindActionCreators({ fetchUser }, dispatch)
    }
}

const Profile = connect(mapStateToProps, mapDispatchToProps)(LoginComponent);

export default Profile;
