package com.wafflestudio.seminar.config

import com.wafflestudio.seminar.core.user.service.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor
) : WebMvcConfigurer {

    /**
     * TODO 세미나 레포지토리를 참고해서,
     *   헤더를 통한 JWT 인증이 가능하게끔 적절한 컴포넌트들을 구성해주세요.
     */

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/v1/*")
    }
}