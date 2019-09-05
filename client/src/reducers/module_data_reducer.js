import {
  USERS_BY_APP_FETCHED, USERS_BY_APP_FETCH_ERROR,
  GROUPS_BY_APP_FETCHED, GROUPS_BY_APP_FETCH_ERROR,
  PERMISSIONS_BY_APP_FETCHED, PERMISSIONS_BY_APP_FETCH_ERROR,
} from '../actions/action_module_data';

const module = (state = {}, action) => {
  switch (action.type) {
    case USERS_BY_APP_FETCHED:
      return { ...state, usersByAppFetched: action.payload }
    case USERS_BY_APP_FETCH_ERROR:
      return { ...state, usersByAppFetchError: action.payload }
    case GROUPS_BY_APP_FETCHED:
      return { ...state, groupsByAppFetched: action.payload }
    case GROUPS_BY_APP_FETCH_ERROR:
      return { ...state, groupsByAppFetchError: action.payload }
    case PERMISSIONS_BY_APP_FETCHED:
      return { ...state, permissionsByAppFetched: action.payload }
    case PERMISSIONS_BY_APP_FETCH_ERROR:
      return { ...state, permissionsByAppFetchError: action.payload }
    default:
      return state
  }
}

export default module;