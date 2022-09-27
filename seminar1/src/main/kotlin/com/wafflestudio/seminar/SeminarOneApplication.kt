package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@EnableJpaAuditing
class SeminarOneApplication

fun main(args: Array<String>) {
    runApplication<SeminarOneApplication>(*args)
}