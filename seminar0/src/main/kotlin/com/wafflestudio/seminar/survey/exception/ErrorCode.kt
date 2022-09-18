package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, val message: String) {
    OS_NOT_FOUND(HttpStatus.NOT_FOUND, "OS를 찾을 수 없습니다."),
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "Survey를 찾을 수 없습니다.")
}