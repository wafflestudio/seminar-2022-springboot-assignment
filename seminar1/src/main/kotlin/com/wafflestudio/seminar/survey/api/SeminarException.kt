package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus

open class SeminarException(val type: SeminarExceptionType, val status: HttpStatus) : RuntimeException()
enum class SeminarExceptionType(val message: String) {
    NotExistSurveyForId("해당 Id를 가진 설문조사가 존재하지 않습니다."),

    InputNeedsOSName("운영체제 이름을 입력해주세요."),

    NotExistOSForId("해당 Id를 가진 운영체제가 존재하지 않습니다."),
    NotExistOSForName("해당 이름을 가진 운영체제가 존재하지 않습니다.")
}


class Seminar404(val excType: SeminarExceptionType) : SeminarException(excType, HttpStatus.NOT_FOUND)

class Seminar400(val excType: SeminarExceptionType) : SeminarException(excType, HttpStatus.BAD_REQUEST)

// 이거 응용해서 Seminar 409 만들자