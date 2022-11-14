package com.wafflestudio.seminar.config

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val authArgumentResolver: AuthArgumentResolver,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }
}

@Configuration
class AuthArgumentResolver(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserContext::class.java)
                && parameter.parameterType == Long::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Long {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
        val jwtToken = request?.getHeader("Authorization")
            ?: throw AuthException("토큰 에러")
        val userId = authTokenService.getCurrentUserId(jwtToken)
        userRepository.findByIdOrNull(userId) ?: throw Seminar404("존재하지 않는 userId 입니다")
        return userId
    }
}

@Configuration
class AuthInterceptor(
    private val authTokenService: AuthTokenService,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        val needAuthenticated = handlerCasted.hasMethodAnnotation(Authenticated::class.java)
        if (needAuthenticated) {
            val jwtToken = request.getHeader("Authorization")
            val result = authTokenService.verifyToken(jwtToken)
            if (!result) {
                throw AuthException("인증에 실패하였습니다")
            }
        }
        return super.preHandle(request, response, handler)
    }
}