package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class APIExceptionHandler : ResponseEntityExceptionHandler() {
    
    @ExceptionHandler
    protected fun generalException(e: Exception, request: WebRequest) : ResponseEntity<Any> {
        val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        return super.handleExceptionInternal(
            e, "INTERNAL SERVER ERROR - 500 Error", HttpHeaders.EMPTY, status, request
        )
    }
    
    @ExceptionHandler(APIException::class)
    protected fun badRequestException(e: APIException, request: WebRequest) : ResponseEntity<Any> {
        return super.handleExceptionInternal(
            e, e.errorResponse.message, HttpHeaders.EMPTY, e.errorResponse.status, request
        )
    }
}