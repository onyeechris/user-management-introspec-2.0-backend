import { USER_FETCHED } from '../actions/action_login';

const profile = (state = {}, action) => {
  switch (action.type) {
    case USER_FETCHED:
      return action.payload
    default:
      return state
  }
}

export default profile;