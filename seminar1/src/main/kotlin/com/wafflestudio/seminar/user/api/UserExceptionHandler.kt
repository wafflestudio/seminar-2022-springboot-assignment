package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.SeminarException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    
    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
}