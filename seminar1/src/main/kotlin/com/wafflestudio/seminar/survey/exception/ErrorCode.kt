package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

enum class ErrorCode (
    val status: HttpStatus, val message: String
    ) {
    INCORRECT_INPUT(HttpStatus.BAD_REQUEST, "Please submit all exp or OS"),
    INCORRECT_OSNAME(HttpStatus.NOT_FOUND, "Please submit correct os name"),
    ALREADY_EXISTS_SURVEY(HttpStatus.CONFLICT, "You already submit survey")
}