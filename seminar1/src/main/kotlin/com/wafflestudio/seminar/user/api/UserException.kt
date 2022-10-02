package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.SeminarException
import org.springframework.http.HttpStatus


open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class IncorrectPassword : UserException("비밀번호가 틀립니다", HttpStatus.UNAUTHORIZED)

class UserNotFound : UserException("존재하지 않는 사용자입니다", HttpStatus.BAD_REQUEST)

class InvalidEmail : UserException("이미 사용중인 이메일입니다", HttpStatus.CONFLICT)

class NotFound : UserException("존재하지 않는 사용자입니다", HttpStatus.NOT_FOUND)

class Forbidden : UserException("접근할 수 없습니다", HttpStatus.FORBIDDEN)

class ResponseRequired : UserException("필수 응답 항목을 입력해주세요", HttpStatus.BAD_REQUEST)