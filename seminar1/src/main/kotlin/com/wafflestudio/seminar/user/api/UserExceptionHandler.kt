package com.wafflestudio.seminar.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(value = [CreateUserException::class])
    fun userCreationExceptionHandler(createUserException: CreateUserException): ResponseEntity<Any> {
        when (createUserException) {
            is CreateUserRequestBodyException ->
                return ResponseEntity(
                        object {
                            val msg = createUserException.msg
                            val errors = createUserException.errors.map{it.defaultMessage}
                        },
                        createUserException.status
                )
            else ->
                return ResponseEntity(createUserException.msg, createUserException.status)
        }
    }
    
    @ExceptionHandler(value = [LoginException::class])
    fun loginExceptionHandler(loginException: LoginException): ResponseEntity<Any> {
        return ResponseEntity(loginException.msg, loginException.status)
    }
    
    @ExceptionHandler(value = [GetUserException::class])
    fun getUserExceptionHandler(getUserException: GetUserException): ResponseEntity<Any> {
        return ResponseEntity(getUserException.msg, getUserException.status)
    }
}