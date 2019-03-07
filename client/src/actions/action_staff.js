export const STAFF_FETCHED = 'STAFF_FETCHED';

export function fetchStaff(type) {
  let baseUrl = 'http://localhost:9100/';
  return (dispatch) => {
    return new Promise((resolve, reject) => {

      fetch(baseUrl + type, {
        method: 'GET',
        body: ''
      })
        .then((response) => response.json())
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch(loadStaff(responseJSON));
        })
        .catch((error) => {
          reject(error);
          console.log('Rejected.. Couldn\'t fetch staffs');
        })
    })
  }
}

export function loadStaff(results) {
  return {
    type: STAFF_FETCHED,
    payload: results
  }
}
