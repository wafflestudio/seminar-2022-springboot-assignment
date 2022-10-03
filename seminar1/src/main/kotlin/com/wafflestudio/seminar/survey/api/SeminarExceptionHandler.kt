package com.wafflestudio.seminar.survey.api

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
//    @ExceptionHandler(value = [Exception::class])
//    fun handle(e: Exception): ResponseEntity<Any> {
//        return ResponseEntity("Internal Server Error: " + e.message, HttpStatus.INTERNAL_SERVER_ERROR)
//    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
    
    @ExceptionHandler(value = [MissingKotlinParameterException::class])
    fun handle(e: MissingKotlinParameterException): ResponseEntity<Any> {
        return ResponseEntity("necessary info missing", HttpStatus.BAD_REQUEST)
    }
}