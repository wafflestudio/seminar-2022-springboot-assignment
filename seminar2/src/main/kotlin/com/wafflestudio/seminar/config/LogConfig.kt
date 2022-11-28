package com.wafflestudio.seminar.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class LogConfig {
    @Around("@annotation(com.wafflestudio.seminar.common.LogExecutionTime)")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val logger = LoggerFactory.getLogger(javaClass)
        
        val start = System.currentTimeMillis()

        var proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - start

        logger.info("${joinPoint.signature} excuted in ${executionTime}ms")
        return proceed
    }
    
}