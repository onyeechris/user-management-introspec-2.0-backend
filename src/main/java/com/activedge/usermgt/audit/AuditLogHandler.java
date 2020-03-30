package com.activedge.usermgt.audit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@Aspect
@Component
/**
 * https://docs.spring.io/spring/docs/2.5.x/reference/aop.html
 */
public class AuditLogHandler extends AbstractRequestLoggingFilter {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public AuditLogHandler() {
        this.setIncludeQueryString(true);
        this.setIncludePayload(true);
        this.setMaxPayloadLength(10000);
        this.setIncludeHeaders(true);
        this.setIncludeClientInfo(true);
        this.setBeforeMessagePrefix("");
        this.setBeforeMessageSuffix("");
        this.setAfterMessagePrefix("");
        this.setAfterMessageSuffix("");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String s) {
        System.out.println("beforeRequest...");
        // log.error(s);
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
        System.out.println("afterRequest...");

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, s);
    }

    @Pointcut("execution(* *.*(..))") // the pointcut expression
    protected void allMethod() { // the method serving as the pointcut signature must have a void return type

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    // After -> All method within resource annotated with @RestController annotation
    // and return a  value
    @AfterReturning(
        pointcut  = "controller() && allMethod()",
        returning = "result"
    )
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);

        log.debug("Method Return value : " + returnValue);
    }

    // After -> Any method within resource annotated with @Controller annotation
    // throws an exception ...Log it
    @AfterThrowing(
        pointcut = "controller() && allMethod() && args(..,request, response)",
        throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.error("Cause : " + exception.getCause());
    }

//    @Pointcut("within(com.activedge.usermgt..*)")
//    public void logAnyFunctionWithinResource() {
//    }

//    @Before("controller()")
//    public void loggingAll(JoinPoint joinPoint) {
//        System.out.println("Just logging...");
//    }

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void controllerAdvice() {}

    @Before("controllerAdvice() && args(body, exception, request, response)")
    public void logAllAfterThrowing(JoinPoint joinPoint, Object body, Throwable exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("method: ").append(request.getMethod()).append("\t");
        logMessage.append("uri: ").append(request.getRequestURI()).append("\t");
        logMessage.append("status: ").append(response.getStatus()).append("\t");
        logMessage.append("remoteAddress: ").append(request.getRemoteAddr()).append("\t");
        log.error("A global exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.error("Message : " + logMessage);
        log.error("Cause : " + exception.getCause());
        HttpServletRequest requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
        Iterator<Map.Entry<String, String[]>> it = requestCacheWrapperObject.getParameterMap().entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String[]> e = it.next();
            log.error("ParameterMap[{}] : {}", e.getKey(), Arrays.toString(e.getValue()));
        }
        log.error("Body : " + Arrays.toString(((ContentCachingRequestWrapper) requestCacheWrapperObject).getContentAsByteArray()));
    }

    // Around -> Any method within resource annotated with @Controller annotation
    @Around("controller() && allMethod() && args(..,request, response)")
    public Object logAround(ProceedingJoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            String className   = joinPoint.getSignature().getDeclaringTypeName();
            String methodName  = joinPoint.getSignature().getName();
            Object result      = joinPoint.proceed();
            long   elapsedTime = System.currentTimeMillis() - start;

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("method: ").append(request.getMethod()).append("\t");
            logMessage.append("uri: ").append(request.getRequestURI()).append("\t");
            logMessage.append("status: ").append(response.getStatus()).append("\t");
            logMessage.append("remoteAddress: ").append(request.getRemoteAddr()).append("\t");

            log.info("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms " + logMessage);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                      + joinPoint.getSignature().getName() + "()");

            throw e;
        }
    }

    // before -> Any resource annotated with @Controller annotation
    // and all method and function taking HttpServletRequest as last parameter
    @Before("controller() && allMethod() && args(request, response, body,..)")
    public void logBefore(JoinPoint joinPoint, Object body, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());
        log.debug("Request Body : " + body);
//        request.get
        if (null != request) {
            log.debug("Start Header Section of request ");
            log.debug("Method Type : " + request.getMethod());

            Enumeration headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName  = headerNames.nextElement().toString();
                String headerValue = request.getHeader(headerName);

                log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
            }

            log.debug("Request Path info :" + request.getServletPath());
            log.debug("End Header Section of request ");
        }
    }

    @Pointcut("execution(* *.*(..))")
    protected void loggingAllOperation() {}

    @Pointcut("execution(public * *(..))")
    protected void loggingPublicOperation() {}

    private String getValue(Object result) {
        String returnValue = null;

        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = ReflectionToStringBuilder.toString(result);
            } else {
                returnValue = result.toString();
            }
        }

        return returnValue;
    }
//    uri=/auth-service/auth?=3;client=0:0:0:0:0:0:0:1;user=admin@aet.com;
    // headers=[affiliatecode:"CI", authorization:"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhZXQuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJwZXJtaXNzaW9ucyI6W10sImlhdCI6MTU4Mzc4NjUxMCwiZXhwIjoxNTgzODcyNTcwfQ.He4OMk-8nAzkrVZocPwjm410xrkFgiy4--D3IUZdadGMCVo_jtPWfnnCYHcHYEyxqt_2S_mPZ98iP3bXY_-ffg",
// cache-control:"no-cache", postman-token:"e72c03a3-08b9-4bd3-800a-c6fb12821696", user-agent:"PostmanRuntime/7.6.0", accept:"*/*",
// host:"localhost:9100", cookie:"mongo-express=s%3ANrNYHSJWUVCjndNbJU8d1c_E8zO0EyTv.cOmDXcA5bO8RbRHaegoUhqLqQSvqFH4iL9B2CSi%2Fzf4;
// JSESSIONID=D2706A9EC8314649D4B4A2AF654B9435", accept-encoding:"gzip, deflate", content-length:"25", connection:"keep-alive",
// Content-Type:"application/json;charset=UTF-8"]<;payload=For God so love the world>

}