package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.SeminarException
import org.springframework.http.HttpStatus

class AuthException(val msg: String = "") : SeminarException(msg, HttpStatus.UNAUTHORIZED)