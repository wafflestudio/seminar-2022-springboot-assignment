package com.wafflestudio.seminar

import com.wafflestudio.seminar.core.user.service.AuthProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SeminarTwoApplication

fun main(args: Array<String>) {
    runApplication<SeminarTwoApplication>(*args)
}