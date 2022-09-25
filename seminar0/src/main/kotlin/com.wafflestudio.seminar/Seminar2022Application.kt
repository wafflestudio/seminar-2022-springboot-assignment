package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication(scanBasePackages = ["com.wafflestudio.seminar.survey"])
class Seminar2022Application

fun main(args: Array<String>) {
    runApplication<Seminar2022Application>(*args)
}