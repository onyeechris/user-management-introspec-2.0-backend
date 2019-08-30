import {
  STAFFS_FETCHED, STAFFS_FETCH_ERROR,
  ALL_STAFFS_FETCHED, ALL_STAFFS_FETCH_ERROR,
  STAFF_FETCHED, STAFF_FETCH_ERROR,
  STAFF_DELETED, STAFF_DELETE_ERROR,
  STAFF_CREATED, STAFF_CREATE_ERROR,
  STAFF_UPDATED, STAFF_UPDATE_ERROR,
  APP_USERS_FETCHED, APP_USERS_FETCH_ERROR,
  APP_USER_FETCHED, APP_USER_FETCH_ERROR,
  APP_USER_DELETED, APP_USER_DELETE_ERROR,
  USER_ADDED, USER_ADD_ERROR
} from '../actions/action_staff';

const staff = (state = {}, action) => {
  switch (action.type) {
    case STAFFS_FETCHED:
      return { ...state, staffsFetched: action.payload }
    case STAFFS_FETCH_ERROR:
      return { ...state, staffsFetchError: action.payload }
    case ALL_STAFFS_FETCHED:
      return { ...state, allStaffsFetched: action.payload }
    case ALL_STAFFS_FETCH_ERROR:
      return { ...state, allStaffsFetchError: action.payload }
    case STAFF_FETCHED:
      return { ...state, staffFetched: action.payload }
    case STAFF_FETCH_ERROR:
      return { ...state, staffFetchError: action.payload }
    case STAFF_DELETED:
      return { ...state, staffDeleted: action.payload }
    case STAFF_DELETE_ERROR:
      return { ...state, staffDeleteError: action.payload }
    case STAFF_CREATED:
      return { ...state, staffCreated: action.payload }
    case STAFF_CREATE_ERROR:
      return { ...state, staffCreateError: action.payload }
    case STAFF_UPDATED:
      return { ...state, staffUpdated: action.payload }
    case STAFF_UPDATE_ERROR:
      return { ...state, staffUpdateError: action.payload }
    case APP_USERS_FETCHED:
      return { ...state, appUsersFetched: action.payload }
    case APP_USERS_FETCH_ERROR:
      return { ...state, appUsersFetchError: action.payload }
    case APP_USER_FETCHED:
      return { ...state, appUserFetched: action.payload }
    case APP_USER_FETCH_ERROR:
      return { ...state, appUserFetchError: action.payload }
    case APP_USER_DELETED:
      return { ...state, appUsersDeleted: action.payload }
    case APP_USER_DELETE_ERROR:
      return { ...state, appUsersDeleteError: action.payload }
    case USER_ADDED:
      return { ...state, userAdded: action.payload }
    case USER_ADD_ERROR:
      return { ...state, userAddError: action.payload }
    default:
      return state
  }
}

export default staff;