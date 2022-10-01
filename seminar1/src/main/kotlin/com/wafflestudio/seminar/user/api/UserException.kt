package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class UserException(val msg: String, val status: HttpStatus): RuntimeException(msg)

class UserEmtpyNicknameException(): UserException("Empty nickname given!", HttpStatus.BAD_REQUEST)

class UserEmtpyEmailException(): UserException("Empty email given!", HttpStatus.BAD_REQUEST)

class UserEmtpyPasswordException(): UserException("Empty password given!", HttpStatus.BAD_REQUEST)

class UserNotUniqueEmailException(): UserException("Given email is already in use!", HttpStatus.CONFLICT)
