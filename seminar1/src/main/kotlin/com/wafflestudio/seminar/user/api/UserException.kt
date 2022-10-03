package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(
    val errorMessage: String,
    val status: HttpStatus
): RuntimeException()

class DuplicateEmailException(
    errorMessage: String
): UserException(errorMessage, HttpStatus.CONFLICT)  

class UserNotFoundException(
    errorMessage: String
): UserException(errorMessage, HttpStatus.BAD_REQUEST)

class InvalidPasswordException(
    errorMessage: String
): UserException(errorMessage, HttpStatus.UNAUTHORIZED)
