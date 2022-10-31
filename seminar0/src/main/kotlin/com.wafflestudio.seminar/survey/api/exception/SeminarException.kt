package com.wafflestudio.seminar.survey.api.exception

import org.springframework.http.HttpStatus

class SeminarException(errorCode: ErrorCode, data: String) : RuntimeException() {
    public val errorCode : ErrorCode = errorCode
    public val data : String = data
}