package com.wafflestudio.seminar.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [UserException::class])
    fun userCreationExceptionHandler(userException: UserException): ResponseEntity<String> {
        return ResponseEntity(userException.msg, userException.status)
    }
}