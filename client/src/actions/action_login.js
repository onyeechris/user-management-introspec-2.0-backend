export const USER_FETCHED = 'USER_FETCHED';

export function fetchUser(type, appUser) {
  let baseUrl = 'http://localhost:9100/';
  return (dispatch) => {
    return new Promise((resolve, reject) => {

      fetch(baseUrl + type, {
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
