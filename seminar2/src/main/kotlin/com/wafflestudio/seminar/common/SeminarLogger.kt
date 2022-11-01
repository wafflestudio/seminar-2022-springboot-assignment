package com.wafflestudio.seminar.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Aspect
@Component
class SeminarLogger {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Around("within(com.wafflestudio.seminar..*Controller*)")
    fun logRequestHandlingTime(jp: ProceedingJoinPoint): Any? {
        var response: Any? = null
        
        val executionTime = measureTimeMillis { response = jp.proceed() }
        logger.info("Execution time: $executionTime (ms)")
        
        return response
    }
}