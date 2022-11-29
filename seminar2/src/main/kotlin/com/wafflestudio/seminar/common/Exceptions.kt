package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)
class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)