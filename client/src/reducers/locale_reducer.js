import { LOCALE_SET } from '../actions/action_locale';

const locale = (state = { lang: 'en' }, action) => {
  switch (action.type) {
    case LOCALE_SET:
      return { ...state, lang: action.payload }
    default:
      return state;
  }
}

export default locale;