export const LOCALE_SET = 'locale_set';

export const setLocale = (language) => dispatch => {
    localStorage.se8lementLang = language;
    dispatch({
        type: LOCALE_SET,
        payload: language
    })
}