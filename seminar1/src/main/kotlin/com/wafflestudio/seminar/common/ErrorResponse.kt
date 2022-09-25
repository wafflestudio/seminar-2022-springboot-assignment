package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val status: HttpStatus,
    val code: String,
    val message: String
) 
{
     companion object {
         fun of(errorCode: ErrorCode): ErrorResponse {
             return ErrorResponse(
                status = errorCode.status,
                 code = errorCode.code,
                 message = errorCode.message
             )
         }
     }
}