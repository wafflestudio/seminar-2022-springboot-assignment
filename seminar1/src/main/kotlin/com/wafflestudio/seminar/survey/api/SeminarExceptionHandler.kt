package com.wafflestudio.seminar.survey.api


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SeminarExceptionHandler {

    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        if (e.message.contains("on field \'email\'")) {
            return ResponseEntity(
                "올바른 형식의 이메일 주소여야 합니다 - null 또는 빈 string 또는 이메일 형식이 아닙니다. - status 400",
                HttpStatus.BAD_REQUEST
            )
        } else if (e.message.contains("which is a non-nullable type")) {
            return ResponseEntity(
                "field 값이 잘못 됐거나 누락됐습니다. nickname과 email은 null일 수 없습니다 - status 400",
                HttpStatus.BAD_REQUEST
            )
        } else if (e.message.contains("NotBlank")) {
            return ResponseEntity(
                "nickname은 빈 string 일 수 없습니다 - status 400",
                HttpStatus.BAD_REQUEST
            )
        }
        else {
            return ResponseEntity(
                "request field에 필수적인 field가 누락되거나 null 값이 들어왔습니다 - status 400",
                HttpStatus.BAD_REQUEST
            )
        }
    }
    
}