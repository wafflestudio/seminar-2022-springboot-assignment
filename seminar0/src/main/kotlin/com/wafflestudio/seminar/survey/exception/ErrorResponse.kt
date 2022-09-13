package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus


class ErrorResponse(status: HttpStatus, message:String) {
    val status: HttpStatus = status
    val message: String = message
}