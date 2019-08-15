import { TODOS_FETCHED, TODOS_FETCH_ERROR } from '../actions/action_todo';

const todo = (state = {}, action) => {
  switch (action.type) {
    case TODOS_FETCHED:
      return { ...state, todosFetched: action.payload }
    case TODOS_FETCH_ERROR:
      return { ...state, todosFetchError: action.payload }
    default:
      return state
  }
}

export default todo;