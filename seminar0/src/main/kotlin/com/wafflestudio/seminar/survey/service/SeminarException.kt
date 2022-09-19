package com.wafflestudio.seminar.survey.service

import org.springframework.http.HttpStatus

class SeminarException(
    override val message: String?,
    val status: HttpStatus
) : RuntimeException(message)