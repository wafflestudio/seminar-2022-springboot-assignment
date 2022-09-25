package com.wafflestudio.seminar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter
import java.util.*
import javax.servlet.Filter


@Configuration
class SecurityConfig {
    
    @Bean
    fun configure(http: HttpSecurity): DefaultSecurityFilterChain? {
        http.httpBasic().disable()
        http.rememberMe()
        
        http.authorizeRequests()
            .antMatchers("/", "/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf()
            .disable()
                // 이 csrf를 disable 해줘야 post request가 날라간다
//            .ignoringAntMatchers("/h2-console/**")
//            .and()
            .headers()
            .frameOptions().disable();
        // frameOptions().disable() 은 지워주는게 좋다
//            .antMatchers("/", "/os", "/survey", "/signup", "/api").permitAll()
        return http.build()
    }

//    @Bean
//    fun configure(web: WebSecurity): Filter? {
//        web.ignoring().antMatchers("/h2-console/**");
//        return web.build()
//    }
}