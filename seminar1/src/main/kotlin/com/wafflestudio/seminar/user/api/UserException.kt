package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus
open class UserException(val type: UserExceptionType, val status: HttpStatus) : RuntimeException()
enum class UserExceptionType(val message: String) {
    NeedsAuthetication("로그인이 필요합니다."),
    NotExistUserId("존재하지 않는 UserId 입니다."),
    ExistUserEmail("이미 존재하는 이메일입니다."),
    NotExistSurvey("설문조사를 진행하지 않은 유저입니다."),
    InvalidPassword("잘못된 비밀번호입니다.")
}


class User400(val excType: UserExceptionType) : UserException(excType, HttpStatus.BAD_REQUEST)
class User401(val excType: UserExceptionType) : UserException(excType, HttpStatus.UNAUTHORIZED)
class User403(val excType: UserExceptionType) : UserException(excType, HttpStatus.FORBIDDEN)
class User404(val excType: UserExceptionType) : UserException(excType, HttpStatus.NOT_FOUND)
class User409(val excType: UserExceptionType) : UserException(excType, HttpStatus.CONFLICT)