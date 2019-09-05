export const BASE_URL = 'http://localhost:9100/auth-service/';

export const userToken = JSON.parse(sessionStorage.getItem("userData")) ? JSON.parse(sessionStorage.getItem("userData")).token : ""; 