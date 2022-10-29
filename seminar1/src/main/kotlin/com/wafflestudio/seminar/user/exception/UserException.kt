package com.wafflestudio.seminar.user.exception

import org.apache.catalina.User
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class UserException (
    val errorCode: ErrorCode, 
        ) : RuntimeException(errorCode.message) {
            val status: HttpStatus = errorCode.status
        }

class ExistsEmailException() : UserException(ErrorCode.ALREADY_EXISTS_EMAIL)

class IncorrectPasswordException() : UserException(ErrorCode.INCORRECT_PASSWORD)

class NotExistsUsers(): UserException(ErrorCode.NOT_EXISTS_USER)

class UserUnauthorized(): UserException(ErrorCode.USER_UNAUTHORIZED)