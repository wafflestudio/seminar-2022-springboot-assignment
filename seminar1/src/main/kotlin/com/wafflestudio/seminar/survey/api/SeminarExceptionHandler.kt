package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.common.ErrorResponse
import com.wafflestudio.seminar.user.exception.UserException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException


@RestControllerAdvice
class SeminarExceptionHandler {

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

    
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handle(e: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val message = e.constraintViolations
            .stream()
            .map { obj: ConstraintViolation<*> -> obj.message }
            .collect(Collectors.toList())
            .first()
        return ResponseEntity(ErrorResponse(HttpStatus.BAD_REQUEST, "C000", message), HttpStatus.BAD_REQUEST)
    }
}