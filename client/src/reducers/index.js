import { combineReducers } from 'redux';
import profile from './user_reducer';
import staff from './staff_reducer';
import group from './group_reducer';
import permission from './permission_reducer';
import locale from './locale_reducer';
import todo from './todo_reducer';
import module from './module_reducer';
import moduleData from './module_data_reducer';
import { reducer as formReducer } from 'redux-form';

const rootReducer = combineReducers({
  profile,
  staff,
  group,
  permission,
  locale,
  todo,
  module,
  moduleData,
  form: formReducer,
});

export default rootReducer;
