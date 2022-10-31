package com.wafflestudio.seminar.common

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [SignatureException::class])
    fun handle(e: SignatureException): ResponseEntity<Any> {
        return ResponseEntity("User Authentication Failed", HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [ExpiredJwtException::class])
    fun handle(e: ExpiredJwtException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<Any> {
        return ResponseEntity("role must be PARTICIPANT or INSTRUCTOR", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        return ResponseEntity(e.bindingResult.allErrors[0].defaultMessage, HttpStatus.BAD_REQUEST)
    }
}