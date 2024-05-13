package kr.co.polycube.backendtest.Component;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* kr.co.polycube.backendtest.Controller.UserController.*(..))")
    public void userAPIMethods() {}

    @Before("userAPIMethods()")
    public void logClientAgentAndDetails(JoinPoint joinPoint) {
        // Honestly, never done this before. Straight off a tutorial from the web.
        HttpServletRequest request = (
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()
        ).getRequest();

        String clientAgent = request.getHeader("User-Agent");
        System.out.println(
                "Client Agent: " + clientAgent + " | Request Method: " + request.getMethod() +
                " | URL: " + request.getRequestURL()
        );

    }
}
