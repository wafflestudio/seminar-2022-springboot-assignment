package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [ServletRequestBindingException::class])
    fun handleHeaderError(): ResponseEntity<Any>? {
        return ResponseEntity("유저 번호를 입력해주세요.", HttpStatus.FORBIDDEN)
    }
    
}