package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.exception.DataNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: DataNotFoundException): ResponseEntity<Any>{
        return ResponseEntity(DataNotFoundException(e.errorCode), HttpStatus.NOT_FOUND)
    }
}