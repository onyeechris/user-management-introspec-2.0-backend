import axios from 'axios';
import {
  BASE_URL
} from './types';
import { errorSwitch, interceptor } from './utils';

export const GROUP_FETCHED = 'GROUP_FETCHED';
export const GROUP_FETCH_ERROR = 'GROUP_FETCH_ERROR';
export const GROUPS_FETCHED = 'GROUPS_FETCHED';
export const GROUPS_FETCH_ERROR = 'GROUPS_FETCH_ERROR';
export const GROUP_CREATED = 'GROUP_CREATED';
export const GROUP_CREATE_ERROR = 'GROUP_CREATE_ERROR';
export const GROUP_DELETED = 'GROUP_DELETED';
export const GROUP_DELETE_ERROR = 'GROUP_DELETE_ERROR';
export const GROUP_UPDATED = 'GROUP_UPDATED';
export const GROUP_UPDATE_ERROR = 'GROUP_UPDATE_ERROR';

let apiUrl = BASE_URL + 'api/groups/';

interceptor();

export function fetchGroup(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: GROUP_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: GROUP_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch group');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function fetchGroups() {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      axios({
        method: 'GET',
        url: apiUrl
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: GROUPS_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: GROUPS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch groups');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function createGroup(groupInfo) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(groupInfo);
      axios.post(apiUrl, groupInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: GROUP_CREATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: GROUP_CREATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}

export function deleteGroup(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'DELETE',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: GROUP_DELETED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(error.response.data.message);
        dispatch({
          type: GROUP_DELETE_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t delete group');
        console.log(errorSwitch(error));
        console.log(error.response.data.message);
      })
    })
  }
}

export function updateGroup(groupInfo, flag) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + flag);
      console.log(groupInfo);
      axios.put(apiUrl + flag, groupInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: GROUP_UPDATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: GROUP_UPDATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}