package com.wafflestudio.seminar.survey.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
//    @ExceptionHandler(value = [Exception::class])
//    fun handle(e: Exception): ResponseEntity<Any> {
//        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
//    }
//
//    @ExceptionHandler(value = [SeminarException::class])
//    fun handle(e: SeminarException): ResponseEntity<Any> {
//        return ResponseEntity(e.message, e.status)
//    }
    @ExceptionHandler(value = [SeminarException::class])
        fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
}