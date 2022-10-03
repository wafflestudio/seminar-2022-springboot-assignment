package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)

class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class Seminar409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)
