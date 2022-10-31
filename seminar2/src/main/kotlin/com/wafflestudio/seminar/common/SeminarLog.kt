package com.wafflestudio.seminar.common


import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class SeminarLog {
    private val log = LoggerFactory.getLogger(javaClass)

    @Around("within(com.wafflestudio.seminar..*Controller)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()

        val proceed = joinPoint.proceed()
        val time = System.currentTimeMillis() - start

        log.info("${joinPoint.signature} executed in ${time}ms")
        return proceed

       
    }

}