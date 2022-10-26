package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)
