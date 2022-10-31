package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestHandler
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
    
    // TODO as-you-wish
    
    @ExceptionHandler(Exception::class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(e: Exception): ResponseEntity<Any> = ResponseEntity(
        e.message + e.javaClass.name,
        HttpStatus.INTERNAL_SERVER_ERROR,
    )
    
    @ExceptionHandler(SeminarException::class)
    fun handle(e: SeminarException): ResponseEntity<Any> = ResponseEntity(
        e.message,
        e.status,
    )
    
    // on validation failure
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> = ResponseEntity(
        e.bindingResult.allErrors[0].defaultMessage,
        HttpStatus.BAD_REQUEST
    )
    
    // on not supported method request
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException): ResponseEntity<Any> = ResponseEntity(
        e.message,
        HttpStatus.BAD_REQUEST
    )
    
    // on request with invalid enum value 
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<Any> = ResponseEntity(
        e.message,
        HttpStatus.BAD_REQUEST
    )
    
    
}