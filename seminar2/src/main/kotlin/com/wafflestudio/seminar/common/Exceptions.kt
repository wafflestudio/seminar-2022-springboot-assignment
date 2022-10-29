package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class SeminarRequestBodyException(val errorList: List<FieldError>)
    : SeminarException("Wrong formatted request body given.", HttpStatus.BAD_REQUEST)