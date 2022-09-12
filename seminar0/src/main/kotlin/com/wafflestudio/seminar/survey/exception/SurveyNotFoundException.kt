package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

class SurveyNotFoundException(val errorCode: ErrorCode): RuntimeException() {
    val status: HttpStatus
        get() = errorCode.status
}