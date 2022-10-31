package com.wafflestudio.seminar.config

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator
import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
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
class WebConfig (
    private val authInterceptor : AuthInterceptor,
    private val authArgumentResolver: AuthArgumentResolver
        ): WebMvcConfigurer{
    
    override fun addInterceptors(registry : InterceptorRegistry){
        registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }
    
}
@Configuration
class AuthArgumentResolver(
): HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasMethodAnnotation(Authenticated::class.java) && parameter.parameterType == 1L::class.javaObjectType // javaObjectType으로 해야 non primitive Type
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return (webRequest.nativeRequest as HttpServletRequest).getAttribute("userID")?: throw AuthException("회원가입이 안된 ID로 접근을 시도했습니다.")
    }

}
@Configuration
class AuthInterceptor(
    private val authService: AuthTokenService
):HandlerInterceptor{
    override fun preHandle(request: HttpServletRequest, response : HttpServletResponse, handler : Any): Boolean{
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        val needAuthentication = handlerCasted.hasMethodAnnotation(Authenticated::class.java)
        if(needAuthentication){
//            val authHeader = request.getHeader("USERNAME")
            val authHeader : String = request.getHeader("Authorization")
            authService.verifyToken(authHeader)
            val userID = authService.getCurrentUserId(authHeader)
            request.setAttribute("userID", userID)
        }
        return super.preHandle(request, response, handler)
        
    }
}