package com.wafflestudio.seminar.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class Logger {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Around("within(com.wafflestudio.seminar..*Controller+)")
    fun timeLog(pjp: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        val result = pjp.proceed()
        val end = System.currentTimeMillis()
        logger.info(pjp.signature.name + "수행 시간 : " + (end - start) + "ms")
        return result
    }
}