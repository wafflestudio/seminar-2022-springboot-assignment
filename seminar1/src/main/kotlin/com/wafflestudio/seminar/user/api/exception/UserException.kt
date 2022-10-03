package com.wafflestudio.seminar.user.api.exception

import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class UserException400(msg: String = "400 BAD REQUEST") : UserException(msg, HttpStatus.BAD_REQUEST)
class UserException401(msg: String = "401 UNAUTHORISED") : UserException(msg, HttpStatus.UNAUTHORIZED)
class UserException403(msg: String = "403 NOT FOUND") : UserException(msg, HttpStatus.FORBIDDEN)
class UserException404(msg: String = "404 NOT FOUND") : UserException(msg, HttpStatus.NOT_FOUND)
class UserException409(msg: String = "409 CONFLICT") : UserException(msg, HttpStatus.CONFLICT)
