package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.exception.OSNotFoundException
import com.wafflestudio.seminar.survey.exception.SurveyResultNotFoundException
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

    /**
     * 상황에 맞는 적절한 예외를 미리 준비해둘 수도 있을 것 같아요.
     * 예외들은 어느 패키지에 있는게 적절할까요?
     * 예외는 어떤 정보를 공통적으로 담고 있을까요?
     */
    @ExceptionHandler(value = [SurveyResultNotFoundException::class])
    fun handle(e: SurveyResultNotFoundException): ResponseEntity<Any> {
        return ResponseEntity("해당 설문 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler(value = [OSNotFoundException::class])
    fun handle(e: OSNotFoundException): ResponseEntity<Any> {
        return ResponseEntity("해당 OS를 찾을 수 없습니다", HttpStatus.NOT_FOUND)
    }
}