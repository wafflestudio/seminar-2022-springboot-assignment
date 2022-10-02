package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus

open class SeminarException(val type: SeminarExceptionType, val status: HttpStatus) : RuntimeException()
enum class SeminarExceptionType(val message: String) {
    NotExistSurveyForId("해당 Id를 가진 설문조사가 존재하지 않습니다."),

    InputNeedsOSName("운영체제 이름을 입력해주세요."),
    NeedsAuthetication("로그인이 필요합니다."),
    NotExistUserId("잘못된 UserId 입니다."),
    ExistUserSurvey("이미 설문조사를 진행한 유저입니다."),
    InputNeedsSurvey("설문조사에 필수적인 값들을 입력해주세요."),

    NotExistOSForId("해당 Id를 가진 운영체제가 존재하지 않습니다."),
    NotExistOSForName("해당 이름을 가진 운영체제가 존재하지 않습니다.")
}


class Seminar400(val excType: SeminarExceptionType) : SeminarException(excType, HttpStatus.BAD_REQUEST)
class Seminar403(val excType: SeminarExceptionType) : SeminarException(excType, HttpStatus.FORBIDDEN)
class Seminar404(val excType: SeminarExceptionType) : SeminarException(excType, HttpStatus.NOT_FOUND)


