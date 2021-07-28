package com.activedge.usermgt.audit;

import com.activedge.usermgt.model.CustomHttpTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static long starttime;
    private static long endtime;

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
        log.info("New Request :: {} {}", request.getMethod(), request.getRequestURL());
        starttime = System.currentTimeMillis();
    }

    @Override
    protected void afterRequest(HttpServletRequest req, String s) {
        endtime = System.currentTimeMillis();

        Matcher matcher =   Pattern.compile("payload=[.\\s\\S.*]+", Pattern.CASE_INSENSITIVE).matcher(s);

        CustomHttpTrace ctrace = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .username(req.getRemoteUser())
                .sourceIp(req.getRemoteAddr())
                .path(req.getRequestURL().toString())
                .queryParams(req.getQueryString())
                .method(req.getMethod())
                .timeTaken((endtime-starttime))
                .payload(matcher.find() ? matcher.group().split("=")[1].replaceAll("\"password\":\"[\\s\\S]+\"", "\"password\":\"***\"") : "n/a")
                .rawBody(s.replaceAll("\"password\":\"[\\s\\S]+\"", "\"password\":\"***\""))
                .build();

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, ctrace);
    }

    @Pointcut("execution(* *.*(..))") // the pointcut expression
    protected void allMethod() { // the method serving as the pointcut signature must have a void return type

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void controllerAdvice() {}

    @AfterThrowing(
            pointcut = "within(*..service..*)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String exceptionMsg = "";
        final StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            exceptionMsg += "\nException thrown from " + element.getMethodName()
                    + " in class " + element.getClassName() + " [on line number "
                    + element.getLineNumber() + " of file " + element.getFileName() + "]";
        }
        CustomHttpTrace cTrace = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .status(500)
                .username(joinPoint.getSignature().getName())
                .payload(exception.getMessage())
                .rawBody(exceptionMsg)
                .build();

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, cTrace);
    }

    @Pointcut("execution(* com.activedge.usermgt.controller..*(..)))")
    private void inController() {}

    @Pointcut("execution(* com.activedge.usermgt.service..*(..)))")
    private void inService() {}

    @Pointcut("execution(* com.activedge.usermgt.repository..*(..)))")
    private void inRepository() {}

    @Around("inController() || inService() || inRepository()")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        log.info("Execution time of " + className + "." + methodName + " " + ":: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }

}