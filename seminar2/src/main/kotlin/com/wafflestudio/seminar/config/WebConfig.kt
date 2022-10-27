package com.wafflestudio.seminar.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


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

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.isFieldMatchingEnabled = true
        modelMapper.configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        return modelMapper
    }
}