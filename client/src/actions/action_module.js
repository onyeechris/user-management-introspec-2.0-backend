import axios from 'axios';
import {
  BASE_URL
} from './types';
import { errorSwitch, interceptor } from './utils';

export const MODULE_FETCHED = 'MODULE_FETCHED';
export const MODULE_FETCH_ERROR = 'MODULE_FETCH_ERROR';
export const MODULES_FETCHED = 'MODULES_FETCHED';
export const MODULES_FETCH_ERROR = 'MODULES_FETCH_ERROR';
export const MODULE_CREATED = 'MODULE_CREATED';
export const MODULE_CREATE_ERROR = 'MODULE_CREATE_ERROR';
export const MODULE_DELETED = 'MODULE_DELETED';
export const MODULE_DELETE_ERROR = 'MODULE_DELETE_ERROR';
export const MODULE_UPDATED = 'MODULE_UPDATED';
export const MODULE_UPDATE_ERROR = 'MODULE_UPDATE_ERROR';
export const MODULE_SAVED = 'MODULE_SAVED';

let apiUrl = BASE_URL + 'appmodule/';

interceptor();

export function fetchModule(moduleId) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + moduleId);
      axios({
        method: 'GET',
        url: apiUrl + moduleId
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: MODULE_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(error.response ? error.response.data.message : errorSwitch(error));
        dispatch({
          type: MODULE_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch module');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function fetchModules() {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      axios({
        method: 'GET',
        url: apiUrl
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: MODULES_FETCHED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: MODULES_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch modules');
        console.log(errorSwitch(error));
      })
    })
  }
}

export function createModule(moduleInfo) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(moduleInfo);
      axios.post(apiUrl, moduleInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: MODULE_CREATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(errorSwitch(error));
          dispatch({
            type: MODULE_CREATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(errorSwitch(error));
        })
    })
  }
}

export function deleteModule(type) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl + type);
      axios({
        method: 'DELETE',
        url: apiUrl + type
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: MODULE_DELETED,
          payload: responseJSON
        });
      }).catch((error) => {
        reject(error.response.data.message);
        dispatch({
          type: MODULE_DELETE_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t delete module');
        console.log(errorSwitch(error));
        console.log(error.response.data.message);
      })
    })
  }
}

export function updateModule(moduleInfo) {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      console.log(apiUrl);
      console.log(moduleInfo);
      axios.put(apiUrl, moduleInfo)
        .then((responseJSON) => {
          resolve(responseJSON);
          dispatch({
            type: MODULE_UPDATED,
            payload: responseJSON
          });
        }).catch((error) => {
          reject(error.response ? error.response.data.message : errorSwitch(error));
          dispatch({
            type: MODULE_UPDATE_ERROR,
            payload: errorSwitch(error)
          });
          console.log(error.response ? error.response.data.message : errorSwitch(error));
        })
    })
  }
}

export function saveModuleInfo(moduleInfo) {
  return (dispatch) => {
    dispatch({
      type: MODULE_SAVED,
      payload: moduleInfo
    });
  }
}