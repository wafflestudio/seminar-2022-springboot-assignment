package com.wafflestudio.seminar.common

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class Logger {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var beginTime = System.currentTimeMillis()

    @Before("within(com.wafflestudio.seminar..*Controller+)")
    fun recordBeginTime(jp: JoinPoint) {
        beginTime = System.currentTimeMillis()
    }

    @After("within(com.wafflestudio.seminar..*Controller+)")
    fun logTimeSpent(jp: JoinPoint) {
        val timeSpent = System.currentTimeMillis() - beginTime
        logger.info("${jp.signature} 요청의 수행 시간: ${timeSpent}ms")
    }
}
