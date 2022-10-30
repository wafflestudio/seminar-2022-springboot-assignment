package com.wafflestudio.seminar.common

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.boot.json.JsonParseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class SeminarExceptionHandler {
    // 모든 예외에 대한 처리
    @ExceptionHandler(value = [Exception::class])
    fun exception(e: Exception): ResponseEntity<Any> {
        println("!특정 예외 발생!")
        println(e.javaClass.name) // 예외가 어디서 발생했는 지 찾음
        println(e.localizedMessage) // 예외 메시지 출력
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류 발생")
    }
    
    @ExceptionHandler(value = [SeminarException::class])
    fun seminarException(e: SeminarException): ResponseEntity<Any> {
        return ResponseEntity(e.message, e.status)
    }

    // request validation
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        val cause = ex.cause
        val message: String = if (cause is JsonParseException) "올바른 JSON 형식이 아닙니다."
            else if (cause is JsonMappingException) {
                if (cause is InvalidFormatException) {
                    "[${cause.path.joinToString(", ") { it.fieldName }}] 타입이 올바르지 않습니다."
                } else {
                    "[${cause.path.joinToString(", ") { it.fieldName }}] 값은 필수입니다."
                }
            } else {
                "JSON 파싱 중 알 수 없는 오류입니다."
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(e: java.lang.Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("헤더가 올바르지 않습니다.")
    }

    @ExceptionHandler(UnsupportedJwtException::class)
    fun handleUnsupportedJwtException(e: java.lang.Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰의 구성 혹은 형식이 올바르지 않습니다.")
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(e: java.lang.Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰의 구조가 올바르지 않습니다.")
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun ExpiredJwtException(e: java.lang.Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.")
    }

    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(e: java.lang.Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰의 서명이 올바르지 않습니다.")
    }
}