package com.wafflestudio.seminar.common

import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class SeminarExceptionHandler {
    
    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception) : ResponseEntity<Map<String, Any>> {
        return createErrorInfo(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.message!!
        )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errorMessage : String = if (e.message.contains("on field \'email\'")) {
            "올바른 형식의 이메일 주소여야 합니다 - null 또는 빈 string 또는 이메일 형식이 아닙니다"
        } else if (e.message.contains("NotBlank") && e.message.contains("username")) {
            "username은 빈 string 일 수 없습니다"
        } else if (e.message.contains("NotBlank") && e.message.contains("name")) {
            "name은 빈 string 일 수 없습니다"
        } else if (e.message.contains("count") || e.message.contains("capacity")) {
            "count와 capacity는 양수여야 합니다"
        } else {
            "잘못된 형식의 field 값이 들어왔거나 request field에 필수적인 field가 누락되거나 null 값이 들어왔습니다"
        }
        return createErrorInfo(HttpStatus.BAD_REQUEST, errorMessage)
    }
    
    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handle(e: HttpMessageNotReadableException) : ResponseEntity<Map<String, Any>> {
        val errorMessage : String = if (e.message!!.contains("which is a non-nullable type")) {
            "field 값이 잘못 됐거나 누락됐습니다."
        } else if (e.message!!.contains("Enum class: [PARTICIPANT, INSTRUCTOR, BOTH")) {
            "role은 오직 PARTICIPANT 혹은 INSTRUCTOR 혹은 BOTH 뿐입니다"
        } else {
            "잘못된 형식의 field 값이 들어왔거나 request field에 필수적인 field가 누락되거나 null 값이 들어왔습니다"
        }
        return createErrorInfo(HttpStatus.BAD_REQUEST, errorMessage)
    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Map<String, Any>> {
        return createErrorInfo(
            e.status, e.message!!
        )
    }
    
    @ExceptionHandler(value = [AuthException::class])
    fun handle(e: AuthException): ResponseEntity<Map<String, Any>> {
        return createErrorInfo(e.status, e.message!!)
    }
    
    private fun createErrorInfo(status: HttpStatus, error: String) : ResponseEntity<Map<String, Any>> {
        return ResponseEntity(
            mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to status.value(),
            "error" to error
            ),
            status
        )
    }
}