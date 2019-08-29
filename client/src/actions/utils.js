import axios from 'axios';

export const errorSwitch = (error) => {
    console.log(error);
    console.log(error.response);
    // console.log(error.response.data.status);
    // let errorMessage = error.toString();
    // let errorStatus = errorMessage.replace(/^\D+/g, '');
    // errorStatus = parseInt(errorStatus);
    // switch (errorStatus) {
    if (error.response) {
        console.log(error.response.data.message);
        switch (error.response.data.status) {
            case 400:
                return "Error, bad request: " + error.response.data.message;
            case 401:
                return "Unauthorized, login required: " + error.response.data.message;
            case 403:
                return "You don't have the permission to access this function: " + error.response.data.message;
            case 404:
                return "Sorry Page Not Found: " + error.response.data.message;
            case 500:
                return "Something went wrong, please try again: " + error.response.data.message;
            default:
                return "Sorry there was an error: " + error.response.data.message;
        }
    }
    else {
        return error;
    }
}

export const interceptor = () => {
    axios.interceptors.request.use(function (config) {
        let token = '';
        // let userModule = sessionStorage.getItem("userModule") ? sessionStorage.getItem("userModule") : "";
        if (JSON.parse(sessionStorage.getItem("userData"))) {
            token = JSON.parse(sessionStorage.getItem("userData")).token;
        }
        if (token != null) {
            config.headers.Authorization = token ? `${token}` : '';
            // config.headers.Module = userModule ? `${userModule}` : '';
            // if (userModule != null && userModule !== undefined) {
            //     config.headers.Module = userModule ? `${userModule}` : '';
            // }
            return config;
        }
    });
}

// Interceptor for app specific endpoints
export const appInterceptor = () => {
    axios.interceptors.request.use(function (config) {
        let token = '';
        let userModule = sessionStorage.getItem("userModule");
        if (JSON.parse(sessionStorage.getItem("userData"))) {
            token = JSON.parse(sessionStorage.getItem("userData")).token;
        }
        if (token != null) {
            config.headers.Authorization = token ? `${token}` : '';
            config.headers.Module = userModule ? userModule : "ADMIN"
            return config;
        }
    });
}
