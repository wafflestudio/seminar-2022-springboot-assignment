package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class GetUserException(val msg: String, val status: HttpStatus): RuntimeException(msg)

class GetUserNotFindException: GetUserException("User not registered.", HttpStatus.BAD_REQUEST)

class GetUserUnauthorizedException: GetUserException("Unauthorized.", HttpStatus.UNAUTHORIZED)