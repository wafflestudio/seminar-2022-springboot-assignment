package com.wafflestudio.seminar.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
import java.net.http.HttpHeaders
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/api/v1/signup")
    }
    
}


@Configuration
class AuthInterceptor(
    private val authTokenService: AuthTokenService,
): HandlerInterceptor {
    
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        val needAuthentication = handlerCasted.hasMethodAnnotation(Authenticated::class.java)
        
        if(needAuthentication){
            request.getHeader("Authorization") ?: throw AuthException("토큰이 없습니다")
            
            val authHeader : String = request.getHeader("Authorization")
            //토큰이 없는 상황이면?
            if(authHeader.isEmpty())
                throw AuthException("토큰이 잘못되었습니다.")
            
            val jwtToken: String = authHeader.substring(7, authHeader.length)
            
            try{
                val email: String = authTokenService.getCurrentUserId(jwtToken)
                request.setAttribute("email", email)   
                return true
                  
            }catch (e : MalformedJwtException){
                throw AuthException("토큰이 변조되었습니다.")
                
            }catch (e: ExpiredJwtException){
                throw AuthException("토큰이 만료되었습니다.")
            }
            
        }
        
        return super.preHandle(request, response, handler)
    }
}

