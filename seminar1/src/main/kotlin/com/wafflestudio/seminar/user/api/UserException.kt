package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus): RuntimeException(msg)

class UserNotFoundException: UserException("User not found", HttpStatus.NOT_FOUND)

class UserEmailAlreadyExistsException: UserException("User email already exists", HttpStatus.CONFLICT)

class UserPasswordIncorrectException: UserException("User password incorrect", HttpStatus.UNAUTHORIZED)