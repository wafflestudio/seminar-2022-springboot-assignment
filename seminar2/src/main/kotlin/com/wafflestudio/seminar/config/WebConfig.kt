package com.wafflestudio.seminar.config

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import io.jsonwebtoken.JwtException
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
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
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
         registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }
}

@Configuration
class AuthArgumentResolver(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasUserContextAnnotation = parameter.hasParameterAnnotation(UserContext::class.java)
        val hasUserEntityType = UserEntity::class.java.isAssignableFrom(parameter.parameterType)
        return hasUserContextAnnotation && hasUserEntityType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java) as HttpServletRequest
        val jwt = request.getHeader("Authorization")
        val userEmail = authTokenService.getCurrentUserEmail(jwt)
        return userRepository.findByEmail(userEmail) ?: throw JwtException("잘못된 토큰입니다.")
    }
}

@Configuration
class AuthInterceptor(
    private val authTokenService: AuthTokenService,
): HandlerInterceptor {
    
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        
        if (handlerCasted.hasMethodAnnotation(Authenticated::class.java)) {
            val jwtToken = getJwtToken(request)
            authTokenService.verifyToken(jwtToken)
        }
        
        return super.preHandle(request, response, handler)
    }

    private fun getJwtToken(request: HttpServletRequest): String {
        return request.getHeader("Authorization") ?: throw JwtException("Authorization 헤더가 존재하지 않습니다.")
    }
}