package com.wafflestudio.seminar.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class SeminarLogger {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Around("within(com.wafflestudio.seminar.core..*Controller+)")
    @Throws(Throwable::class)
    fun logExecutionTime(jp: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        var proceed = jp.proceed()
        val executionTime = System.currentTimeMillis() - startTime
        if (executionTime > 2000) {
            throw Seminar408("Time for request exceeds 2 seconds.")
        }
        logger.info("Request '${jp.signature.toShortString()}' is completed in ${executionTime}ms!")
        return proceed
    }

}


