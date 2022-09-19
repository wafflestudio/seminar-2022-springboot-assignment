package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
    }
    
    @ExceptionHandler(value = [NoSuchElementException::class])
    fun handle(e: NoSuchElementException): ResponseEntity<Any> {
        return ResponseEntity("404 NOT FOUND", HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun handle(e: MethodArgumentTypeMismatchException): ResponseEntity<Any> {
        return ResponseEntity("400 BAD REQUEST", HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(value = [MissingServletRequestParameterException::class])
    fun handle(e: MissingServletRequestParameterException): ResponseEntity<Any> {
        return ResponseEntity("400 BAD REQUEST", HttpStatus.BAD_REQUEST)
    }

}