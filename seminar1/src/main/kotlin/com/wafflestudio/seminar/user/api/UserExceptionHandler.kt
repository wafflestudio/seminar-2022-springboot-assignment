package com.wafflestudio.seminar.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [CreateUserException::class])
    fun userCreationExceptionHandler(createUserException: CreateUserException): ResponseEntity<String> {
        return ResponseEntity(createUserException.msg, createUserException.status)
    }
    
    @ExceptionHandler(value = [LoginException::class])
    fun loginExceptionHandler(loginException: LoginException): ResponseEntity<String> {
        return ResponseEntity(loginException.msg, loginException.status)
    }
    
    @ExceptionHandler(value = [GetUserException::class])
    fun getUserExceptionHandler(getUserException: GetUserException): ResponseEntity<String> {
        return ResponseEntity(getUserException.msg, getUserException.status)
    }
}