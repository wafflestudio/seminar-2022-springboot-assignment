package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class User404(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)

class User401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)
