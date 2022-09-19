package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Error

class DataNotFoundException(val errorCode: ErrorCode): RuntimeException() {
    val status: HttpStatus
        get() = errorCode.status
    override val message = errorCode.message
}