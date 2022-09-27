package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.SeminarException
import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class User409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)