package com.wafflestudio.seminar.user.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity("Internal Server Error: " + e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
}