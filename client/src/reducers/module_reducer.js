import {
  MODULE_FETCHED, MODULE_FETCH_ERROR,
  MODULES_FETCHED, MODULES_FETCH_ERROR,
  MODULE_CREATED, MODULE_CREATE_ERROR,
  MODULE_DELETED, MODULE_DELETE_ERROR,
  MODULE_UPDATED, MODULE_UPDATE_ERROR,
  // USERS_BY_APP_FETCHED, USERS_BY_APP_FETCH_ERROR,
  // GROUPS_BY_APP_FETCHED, GROUPS_BY_APP_FETCH_ERROR,
  // PERMISSIONS_BY_APP_FETCHED, PERMISSIONS_BY_APP_FETCH_ERROR,
  MODULE_SAVED
} from '../actions/action_module';

const module = (state = {}, action) => {
  switch (action.type) {
    case MODULE_FETCHED:
      return { ...state, moduleFetched: action.payload }
    case MODULE_FETCH_ERROR:
      return { ...state, moduleFetchError: action.payload }
    case MODULES_FETCHED:
      return { ...state, modulesFetched: action.payload }
    case MODULES_FETCH_ERROR:
      return { ...state, modulesFetchError: action.payload }
    case MODULE_CREATED:
      return { ...state, moduleCreated: action.payload }
    case MODULE_CREATE_ERROR:
      return { ...state, moduleCreateError: action.payload }
    case MODULE_DELETED:
      return { ...state, moduleDeleted: action.payload }
    case MODULE_DELETE_ERROR:
      return { ...state, moduleDeleteError: action.payload }
    case MODULE_UPDATED:
      return { ...state, moduleUpdated: action.payload }
    case MODULE_UPDATE_ERROR:
      return { ...state, moduleUpdateError: action.payload }
    // case USERS_BY_APP_FETCHED:
    //   return { ...state, usersByAppFetched: action.payload }
    // case USERS_BY_APP_FETCH_ERROR:
    //   return { ...state, usersByAppFetchError: action.payload }
    // case GROUPS_BY_APP_FETCHED:
    //   return { ...state, groupsByAppFetched: action.payload }
    // case GROUPS_BY_APP_FETCH_ERROR:
    //   return { ...state, groupsByAppFetchError: action.payload }
    // case PERMISSIONS_BY_APP_FETCHED:
    //   return { ...state, permissionsByAppFetched: action.payload }
    // case PERMISSIONS_BY_APP_FETCH_ERROR:
    //   return { ...state, permissionsByAppFetchError: action.payload }
    case MODULE_SAVED:
      return { ...state, moduleSaved: action.payload }
    default:
      return state
  }
}

export default module;