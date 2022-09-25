package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) 
{
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "C002", "Invalid Password"),
    
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "C004", "Unauthorized user"),
    
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "User not found"),
    
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "C001", "Email is duplicated"),
}