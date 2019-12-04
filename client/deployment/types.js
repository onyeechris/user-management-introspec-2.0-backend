export const BASE_URL = '/auth-service/';

export let userToken = "";

if (sessionStorage.getItem("userData")) {
    userToken = JSON.parse(sessionStorage.getItem("userData")) ? JSON.parse(sessionStorage.getItem("userData")).token : "";
}