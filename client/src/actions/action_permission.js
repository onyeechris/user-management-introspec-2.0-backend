import axios from 'axios';
import {
  BASE_URL
} from './types';
import {
  errorSwitch,
  // interceptor 
  appInterceptor,
} from './utils';

export const PERMISSIONS_FETCHED = 'PERMISSIONS_FETCHED';
export const PERMISSIONS_FETCH_ERROR = 'PERMISSIONS_FETCH_ERROR';
export const PERMISSION_FETCHED = 'PERMISSION_FETCHED';
export const PERMISSION_FETCH_ERROR = 'PERMISSION_FETCH_ERROR';
export const PERMISSION_DELETED = 'PERMISSION_DELETED';
export const PERMISSION_DELETE_ERROR = 'PERMISSION_DELETE_ERROR';
export const PERMISSION_CREATED = 'PERMISSION_CREATED';
export const PERMISSION_CREATE_ERROR = 'PERMISSION_CREATE_ERROR';
export const PERMISSION_UPDATED = 'PERMISSION_UPDATED';
export const PERMISSION_UPDATE_ERROR = 'PERMISSION_UPDATE_ERROR';

let apiUrl = BASE_URL + 'permissions/';
// interceptor(userModule);
appInterceptor();

// let permissionHeaders = {}

// const updateHeaders = () => {
//   let userModule = sessionStorage.getItem("userModule");
//   let userToken = JSON.parse(sessionStorage.getItem("userData")).token ? JSON.parse(sessionStorage.getItem("userData")).token : "";
//   console.log(userModule);
//   permissionHeaders = {
//     Authorization: userToken,
//     Module: userModule ? userModule : "ADMIN"
//   }
//   console.log(permissionHeaders);
// }


export function fetchPermissions(type) {
  // updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type,
        // headers: permissionHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: PERMISSIONS_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: PERMISSIONS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch permissions');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function fetchPermission(type) {
  // updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type,
        // headers: permissionHeaders
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: PERMISSION_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: PERMISSION_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch permissions');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function deletePermission(type) {
  // updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'DELETE',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: PERMISSION_DELETED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(error.response.data.message);
        dispatch({
          type: PERMISSION_DELETE_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t delete permission');
        console.log(errorSwitch(error));
        console.log(error.response.data.message);
      })
    })
  }
}

export function createPermission(permissionInfo) {
  // updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(permissionInfo);
      axios.post(apiUrl, permissionInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: PERMISSION_CREATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: PERMISSION_CREATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}

export function updatePermission(permissionInfo) {
  // updateHeaders();
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(permissionInfo);
      axios.put(apiUrl, permissionInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: PERMISSION_UPDATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: PERMISSION_UPDATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}
