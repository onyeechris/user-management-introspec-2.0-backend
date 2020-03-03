//export const BASE_URL = 'https://10.8.243.1/auth-service/';
export const BASE_URL = '/auth-service/';

export let userToken = "";

if (sessionStorage.getItem("userData")) {
    userToken = JSON.parse(sessionStorage.getItem("userData")) ? JSON.parse(sessionStorage.getItem("userData")).token : "";
}