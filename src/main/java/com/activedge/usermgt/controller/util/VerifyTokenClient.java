//package com.activedge.usermgt.controller.util;
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "verify-token", url = "${verify.token.uri}")
//public interface VerifyTokenClient {
//
//    @PostMapping()
//    public ValidationResponse verify(@RequestHeader HttpHeaders headers,TokenRequest tokenRequest);
//}
