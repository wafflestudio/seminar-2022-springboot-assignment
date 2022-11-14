package com.wafflestudio.seminar.core.user.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.seminar.common.SeminarException
import org.springframework.http.HttpStatus

class AuthException(val msg: String = "") : SeminarException(msg, HttpStatus.UNAUTHORIZED) {
    fun toJson(): String = ObjectMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(this)
}