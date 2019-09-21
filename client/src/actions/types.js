export const BASE_URL = 'http://localhost:9100/auth-service/';

export let userToken = "";

if (sessionStorage.getItem("userData")) {
    userToken = JSON.parse(sessionStorage.getItem("userData")) ? JSON.parse(sessionStorage.getItem("userData")).token : "";
}