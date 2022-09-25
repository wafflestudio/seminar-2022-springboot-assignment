package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException (msg: String, val status: HttpStatus) : RuntimeException(msg)


class User401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)
class User404(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)
class User409(msg: String) : UserException(msg, HttpStatus.CONFLICT)
class User500(msg: String) : UserException(msg, HttpStatus.INTERNAL_SERVER_ERROR)
