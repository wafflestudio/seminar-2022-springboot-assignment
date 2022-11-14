package com.wafflestudio.seminar.common

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenExtractor
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

annotation class Authenticated

annotation class UserContext

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginUser()

@Component
class LoginUserArgumentResolver(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UserEntity? {
        // From Http Request, extract token
        val httpServletRequest: HttpServletRequest = webRequest.getNativeRequest(
            HttpServletRequest::class.java
        ) as HttpServletRequest
        val token = AuthTokenExtractor.extract(httpServletRequest)
        // If Token Exists, verify and if verified, return UserEntity found by Email
        token?.let {
            if (authTokenService.verifyToken(token)) {
                val userEmail = authTokenService.getCurrentUserEmail(token)
                    ?: return null
                return userRepository.findUserEntityByEmail(userEmail)
            }
        }
        // Else return null
        return null
    }

    // private fun getUserIdFromToken...
}

// AOP Logger
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime

@Aspect
@Component
class ExecutionTimeLogger {
    private val log = LoggerFactory.getLogger(javaClass)

    @Around("@annotation(LogExecutionTime)")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - start

        log.info("${joinPoint.signature} executed in ${executionTime}ms")

        return proceed
    }

}