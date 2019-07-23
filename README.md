##	IntroSpec User Management Development Repository

 IntroSpec Single Sign On Service.

 With React.js clientSide and Spring-boot backend.
 
 Powered by Spring Security and JWT

- All endpoints are exposed on **Port: 9100**


######Application Requirements:
- Java 8+
- Maven 3+
- Node 8+

######Deploying and Running application:
> Step One: Build the client side (from the root directory, navigate to the client folder)
```
~/client$> npm install
~/client$> npm run build
~/client$> npm start
```
> Step Two: Run the  backend (From the root directory).
```
~$> mvn clean package spring-boot:run
```
Once the service is up, you can navigate to the URL below.

`http://localhost:9100`
				
####   WIP 

#### Database Information
```
    url: jdbc:postgresql://localhost:5432/usermgt
    database = "usermgt"
    username: YOUR USERNAME
    password: YOUR PASSWORD
```
You can update the application.yml file to your postgre username and password in lines 33 and 34 respectively.





##User Management Application Sign In

Prerequisite: The User Management module should be pulled and running on your localhost. Follow the README.MD file in the root directory for instruction on how to startup the application's frontend and backend servers.

You will need to add the crypto-js resource package to your react app thus: 
```
npm install --save crypto-js
```
Then in your landing page or component, you will need to import it thus:
```
import CryptoJS from "crypto-js";
```

###Sign In Process:

> - From your home page, if the url param 'var' is absent, your app (Settlement or ATM Recon) should offer the option to 'sign in using Introspec User Management'.

> - When clicked, it should 
> - - A. Save the application url in a variable thus: 
```
    const redirectUrl = window.location.href;
```
> - - B. Redirect to the frontend server url with your saved url appended into a 'redirectUrl' param thus:
```
    window.location.href = "http://localhost:3000/applogin/?redirectUrl=" + redirectUrl;
```
#####NB: If there was something occupying port 3000 at the start of the User Management frontend, you might be running it on another port: 3001, 3002 etc. So you may need to changed the above step accordingly.

> - After the user's details have been filled correctly, the system will redirect to the application's URL which will then repeat the first step in the process (check the url for the param 'var').

> - If it is present then the page should get the value, save to a variable and then decode it to retrieve the token using the react-native-crypto-js plugin. Here's a short way to do this:

```
// Get param from URL
    let url_string = window.location.href;
    let url = new URL(url_string);
    let ciphertext = url.searchParams.get("var"); // 'var' is the param that will be sent in the URL
    if (ciphertext){
        // Decrypt
        let bytes = CryptoJS.AES.decrypt(ciphertext.split(" ").join("+"), 'introspecAppToken');
        let decryptedToken = bytes.toString(CryptoJS.enc.Utf8);

        console.log(decryptedToken); // 'my token'
        
        // save the decrypted token to the session storage
        sessionStorage.setItem("myToken", decryptedToken); 
    } else {
        // redirect browser to user management login with your app url included as the redirectUrl
        window.location.href = "http://localhost:3000/applogin/?redirectUrl=" + url_string;
        // also, for internationalization, you can add a 'lang' param which supports fr, es, en, de and pt languages,// Using French for example ('fr') the previous url can be appended thus
        // window.location.href = "http://localhost:3000/applogin/?redirectUrl=" + url_string + "&lang=fr";
    }

```

#####NB: Here we are using 'introspecAppToken' as the key to decrypt the encrypted token.. this can be changed to hard-to-guess string we choose but has to be the same on both ends.

Your token will then be used to access the different endpoints in your app.

To Clone this branch to your local, copy the clone command and add the following appendage before you run:
```
 -b dev
```

These are the credentials you need to login to the application now:

```
sysdev@aet.com : sysdevsecret, For creating permissions alone
maker@aet.com : makersecret, For creating, modifying and deleting other things
checker@aet.com : checkersecret, For approving modifications on groups and staffs
```

To run the backend, please use:

```
mvn clean package -DskipTests spring-boot:run
```

