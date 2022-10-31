package com.wafflestudio.seminar.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class RequestLoggerAspect {
    
    val logger = LoggerFactory.getLogger(javaClass)
    
    @Pointcut("within(com.wafflestudio.seminar..*Controller+)")
    fun  onRequest() {}
    
    @Around("com.wafflestudio.seminar.common.RequestLoggerAspect.onRequest()")
    fun requestLogging (proceedingJoinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        
        try{
            return proceedingJoinPoint.proceed()
        } finally {
            val finish = System.currentTimeMillis()
            logger.info("{} function : {} milliSeconds", proceedingJoinPoint.toString(), finish - start)
        }
    }
    
}