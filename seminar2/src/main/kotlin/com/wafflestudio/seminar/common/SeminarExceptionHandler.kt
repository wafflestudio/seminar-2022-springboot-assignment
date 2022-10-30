package com.wafflestudio.seminar.common

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
    
    @ExceptionHandler(SeminarException::class)
    fun handle(e: SeminarException) =
        ResponseEntity(e.message, e.status)
    
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handle(e: DataIntegrityViolationException) =
        ResponseEntity(e.message, HttpStatus.CONFLICT)
    
}