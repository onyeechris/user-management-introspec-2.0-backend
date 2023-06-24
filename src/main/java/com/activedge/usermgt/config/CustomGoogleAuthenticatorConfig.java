//package com.activedge.usermgt.config;
//
//import com.activedge.usermgt.repository.CredentialRepository;
//import com.warrenstrange.googleauth.GoogleAuthenticator;
//import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//@RequiredArgsConstructor
//public class CustomGoogleAuthenticatorConfig {
//
//    private final CredentialRepository credentialRepository;
//
//    @Bean
//    public GoogleAuthenticator gAuth() {
//        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
//        googleAuthenticator.setCredentialRepository(credentialRepository);
//        return googleAuthenticator;
//    }
//
//    @Bean
//    public GoogleAuthenticator timeSpan(){
//        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder gacb =
//                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
//                        .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
//                        .setWindowSize(5)
//                        .setCodeDigits(6);
//        GoogleAuthenticator ga = new GoogleAuthenticator(gacb.build());
//        return ga;
//    }
//}
