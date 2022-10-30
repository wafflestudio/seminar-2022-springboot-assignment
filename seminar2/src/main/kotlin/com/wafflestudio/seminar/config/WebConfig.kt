package com.wafflestudio.seminar.config

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import io.jsonwebtoken.ExpiredJwtException
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
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .excludePathPatterns("/api/v1/signup")
            .excludePathPatterns("/api/v1/signin")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }
}

@Configuration
class AuthArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isAuthenticated: Boolean = parameter.hasParameterAnnotation(Authenticated::class.java)
        val isString = parameter.parameterType == String::class.java
        return isAuthenticated && isString
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        parameter.hasMethodAnnotation(UserContext::class.java)
        // TODO("어떤 값을 반환해서 넣어주면 될지 알아보자.")
        return null
    }
}

@Configuration
class AuthInterceptor(
    private val authTokenService: AuthTokenService
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        val jwtToken: String = request.getHeader("Authorization")
            ?: throw Seminar401("Authorization 헤더가 없습니다.")

        val isVerify: Boolean = authTokenService.verifyToken(jwtToken)
        if (!isVerify) throw ExpiredJwtException(null, null, "expired")

        println(jwtToken)
        val userId: Long = authTokenService.getCurrentUserId(jwtToken)
        request.setAttribute("userId", userId)
        return super.preHandle(request, response, handler)
    }
}