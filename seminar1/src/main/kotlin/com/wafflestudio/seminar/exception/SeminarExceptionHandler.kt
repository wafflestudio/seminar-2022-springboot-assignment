package com.wafflestudio.seminar.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class SeminarExceptionHandler {

    @ExceptionHandler(value = [Exception::class])
    fun handle(e: Exception): ResponseEntity<Any> {
        val request : HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.request

        return ResponseEntity(
            ErrorResponse(
                status = 500,
                error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                message = "오류가 발생했어요!",
                path = request.requestURI
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [SeminarException::class])
    fun handle(e: SeminarException): ResponseEntity<Any> {
        val request : HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.request
        val queryString: String? = request.queryString

        return ResponseEntity(
            ErrorResponse(
                status = e.errorCode.httpStatus.value(),
                error = e.errorCode.httpStatus.reasonPhrase,
                message = e.errorCode.errorMessage,
                path = if (!queryString.isNullOrEmpty()) {
                    request.requestURI+'?'+request.queryString
                } else request.requestURI
            ),
            e.errorCode.httpStatus
        )
    }

    data class ErrorResponse (
        val status: Int,
        val error: String,
        val message: String,
        val path: String,
    )
}