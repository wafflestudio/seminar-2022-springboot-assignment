package com.wafflestudio.seminar.user.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class UserExceptionHandler{
    @ExceptionHandler(UserException::class)
    fun handleUserException(e: UserException): ResponseEntity<Any> {
        val errorCode = e.errorCode
        val errorResponse = ErrorResponse(errorCode = errorCode.name, message = errorCode.message)
        return ResponseEntity(errorResponse, errorCode.status)
    }
}
