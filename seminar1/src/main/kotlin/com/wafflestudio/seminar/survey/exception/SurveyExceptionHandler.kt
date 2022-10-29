package com.wafflestudio.seminar.survey.exception

import com.wafflestudio.seminar.user.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SurveyExceptionHandler{
    
    @ExceptionHandler(value = [SurveyException::class])
    fun handlerSurveyException(e: SurveyException): ResponseEntity<Any> {
        val errorCode = e.errorCode
        val errorResponse = ErrorResponse(errorCode = errorCode.name, message = errorCode.message)
        return ResponseEntity(errorResponse, errorCode.status)
    }
}
