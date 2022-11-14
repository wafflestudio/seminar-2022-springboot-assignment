package com.wafflestudio.seminar.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {

    // as-you-wish
    @ExceptionHandler(value = [SeminarException::class])
    fun handleUserException(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity<Any>(e.message, e.status)
    }

    @ExceptionHandler(value = [SeminarRequestBodyException::class])
    fun handleSeminarRequestBodyException(e: SeminarRequestBodyException): ResponseEntity<Any> {
        return ResponseEntity<Any>(
            object {
                val message = e.message
                val errorList = e.errorList.map { it.defaultMessage }
            },
            e.status
        )
    }
}