import { STAFF_FETCHED } from '../actions/action_staff';

const staff = (state = {}, action) => {
  switch (action.type) {
    case STAFF_FETCHED:
      return action.payload
    default:
      return state
  }
}

export default staff;