package com.wafflestudio.seminar.survey.api

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
    @ExceptionHandler(value = [OsIdException::class])
    fun handle(e: OsIdException): ResponseEntity<Any> {
        return ResponseEntity("id가 존재하지 않습니다", HttpStatus.BAD_REQUEST)
    }

    /**
     * 상황에 맞는 적절한 예외를 미리 준비해둘 수도 있을 것 같아요.
     * 예외들은 어느 패키지에 있는게 적절할까요?
     * 예외는 어떤 정보를 공통적으로 담고 있을까요?
     */
<<<<<<< HEAD:seminar0/src/main/kotlin/com/wafflestudio/seminar/survey/api/SeminarExceptionHandler.kt
    @ExceptionHandler(value = [OsNameException::class])
    fun handle(e: OsNameException): ResponseEntity<Any> {
        return ResponseEntity("name이 존재하지 않습니다", HttpStatus.BAD_REQUEST)
    }

    inner class OsIdException() : RuntimeException()
    inner class OsNameException() : RuntimeException()
=======
    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }
>>>>>>> 07ef91823c7021a8efff88137b9982ff643dc776:seminar0/src/main/kotlin/com.wafflestudio.seminar/survey/api/SeminarExceptionHandler.kt
}