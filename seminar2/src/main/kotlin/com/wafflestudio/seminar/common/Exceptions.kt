package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

class SeminarException (
    val errorCode: ErrorCode,
): RuntimeException()


enum class ErrorCode(
    val httpStatus: HttpStatus,
    val errorMessage: String,
) {
    
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "시간 초과로 인해 요청이 정상적으로 수행되지 않았습니다."),
    
    // 400 BAD_REQUEST: 잘못된 요청
    FULL_CAPACITY(HttpStatus.BAD_REQUEST, "수강정원이 초과되었습니다."),
    ALREADY_PARTICIPATE(HttpStatus.BAD_REQUEST, "현재 참여 중인 세미나입니다."),
    ALREADY_INSTRUCTED(HttpStatus.BAD_REQUEST, "현재 담당하는 세미나가 존재합니다."),
    ALREADY_DROPPED(HttpStatus.BAD_REQUEST, "중도포기했던 강좌로 재참여가 불가합니다."),
    NOT_PARTICIPATED_SEMINAR(HttpStatus.OK, "수강 이력이 존재하지 않습니다."),
    
    // 401 UNAUTHORIZED
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "일치하지 않는 비밀번호입니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다."),
    INVALID_REGISTER_REQUEST(HttpStatus.FORBIDDEN, "요청 정보가 잘못되었습니다."),
    EDIT_EMAIL_FORBIDDEN(HttpStatus.FORBIDDEN, "이메일은 수정할 수 없습니다."),
    EDIT_REGISTRATION_FORBIDDEN(HttpStatus.FORBIDDEN, "활성회원 여부는 수정할 수 없습니다."),
    MAKE_SEMINAR_FORBIDDEN(HttpStatus.FORBIDDEN, "세미나 생성 권한이 없습니다."),
    EDIT_SEMINAR_FORBIDDEN(HttpStatus.FORBIDDEN, "세미나 수정 권한이 없습니다."),
    INSTRUCTOR_CANNOT_DROP(HttpStatus.FORBIDDEN, "해당 세미나의 담당자로 드랍이 불가능합니다."),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "토큰의 유효 기간을 확인하세요."),
    NOT_BEARER_TYPE(HttpStatus.FORBIDDEN, "Bearer 토큰으로 입력해주세요."),
    NOT_REGISTERED(HttpStatus.FORBIDDEN, "활성회원이 아닙니다."),
    
    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    SEMINAR_NOT_FOUND(HttpStatus.NOT_FOUND, "세미나 정보를 찾을 수 없습니다."),
    
    // 409 CONFLICT
    EMAIL_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    ALREADY_PARTICIPANT(HttpStatus.CONFLICT, "이미 수강생 신분을 가지고 있습니다.")
}