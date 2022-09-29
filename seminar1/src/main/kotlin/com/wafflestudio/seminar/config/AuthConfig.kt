package com.wafflestudio.seminar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
<<<<<<< Updated upstream
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
=======
import org.springframework.transaction.annotation.EnableTransactionManagement

>>>>>>> Stashed changes

@Configuration
@EnableTransactionManagement //@transactional을 사용하기 위해서는 @Configuration 클래스에 @EnableTransactionManagement 선언헤야 힘/
//@Transactional으로 생성된 프록시 객체는 @Transactional이 적용된 메소드가 호출될 경우,
//PlatformTransactionManager를 사용하여 트랜잭션을 시작하고, 정상 여부에 따라 Commit/Rollback 동작을 수행한다.
class AuthConfig {

    /**
     * Configuration 클래스에서 Bean을 선언하면,
     * @Component 와 같이 스프링에서 관리해주는 클래스, 즉 빈(Bean)을 만들 수 있어요.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain =
        httpSecurity
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .build()
}