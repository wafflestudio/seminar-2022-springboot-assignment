package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar400
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor: HandlerInterceptor {
    @Autowired
    private lateinit var authTokenService: AuthTokenService
    val logger = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            var isAuthenticated = handler.getMethodAnnotation(Authenticated::class.java)
            if (isAuthenticated != null) {
                val authToken = request.getHeader("Authorization")
                if (authToken == null) {
                    throw Seminar400("토큰 값을 넣어주세요.")
                } else {
                    authTokenService.verifyToken(authToken)
                }
            }
            logger.info("정상 처리")
            return super.preHandle(request, response, handler)
        } else {
            logger.info("처리 안됨.")
            return false
        }
    }
}