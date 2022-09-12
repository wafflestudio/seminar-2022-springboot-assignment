package com.wafflestudio.seminar.survey.exception

class ExceptionResponse(errorCode: ErrorCode) {
    val message = errorCode.message
}