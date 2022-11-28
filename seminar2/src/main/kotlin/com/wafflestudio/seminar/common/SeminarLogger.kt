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
        
        // 수행시간이 2초를 넘는다면, time out으로 간주
        if(executionTime > 2*1000) throw SeminarException(ErrorCode.REQUEST_TIMEOUT)

        logger.info("${jp.signature.toShortString()} 요청이 ${executionTime}ms만에 완료되었습니다.")
        return proceed
    }
    
}
