package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.common.ErrorResponse
import com.wafflestudio.seminar.user.exception.UserException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.of(e.errorCode)
        return ResponseEntity(errorResponse, e.errorCode.status)
    }
    
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult.fieldError?.defaultMessage!!
        return ResponseEntity(ErrorResponse(HttpStatus.BAD_REQUEST, "C000", message), HttpStatus.BAD_REQUEST)
    }
}