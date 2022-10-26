package com.wafflestudio.seminar.exception

import org.springframework.http.HttpStatus

class SeminarException (
    val errorCode: ErrorCode,
): RuntimeException()


enum class ErrorCode(
    val httpStatus: HttpStatus,
    val errorMessage: String,
) {
    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "필수 정보 입력란을 확인해주세요."),

    // 401 UNAUTHORIZED
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "일치하지 않는 비밀번호입니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 불가합니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    OS_NOT_FOUND(HttpStatus.NOT_FOUND, "OS 정보를 찾을 수 없습니다."),
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 설문 결과를 찾을 수 없습니다."),

    // 409 CONFLICT
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 이메일 주소입니다."),
}