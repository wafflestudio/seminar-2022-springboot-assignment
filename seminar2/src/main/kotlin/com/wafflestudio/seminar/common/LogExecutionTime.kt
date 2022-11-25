package com.wafflestudio.seminar.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime

@Aspect
@Component
class LogExecutionTimeAspect {
    private val log = LoggerFactory.getLogger(javaClass)
    
    @Around("@annotation(LogExecutionTime)")
    @Throws(Throwable::class)
    fun timeLogging(jointPoint: ProceedingJoinPoint) : Any? {
        val ts = System.currentTimeMillis()
        val ret = jointPoint.proceed()
        val te = System.currentTimeMillis()
        log.info("${jointPoint.signature} takes ${te-ts}ms")
        return ret
    }
}