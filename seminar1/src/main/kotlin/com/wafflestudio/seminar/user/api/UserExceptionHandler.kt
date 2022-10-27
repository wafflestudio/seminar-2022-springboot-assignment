package com.wafflestudio.seminar.user.api

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserExceptionHandler {
    
    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<Any> {
        val droppedField: String = "\\[(.*?)\\]".toRegex().findAll(e.message.toString())
            .map{it.groupValues[1]}.last()
        return ResponseEntity("request에 필수적인 $droppedField field가 누락됐습니다", HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(value = [MissingRequestHeaderException::class])
    fun handle(e: MissingRequestHeaderException) : ResponseEntity<Any> {
        return ResponseEntity("X-User-ID header field 값이 필요합니다 - status 403", HttpStatus.FORBIDDEN)
    }
    
    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
    
}