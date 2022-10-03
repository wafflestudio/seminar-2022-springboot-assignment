package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.*

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
    }
    
    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    inner class SeminarException(message: String) : RuntimeException(message)
}