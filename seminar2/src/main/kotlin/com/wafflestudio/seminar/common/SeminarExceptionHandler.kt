package com.wafflestudio.seminar.common

import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<Any> {
        return ResponseEntity("잘못된 형식의 입력입니다.", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [AuthException::class])
    fun handle(e: AuthException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
    
}