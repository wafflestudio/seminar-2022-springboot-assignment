package com.wafflestudio.seminar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthConfig {

    /**
     * Configuration 클래스에서 Bean을 선언하면,
     * @Component 와 같이 스프링에서 관리해주는 클래스, 즉 빈(Bean)을 만들 수 있어요.
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}