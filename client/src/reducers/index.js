import { combineReducers } from 'redux';
import profile from './user_reducer';
import staff from './staff_reducer';

const rootReducer = combineReducers({
  profile,
  staff
});

export default rootReducer;
