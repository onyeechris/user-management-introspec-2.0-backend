package com.activedge.usermgt.model.log;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
/**
 * https://docs.spring.io/spring/docs/2.5.x/reference/aop.html
 */
public class AuditLogHandler {

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

    @Pointcut("within(com.activedge.usermgt..*)")
    public void logAnyFunctionWithinResource() {
        System.out.println("Just logging...");
    }

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
        log.error("Body : " + IOUtils.toString(request.getInputStream()));
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

            log.debug("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms " + logMessage);

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
}
