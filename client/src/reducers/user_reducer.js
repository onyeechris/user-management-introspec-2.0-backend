import { USER_FETCHED, USER_FETCH_ERROR } from '../actions/action_login';

const profile = (state = {}, action) => {
  switch (action.type) {
    case USER_FETCHED:
      return { ...state, userFetched: action.payload }
    case USER_FETCH_ERROR:
      return { ...state, userFetchError: action.payload }
    default:
      return state
  }
}

export default profile;