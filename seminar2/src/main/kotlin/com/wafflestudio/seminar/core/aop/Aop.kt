package com.wafflestudio.seminar.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class Aop {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Around("within(com.wafflestudio.seminar..*Controller+)")
    fun execute(joinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()

        try {
            return joinPoint.proceed()
        } finally {
            val timeMs = System.currentTimeMillis() - start

            logger.info("{} Execution Time: {} ms", joinPoint.toString(), timeMs)
        }
    }
}