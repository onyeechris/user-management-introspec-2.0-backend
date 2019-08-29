import axios from 'axios';
import {
  BASE_URL
} from './types';
import {
  errorSwitch,
  // interceptor 
} from './utils';

export const STAFFS_FETCHED = 'STAFFS_FETCHED';
export const STAFFS_FETCH_ERROR = 'STAFFS_FETCH_ERROR';
export const ALL_STAFFS_FETCHED = 'ALL_STAFFS_FETCHED';
export const ALL_STAFFS_FETCH_ERROR = 'ALL_STAFFS_FETCH_ERROR';
export const STAFF_FETCHED = 'STAFF_FETCHED';
export const STAFF_FETCH_ERROR = 'STAFF_FETCH_ERROR';
export const STAFF_DELETED = 'STAFF_DELETED';
export const STAFF_DELETE_ERROR = 'STAFF_DELETE_ERROR';
export const STAFF_CREATED = 'STAFF_CREATED';
export const STAFF_CREATE_ERROR = 'STAFF_CREATE_ERROR';
export const STAFF_UPDATED = 'STAFF_UPDATED';
export const STAFF_UPDATE_ERROR = 'STAFF_UPDATE_ERROR';
export const APP_USERS_FETCHED = 'APP_USERS_FETCHED';
export const APP_USERS_FETCH_ERROR = 'APP_USERS_FETCH_ERROR';
export const APP_USER_FETCHED = 'APP_USER_FETCHED';
export const APP_USER_FETCH_ERROR = 'APP_USER_FETCH_ERROR';
export const APP_USER_DELETED = 'APP_USER_DELETED';
export const APP_USER_DELETE_ERROR = 'APP_USER_DELETE_ERROR';
export const USER_ADDED = 'USER_ADDED';
export const USER_ADD_ERROR = 'USER_ADD_ERROR';

let apiUrl = BASE_URL + 'staffs/';

// interceptor();

let staffHeaders = {}

const updateHeaders = () => {
  let userModule = sessionStorage.getItem("userModule");
  let userToken = JSON.parse(sessionStorage.getItem("userData")).token ? JSON.parse(sessionStorage.getItem("userData")).token : "";
  console.log(userModule);
  staffHeaders = {
    Authorization: userToken,
    // Module: "ADMIN"
    Module: userModule ? userModule : "ADMIN"
  }
  console.log(staffHeaders);
}

export function fetchStaffs(type) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type,
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: STAFFS_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: STAFFS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function fetchStaff(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: STAFF_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: STAFF_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function deleteStaff(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'DELETE',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: STAFF_DELETED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(error.response.data.message);
        dispatch({
          type: STAFF_DELETE_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t delete staff');
        console.log(errorSwitch(error));
        console.log(error.response.data.message);
      })
    })
  }
}

export function createStaff(staffInfo) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(staffInfo);
      axios.post(apiUrl, staffInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: STAFF_CREATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: STAFF_CREATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}

export function updateStaff(staffInfo) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(staffInfo);
      axios.put(apiUrl, staffInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: STAFF_UPDATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: STAFF_UPDATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}

export function fetchAllStaffs(type) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type,
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: ALL_STAFFS_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: ALL_STAFFS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}


// Fetch users by app
let userApi = BASE_URL + 'userapps/';
export function fetchAppUsers(type) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(userApi + type);
      axios({
        method: 'GET',
        url: userApi + type,
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: APP_USERS_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: APP_USERS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}

// Add users to app
export function addUserToApp(user) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(userApi);
      console.log(user);
      axios.post(userApi, user, {
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: USER_ADDED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: USER_ADD_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}

// Fetch a user from app
export function fetchUserApp(user) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(userApi);
      console.log(user);
      axios({
        method: 'GET',
        url: userApi + user,
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: APP_USER_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: APP_USER_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}


// Remove users from app
export function removeFromApp(user) {
  updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(userApi);
      console.log(user);
      axios({
        method: 'DELETE',
        url: userApi + user,
        headers: staffHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: APP_USER_DELETED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: APP_USER_DELETE_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch staffs');
        console.log(errorSwitch(error));
      })
    })
  }
}
