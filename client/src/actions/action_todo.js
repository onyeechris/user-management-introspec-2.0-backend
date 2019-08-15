import axios from 'axios';
import {
  BASE_URL
} from './types';
import { errorSwitch, interceptor } from './utils';
// import { showTasks } from '../_nav';

export const TODOS_FETCHED = 'TODOS_FETCHED';
export const TODOS_FETCH_ERROR = 'TODOS_FETCH_ERROR';

let apiUrl = BASE_URL + 'todos/';

interceptor();

export function fetchTodos() {
  return (dispatch) => {
    return new Promise((resolve, reject) => {
      // console.log(apiUrl);
      axios({
        method: 'GET',
        url: apiUrl
      }).then((responseJSON) => {
        resolve(responseJSON);
        dispatch({
          type: TODOS_FETCHED,
          payload: responseJSON
        });
        return responseJSON;
      }).catch((error) => {
        reject(errorSwitch(error));
        dispatch({
          type: TODOS_FETCH_ERROR,
          payload: errorSwitch(error)
        });
        console.log('Rejected.. Couldn\'t fetch todos');
        console.log(errorSwitch(error));
      })
    })
  }
}

// let result;

// export const fetchTodoSize = () => {
//   axios({
//     method: 'GET',
//     url: apiUrl
//   }).then((responseJSON) => {
//     sessionStorage.setItem("pendingTasks", responseJSON.data.payload.length);
//     console.log(responseJSON.data.payload.length);
//     result = responseJSON.data.payload.length;
//     console.log(result);
//     // showTasks(responseJSON.data.payload.length);
//     return result;
//   }).catch((error) => {
//     console.log(error);
//   })
//   console.log(result);
//   // return result;
// }

// fetchTodoSize();
// // .then(response => {
// //   console.log(response);
// //   result = response.data.payload.length;
// //   console.log(result);
// // });
// // console.log(result);
// export let todoSize = fetchTodoSize();
// export let todoSize1 = "result";
