package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SeminarTwoApplication

fun main(args: Array<String>) {
    runApplication<SeminarTwoApplication>(*args)
}