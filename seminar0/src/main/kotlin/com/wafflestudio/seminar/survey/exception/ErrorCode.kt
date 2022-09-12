package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, val message: String) {
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 설문 결과를 찾을 수 없습니다."),
    OS_NOT_FOUND(HttpStatus.NOT_FOUND, "OS 정보를 찾을 수 없습니다."),
}