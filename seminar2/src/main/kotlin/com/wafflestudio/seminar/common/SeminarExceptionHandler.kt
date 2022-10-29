package com.wafflestudio.seminar.common

import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.UserException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.ServletException

@RestControllerAdvice
class SeminarExceptionHandler {
    
    // as-you-wish
    @ExceptionHandler(value = [SeminarException::class])
    fun handleUserException(e: UserException): ResponseEntity<Any> {
        return ResponseEntity<Any>(e.message, e.status)
    }
    
    @ExceptionHandler(value = [SeminarRequestBodyException::class])
    fun handleSeminarRequestBodyException(e: SeminarRequestBodyException): ResponseEntity<Any> {
        return ResponseEntity<Any>(
                object {
                    val message = e.message
                    val errorList = e.errorList.map {it.defaultMessage}
                },
                e.status
        )
    }
}