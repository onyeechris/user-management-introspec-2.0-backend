import axios from 'axios';

export const errorSwitch = (error) => {
    console.log(error);
    console.log(error.response);
    // console.log(error.response.data.status);
    // let errorMessage = error.toString();
    // let errorStatus = errorMessage.replace(/^\D+/g, '');
    // errorStatus = parseInt(errorStatus);

    // switch (errorStatus) {
    switch (error) {
        // switch (error.response.data.status) {
        case 400:
            return "Error, bad request";
        case 401:
            return "Unauthorized, login required!";
        case 403:
            return "You don't have the permission to access this function!";
        case 404:
            return "Sorry Page Not Found!";
        case 500:
            return "Something went wrong, please try again.";
        default:
            return "Sorry there was an error";
    }
}

// export let TOKEN = '';

// if (JSON.parse(sessionStorage.getItem("userData"))) {
//     TOKEN = JSON.parse(sessionStorage.getItem("userData")).token;
// }

export const interceptor = (userModule) => {
    console.log(userModule);
    axios.interceptors.request.use(function (config) {
        let token = '';

        if (JSON.parse(sessionStorage.getItem("userData"))) {
            token = JSON.parse(sessionStorage.getItem("userData")).token;
        }
        if (token != null) {
            config.headers.Authorization = token ? `${token}` : '';
            // if (userModule != null && userModule !== undefined) {
            //     config.headers.Module = userModule ? `${userModule}` : '';
            // }
            return config;
        }
    });
}