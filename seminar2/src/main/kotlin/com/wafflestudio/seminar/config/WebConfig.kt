package com.wafflestudio.seminar.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class WebConfig {

    /**
     * TODO 세미나 레포지토리를 참고해서,
     *   헤더를 통한 JWT 인증이 가능하게끔 적절한 컴포넌트들을 구성해주세요.
     */


    @PersistenceContext
    var em: EntityManager? = null

    @Bean
    fun jpaQueryFactory(): JPAQueryFactory? {
        return JPAQueryFactory(em)
    }

    

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
    class AuthArgumentResolver : HandlerMethodArgumentResolver {
        override fun supportsParameter(parameter: MethodParameter): Boolean {
            return parameter.hasParameterAnnotation(UserContext::class.java)
                    && parameter.parameterType == Long::class.java
        }

        override fun resolveArgument(
                parameter: MethodParameter,
                mavContainer: ModelAndViewContainer?,
                webRequest: NativeWebRequest,
                binderFactory: WebDataBinderFactory?
        ): Any? {
            parameter.hasMethodAnnotation(UserContext::class.java)
            return (webRequest as ServletWebRequest).request.getAttribute("userId")
        }
    }

    @Configuration
    class AuthInterceptor(
            private val authTokenService: AuthTokenService
    ) : HandlerInterceptor {
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val handlerCasted = (handler as? HandlerMethod) ?: return true
            if (handlerCasted.hasMethodAnnotation(Authenticated::class.java)) {
                val authToken = request.getHeader("Authorization") ?: throw Seminar401("토큰 인증 적절하지 않음")
                val userId = authTokenService.getCurrentUserId(authToken)
                request.setAttribute("userId", userId)
            }

            return super.preHandle(request, response, handler)
        }
    }
}