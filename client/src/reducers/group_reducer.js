import { GROUP_FETCHED, GROUP_FETCH_ERROR, GROUPS_FETCHED, GROUPS_FETCH_ERROR, GROUP_CREATED, GROUP_CREATE_ERROR, GROUP_DELETED, GROUP_DELETE_ERROR, GROUP_UPDATED, GROUP_UPDATE_ERROR } from '../actions/action_group';

const group = (state = {}, action) => {
  switch (action.type) {
    case GROUP_FETCHED:
      return { ...state, groupFetched: action.payload }
    case GROUP_FETCH_ERROR:
      return { ...state, groupFetchError: action.payload }
    case GROUPS_FETCHED:
      return { ...state, groupsFetched: action.payload }
    case GROUPS_FETCH_ERROR:
      return { ...state, groupsFetchError: action.payload }
    case GROUP_CREATED:
      return { ...state, groupCreated: action.payload }
    case GROUP_CREATE_ERROR:
      return { ...state, groupCreateError: action.payload }
    case GROUP_DELETED:
      return { ...state, groupDeleted: action.payload }
    case GROUP_DELETE_ERROR:
      return { ...state, groupDeleteError: action.payload }
    case GROUP_UPDATED:
      return { ...state, groupUpdated: action.payload }
    case GROUP_UPDATE_ERROR:
      return { ...state, groupUpdateError: action.payload }
    default:
      return state
  }
}

export default group;