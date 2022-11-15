package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SeminarAssignmentTwo

fun main(args: Array<String>) {
    runApplication<SeminarAssignmentTwo>(*args)
}