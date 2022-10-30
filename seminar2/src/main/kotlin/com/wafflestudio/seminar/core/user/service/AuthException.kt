package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.SeminarException
import org.springframework.http.HttpStatus

class AuthException(msg: String = "") : SeminarException(msg, HttpStatus.UNAUTHORIZED)