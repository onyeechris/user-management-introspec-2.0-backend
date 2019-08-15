import axios from 'axios';
import {
  BASE_URL
} from './types';
import { errorSwitch, interceptor } from './utils';

export const STAFFS_FETCHED = 'STAFFS_FETCHED';
export const STAFFS_FETCH_ERROR = 'STAFFS_FETCH_ERROR';
export const STAFF_FETCHED = 'STAFF_FETCHED';
export const STAFF_FETCH_ERROR = 'STAFF_FETCH_ERROR';
export const STAFF_DELETED = 'STAFF_DELETED';
export const STAFF_DELETE_ERROR = 'STAFF_DELETE_ERROR';
export const STAFF_CREATED = 'STAFF_CREATED';
export const STAFF_CREATE_ERROR = 'STAFF_CREATE_ERROR';
export const STAFF_UPDATED = 'STAFF_UPDATED';
export const STAFF_UPDATE_ERROR = 'STAFF_UPDATE_ERROR';

let apiUrl = BASE_URL + 'staffs/';

interceptor();

export function fetchStaffs(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'GET',
        url: apiUrl + type
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
