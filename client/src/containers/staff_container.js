import { connect } from 'react-redux';
import { fetchStaff } from '../actions/action_login';
import { bindActionCreators } from 'redux';

import TableComponent from '../views/Base/Tables/Tables';

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

const Staff = connect(mapStateToProps, mapDispatchToProps)(TableComponent);

export default Staff;
