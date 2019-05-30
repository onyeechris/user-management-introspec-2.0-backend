import { combineReducers } from 'redux';
import profile from './user_reducer';
import staff from './staff_reducer';
import group from './group_reducer';
import permission from './permission_reducer';
import locale from './locale_reducer';

const rootReducer = combineReducers({
  profile,
  staff,
  group,
  permission,
  locale
});

export default rootReducer;
