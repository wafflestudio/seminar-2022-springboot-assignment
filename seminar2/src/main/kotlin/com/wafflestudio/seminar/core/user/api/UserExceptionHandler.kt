package com.wafflestudio.seminar.core.user.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
//    @ExceptionHandler(value = [Exception::class])
//    fun handle(e: Exception):ResponseEntity<Any> {
//        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
//    }
    
    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
}