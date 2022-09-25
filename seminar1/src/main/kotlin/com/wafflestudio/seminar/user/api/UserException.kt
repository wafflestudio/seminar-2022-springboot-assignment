package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class User401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)

class User403(msg: String) : UserException(msg, HttpStatus.FORBIDDEN)

class User404(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)

class User409(msg: String) : UserException(msg, HttpStatus.CONFLICT)
