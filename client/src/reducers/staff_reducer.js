import { STAFFS_FETCHED, STAFFS_FETCH_ERROR, STAFF_FETCHED, STAFF_FETCH_ERROR, STAFF_DELETED, STAFF_DELETE_ERROR, STAFF_CREATED, STAFF_CREATE_ERROR, STAFF_UPDATED, STAFF_UPDATE_ERROR } from '../actions/action_staff';

const staff = (state = {}, action) => {
  switch (action.type) {
    case STAFFS_FETCHED:
      return { ...state, staffsFetched: action.payload }
    case STAFFS_FETCH_ERROR:
      return { ...state, staffsFetchError: action.payload }
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
    default:
      return state
  }
}

export default staff;