package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [SeminarUserException::class])
    fun handle(e: SeminarUserException): ResponseEntity<Any>{
        return ResponseEntity(e.message, e.status)
    }
}