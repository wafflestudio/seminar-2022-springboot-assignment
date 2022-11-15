package com.wafflestudio.seminar.exception

import io.jsonwebtoken.Jwt
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@RestControllerAdvice
class SeminarExceptionHandler {
    
    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(e.status.value(), e.msg)
        return ResponseEntity(errorResponse, e.status)
    }

    @ExceptionHandler(value = [JwtException::class])
    fun handle(e: JwtException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.message!!)
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.bindingResult.fieldError!!.defaultMessage.toString())
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), "요청 형식에 맞게 요청해주세요.")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}