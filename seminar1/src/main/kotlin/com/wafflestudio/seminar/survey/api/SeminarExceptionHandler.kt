package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
//        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
        print("오류:")
        println(e)
        return ResponseEntity("An error has occurred.", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
}