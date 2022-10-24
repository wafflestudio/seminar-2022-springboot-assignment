package com.wafflestudio.seminar.user.exception

import com.wafflestudio.seminar.common.ErrorCode

open class UserException(): RuntimeException() {
    open val errorCode: ErrorCode = ErrorCode.UNAUTHORIZED_USER
}

class InvalidPasswordException: UserException() {
    override val errorCode: ErrorCode = ErrorCode.INVALID_PASSWORD
}

class EmailDuplicatedException() : UserException() {
    override val errorCode: ErrorCode = ErrorCode.EMAIL_DUPLICATED
}

class UserNotFoundException: UserException() {
    override val errorCode: ErrorCode = ErrorCode.USER_NOT_FOUND
}

class UserUnauthorizedException: UserException() {
    override val errorCode: ErrorCode = ErrorCode.UNAUTHORIZED_USER
}