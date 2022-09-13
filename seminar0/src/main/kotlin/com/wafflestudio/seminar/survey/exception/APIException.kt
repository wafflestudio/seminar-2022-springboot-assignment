package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus


class APIException : RuntimeException {
    lateinit var errorResponse: ErrorResponse
    
    constructor(): super() {}
    
    constructor(status: HttpStatus, message: String) : super(message) {
        errorResponse = ErrorResponse(status, message)
    }
    
    constructor(message: String) : super(message){
        errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message)
    }
}