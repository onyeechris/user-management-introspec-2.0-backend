import { connect } from 'react-redux';
import { fetchStaff } from '../actions/action_staff';
import { bindActionCreators } from 'redux';

import StaffComponent from '../views/Base/Staffs/Staffs';

const mapStateToProps = state => {
    return {
        staff: state.staff
    };
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        actions: bindActionCreators({ fetchStaff }, dispatch)
    }
}

const Staff = connect(mapStateToProps, mapDispatchToProps)(StaffComponent);

export default Staff;
