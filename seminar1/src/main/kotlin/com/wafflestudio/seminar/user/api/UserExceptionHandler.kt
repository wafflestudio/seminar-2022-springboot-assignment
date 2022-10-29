package com.wafflestudio.seminar.user.api

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
//    @ExceptionHandler(value = [Exception::class])
//    fun handle(e: Exception):ResponseEntity<Any> {
//        return ResponseEntity("오류가 발생했어요!", HttpStatus.INTERNAL_SERVER_ERROR)
//    }

    @ExceptionHandler(value = [DataIntegrityViolationException::class])
    fun handle(e: DataIntegrityViolationException): ResponseEntity<Any> {
        return ResponseEntity("동일한 이메일이 존재합니다.", HttpStatus.CONFLICT)
    }
    
    @ExceptionHandler(value = [UserException::class])
    fun handle(e: UserException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }
    
    @ExceptionHandler(value = [MissingRequestHeaderException::class])
    fun handle(e: MissingRequestHeaderException): ResponseEntity<Any> {
        return ResponseEntity("ID를 입력하지 않았습니다", HttpStatus.FORBIDDEN)
    }
}