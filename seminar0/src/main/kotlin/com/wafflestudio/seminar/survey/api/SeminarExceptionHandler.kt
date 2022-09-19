package com.wafflestudio.seminar.survey.api

<<<<<<< HEAD
import com.wafflestudio.seminar.survey.exception.DataNotFoundException
=======
>>>>>>> f2ec6487e0048468ca29bdf2f1a7a8c35397fb87
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
<<<<<<< HEAD
import java.lang.Exception

@RestControllerAdvice
class SeminarExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: DataNotFoundException): ResponseEntity<Any>{
        return ResponseEntity(DataNotFoundException(e.errorCode), HttpStatus.NOT_FOUND)
    }
=======

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
    fun handle(e: SeminarException): ResponseEntity<Any> {
        TODO("적절한 ResponseBody & HttpStatus 조합을 내려줄 수도 있을 것 같다.")
    }

    inner class SeminarException() : RuntimeException()
>>>>>>> f2ec6487e0048468ca29bdf2f1a7a8c35397fb87
}