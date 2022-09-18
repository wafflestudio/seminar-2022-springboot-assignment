package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

enum class SeminarExceptionCode(val message: String, val status: HttpStatus) {
    NoOSError("해당 OS가 존재하지 않습니다", HttpStatus.NOT_FOUND),
    NoSurveyResultError("해당 설문 결과가 존재하지 않습니다", HttpStatus.NOT_FOUND)
}