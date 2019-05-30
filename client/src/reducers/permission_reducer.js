import { PERMISSION_FETCHED, PERMISSION_FETCH_ERROR, PERMISSIONS_FETCHED, PERMISSIONS_FETCH_ERROR, PERMISSION_DELETED, PERMISSION_DELETE_ERROR, PERMISSION_CREATED, PERMISSION_CREATE_ERROR, PERMISSION_UPDATED, PERMISSION_UPDATE_ERROR } from '../actions/action_permission';

const permission = (state = {}, action) => {
  switch (action.type) {
    case PERMISSION_FETCHED:
      return { ...state, permissionFetched: action.payload }
    case PERMISSION_FETCH_ERROR:
      return { ...state, permissionFetchError: action.payload }
    case PERMISSIONS_FETCHED:
      return { ...state, permissionsFetched: action.payload }
    case PERMISSIONS_FETCH_ERROR:
      return { ...state, permissionsFetchError: action.payload }
    case PERMISSION_DELETED:
      return { ...state, permissionDeleted: action.payload }
    case PERMISSION_DELETE_ERROR:
      return { ...state, permissionDeleteError: action.payload }
    case PERMISSION_CREATED:
      return { ...state, permissionCreated: action.payload }
    case PERMISSION_CREATE_ERROR:
      return { ...state, permissionCreateError: action.payload }
    case PERMISSION_UPDATED:
      return { ...state, permissionUpdated: action.payload }
    case PERMISSION_UPDATE_ERROR:
      return { ...state, permissionUpdateError: action.payload }
    default:
      return state
  }
}

export default permission;