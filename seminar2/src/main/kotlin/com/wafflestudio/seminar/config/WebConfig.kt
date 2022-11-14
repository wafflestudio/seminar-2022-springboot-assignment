package com.wafflestudio.seminar.config

import com.wafflestudio.seminar.common.LoginUserArgumentResolver
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthTokenExtractor
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class WebConfig(
    private val tokenVerifyInterceptor: TokenVerifyInterceptor,
    private val loginUserArgumentResolver: LoginUserArgumentResolver
) : WebMvcConfigurer {

    /**
     * TODO 세미나 레포지토리를 참고해서,
     *   헤더를 통한 JWT 인증이 가능하게끔 적절한 컴포넌트들을 구성해주세요.
     */

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenVerifyInterceptor)
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns("/api/v1/signup", "/api/v1/signin")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserArgumentResolver)
    }

}

@Component
class TokenVerifyInterceptor(
    private val authTokenService: AuthTokenService
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method
        // 사전요청 => pass
        if (method.equals(HttpMethod.OPTIONS)) return true
        // Extract Token from Http Request
        val token: String? = AuthTokenExtractor.extract(request)
        token?.let {
            authTokenService.verifyToken(it)
        } ?: throw AuthException("Authentication Failed")

        // TODO: ???
        return true
    }
}