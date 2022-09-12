package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.exception.ExceptionResponse
import com.wafflestudio.seminar.survey.exception.SurveyNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * ControllerAdvice 빈을 선언하고,
 * @ExceptionHandler 어노테이션을 통해 처리해주려는 예외를 다룰 수 있어요.
 */
@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
    }
    
    @ExceptionHandler
    fun handle(e: SurveyNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity(ExceptionResponse(e.errorCode), e.status)
    }
    
}