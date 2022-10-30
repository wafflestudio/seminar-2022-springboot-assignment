package com.wafflestudio.seminar.common

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

}