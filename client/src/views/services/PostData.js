export function PostData(type, appUser) {

    let baseUrl = 'http://localhost:9100/';
    //let baseUrl = 'https://api.thewallscript.com/restful/';
    return new Promise((resolve, reject) => {

        fetch(baseUrl + type, {
            method: 'POST',
            body: JSON.stringify(appUser)

        })
            .then((response) => response.json())
            .then((responseJSON) => {
                resolve(responseJSON);
            })
            .catch((error) => {
                reject(error);
                console.log('Rejected.. Error');
            })
    })
}