package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableAspectJAutoProxy
@EnableJpaAuditing
@SpringBootApplication
class SeminarOneApplication

fun main(args: Array<String>) {
    runApplication<SeminarOneApplication>(*args)
}