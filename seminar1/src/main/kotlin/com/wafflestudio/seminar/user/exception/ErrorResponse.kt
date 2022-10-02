package com.wafflestudio.seminar.user.exception

data class ErrorResponse (
    val errorCode: String,
    val message: String?
        )