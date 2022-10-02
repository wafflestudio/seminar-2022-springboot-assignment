package com.wafflestudio.seminar.user.api.request

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class DuplicatedEmailException(msg: String) : UserException(msg, HttpStatus.CONFLICT)

class UnAuthorizedException(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)

class UserNotFoundException(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)

class BadRequestException(msg: String) : UserException(msg, HttpStatus.BAD_REQUEST)

