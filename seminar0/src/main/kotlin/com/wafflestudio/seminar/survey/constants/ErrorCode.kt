package com.wafflestudio.seminar.survey.constants

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

enum class ErrorCode(val status: HttpStatus, val message: String) {
    SURVEY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 설문조사 결과를 찾지 못했어요!"),
    OS_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 OS 정보를 찾지 못했어요!"),
    OS_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이름의 OS 정보를 찾지 못했어요!");
    
    fun getResponseEntity(): ResponseEntity<Any> {
        return ResponseEntity(this.message, this.status)
    }
}