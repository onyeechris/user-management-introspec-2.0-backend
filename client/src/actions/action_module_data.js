import axios from 'axios';
import {
  BASE_URL,
  userToken
} from './types';
import { errorSwitch } from './utils';

export const USERS_BY_APP_FETCHED = 'USERS_BY_APP_FETCHED';
export const USERS_BY_APP_FETCH_ERROR = 'USERS_BY_APP_FETCH_ERROR';
export const GROUPS_BY_APP_FETCHED = 'GROUPS_BY_APP_FETCHED';
export const GROUPS_BY_APP_FETCH_ERROR = 'GROUPS_BY_APP_FETCH_ERROR';
export const PERMISSIONS_BY_APP_FETCHED = 'PERMISSIONS_BY_APP_FETCHED';
export const PERMISSIONS_BY_APP_FETCH_ERROR = 'PERMISSIONS_BY_APP_FETCH_ERROR';

// App MetaData

// Fetch users by app
let userApi = BASE_URL + 'userapps/';
export function fetchUsersByApp(moduleCode) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(userApi);
      console.log(moduleCode);
      axios({
        method: 'GET',
        url: userApi,
        headers: {
          Authorization: userToken,
          Module: moduleCode
        }
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: USERS_BY_APP_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: USERS_BY_APP_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch users');
        console.log(errorSwitch(error));
      })
    })
  }
}

// Fetch groups by app
let groupApi = BASE_URL + 'groups/';
export function fetchGroupsByApp(moduleCode) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(groupApi);
      axios({
        method: 'GET',
        url: groupApi,
        headers: {
          Authorization: userToken,
          Module: moduleCode
        }
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: GROUPS_BY_APP_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: GROUPS_BY_APP_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch groups');
        console.log(errorSwitch(error));
      })
    })
  }
}

// Fetch permissions by app
let permissionApi = BASE_URL + 'permissions/';
export function fetchPermissionsByApp(moduleCode) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(permissionApi);
      axios({
        method: 'GET',
        url: permissionApi,
        headers: {
          Authorization: userToken,
          Module: "SETTLEMENT"
        }
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: PERMISSIONS_BY_APP_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: PERMISSIONS_BY_APP_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch groups');
        console.log(errorSwitch(error));
      })
    })
  }
}