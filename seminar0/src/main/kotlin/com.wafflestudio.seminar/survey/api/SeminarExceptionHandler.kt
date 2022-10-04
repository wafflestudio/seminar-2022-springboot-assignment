package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.jvm.Throws


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
    
    @ExceptionHandler(value = [SeminarException::class])
<<<<<<< HEAD:seminar0/src/main/kotlin/com/wafflestudio/seminar/survey/api/SeminarExceptionHandler.kt
    fun handle(
        e: SeminarException
    ): ResponseEntity<Any> {
       return ResponseEntity("DB에 없는 ID입니다", HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(value = [OsException::class])
    fun osHandle(
        e: OsException
    ): ResponseEntity<Any>{
        return ResponseEntity("OS DB에 없는 ID입니다", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [NameException::class])
    fun osHandle(
        e: NameException
    ): ResponseEntity<Any>{
        return ResponseEntity("OS DB에 없는 NAME입니다", HttpStatus.BAD_REQUEST)
    }

    class SeminarException() : RuntimeException()
    class OsException() : RuntimeException()
    class NameException() : RuntimeException()
    
=======
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }
>>>>>>> upstream/main:seminar0/src/main/kotlin/com.wafflestudio.seminar/survey/api/SeminarExceptionHandler.kt
}