package com.wafflestudio.seminar.config

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

    @Around("within(com.wafflestudio.seminar..*Controller+)")
    fun doPublishEvent(jp: ProceedingJoinPoint): Any? {
        var result: Any?
        val execution = measureTimeMillis { result = jp.proceed() }
        logger.info("${jp.signature}의 수행시간: $execution")
        return result
    }
}