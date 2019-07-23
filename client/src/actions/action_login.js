import {
  BASE_URL
} from './types';

export const USER_FETCHED = 'USER_FETCHED';

export function fetchUser(type, appUser) {
  console.log(appUser);
  return (dispatch) => {
    return new Promise((resolve, reject) => {

      fetch(BASE_URL + type, {
        method: 'POST',
        body: JSON.stringify(appUser)
      })
        .then((response) => response.json())
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch(loadUser(responseJSON));
        })
        .catch((error) => {
          reject(error);
          console.log('Rejected.. Login Error');
        })
    })
  }
}


export function loadUser(results) {
  return {
    type: USER_FETCHED,
    payload: results
  }
}
