package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class SeminarUserException(msg: String, val status: HttpStatus) : IllegalAccessException(msg)
