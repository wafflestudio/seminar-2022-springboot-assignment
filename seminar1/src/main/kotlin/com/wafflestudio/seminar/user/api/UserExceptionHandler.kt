package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler (value = [SameEmailException::class])
    fun handle(e: SameEmailException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }
    
    @ExceptionHandler (value = [DiffPasswordException::class])
    fun handle(e: DiffPasswordException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
    }
    
    @ExceptionHandler (value = [UserNotExistException::class])
    fun handle(e: UserNotExistException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler (value = [MissingHeaderException::class])
    fun handle(e: MissingHeaderException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler (value = [MissingValueException::class])
    fun handle(e: MissingValueException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }
}