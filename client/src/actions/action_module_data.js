// import axios from 'axios';
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
let userApi = BASE_URL + 'userapps/?size=10000';
export function fetchUsersByApp(moduleId) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      // axios({
      //   method: 'GET',
      //   url: userApi,
      //   headers: {
      //     Authorization: userToken,
      //     Module: moduleId
      //   }
      // })
      fetch(userApi, {
        method: 'GET',
        headers: {
          Authorization: userToken,
          Module: moduleId
        }
      })
        .then((response) => response.json())
        .then((responseJSON) => {
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
let groupApi = BASE_URL + 'groups/?size=10000';
export function fetchGroupsByApp(moduleId) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      // axios({
      //   method: 'GET',
      //   url: groupApi,
      //   headers: {
      //     Authorization: userToken,
      //     Module: moduleId
      //   }
      // })
      fetch(groupApi, {
        method: 'GET',
        headers: {
          Authorization: userToken,
          Module: moduleId
        }
      })
        .then((response) => response.json())
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: GROUPS_BY_APP_FETCHED,
            payload: responseJSON
          });
        }).catch((error) => {
          console.log(error,'error')
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
let permissionApi = BASE_URL + 'permissions/?size=10000';
export function fetchPermissionsByApp(moduleId) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      // axios({
      //   method: 'GET',
      //   url: permissionApi,
      //   headers: {
      //     Authorization: userToken,
      //     Module: moduleId
      //   }
      // })

      fetch(permissionApi, {
        method: 'GET',
        headers: {
          Authorization: userToken,
          Module: moduleId
        }
      })
        .then((response) => response.json())
        .then((responseJSON) => {
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