package com.wafflestudio.seminar.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.headers()
            .frameOptions().disable()
        http.authorizeRequests()
            .antMatchers("/h2-console/**").permitAll();
        
        http.authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .httpBasic()
            .and()
                .csrf().disable()
    }

}