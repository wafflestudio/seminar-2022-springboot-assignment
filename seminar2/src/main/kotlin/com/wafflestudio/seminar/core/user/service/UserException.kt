package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.SeminarException
import org.springframework.http.HttpStatus

open class UserException(msg: String, status: HttpStatus): SeminarException(msg, status)

class UserException409(msg: String = ""): UserException(msg, HttpStatus.CONFLICT)
class UserException400(msg: String = ""): UserException(msg, HttpStatus.BAD_REQUEST)