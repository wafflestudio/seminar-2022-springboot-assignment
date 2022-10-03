package com.wafflestudio.seminar.user.api.request

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar404(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)

class Seminar400(msg: String) : UserException(msg, HttpStatus.BAD_REQUEST)

class Seminar409(msg: String) : UserException(msg, HttpStatus.CONFLICT)

class Seminar401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)