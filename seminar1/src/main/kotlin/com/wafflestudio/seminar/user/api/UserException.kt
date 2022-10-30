package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class User409(msg: String) : UserException(msg, HttpStatus.CONFLICT)

class User404(msg: String) : UserException(msg, HttpStatus.BAD_REQUEST)

class User401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)