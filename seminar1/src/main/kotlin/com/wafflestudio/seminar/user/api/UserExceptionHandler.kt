package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any>{
        return ResponseEntity(e.errorMessage, e.status)
    }
    
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any>{
        return ResponseEntity(e.bindingResult.allErrors[0].defaultMessage, HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(value = [MissingRequestHeaderException::class])
    fun handle(e: MissingRequestHeaderException): ResponseEntity<Any>{
        return ResponseEntity("아이디를 입력하세요.", HttpStatus.FORBIDDEN)
    }
}