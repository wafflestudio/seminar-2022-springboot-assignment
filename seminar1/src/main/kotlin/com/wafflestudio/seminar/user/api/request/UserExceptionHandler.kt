package com.wafflestudio.seminar.user.api.request

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
    
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = HashMap<String, String>()
        e.fieldErrors.forEach { errors[it.field] = it.defaultMessage!! }
        return ResponseEntity.badRequest().body(errors)
    }
    
    @ExceptionHandler(value = [MissingRequestHeaderException::class])
    fun handle(e: MissingRequestHeaderException): ResponseEntity<Any> {
        return ResponseEntity("사용자를 식별할 수 없습니다", HttpStatus.FORBIDDEN)
    }
    
    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun handle(e: MethodArgumentTypeMismatchException): ResponseEntity<Any> {
        return ResponseEntity(e.name + " requires " + e.requiredType, HttpStatus.BAD_REQUEST)
    }
}