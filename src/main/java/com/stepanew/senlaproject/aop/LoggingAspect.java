package com.stepanew.senlaproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.stepanew.senlaproject.repository..*(..)))")
    public void allRepositoryMethodsAdvice(JoinPoint joinPoint) {
        log.info("Called repository method " + joinPoint.getSignature().getName() + " in "
                + joinPoint.getTarget().getClass().getSimpleName()
                + " with args " + Arrays.toString(joinPoint.getArgs())
        );
    }

    @Before("execution(* com.stepanew.senlaproject.services..*(..)))")
    public void allServiceMethodsAdvice(JoinPoint joinPoint) {
        log.info("Called service method " + joinPoint.getSignature().getName() + " in "
                + joinPoint.getTarget().getClass().getSimpleName()
                + " with args " + Arrays.toString(joinPoint.getArgs()));
    }

    @Before("execution(* com.stepanew.senlaproject.controller..*(..)))")
    public void allControllerMethodsAdvice(JoinPoint joinPoint) {
        log.info("Called controller method " + joinPoint.getSignature().getName() + " in "
                + joinPoint.getTarget().getClass().getSimpleName()
                + " with args " + Arrays.toString(joinPoint.getArgs()));
    }

}
