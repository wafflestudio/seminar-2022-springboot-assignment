package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus

open class LoginException(val msg: String, val status: HttpStatus): RuntimeException(msg)

class LoginNoEmailException: LoginException("Not a registered email.", HttpStatus.NOT_FOUND)

class LoginWrongPasswordException: LoginException("Password is wrong.", HttpStatus.UNAUTHORIZED)