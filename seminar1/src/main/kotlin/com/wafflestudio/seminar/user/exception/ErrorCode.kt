package com.wafflestudio.seminar.user.exception

import org.springframework.http.HttpStatus


enum class ErrorCode (
    val status: HttpStatus, val message: String
    ){
    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "Email is already exists"),
    
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is incorrect"),
    
    NOT_EXISTS_USER(HttpStatus.NOT_FOUND, "Not exists USER"),
    
    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "User unauthorized"),
    
}