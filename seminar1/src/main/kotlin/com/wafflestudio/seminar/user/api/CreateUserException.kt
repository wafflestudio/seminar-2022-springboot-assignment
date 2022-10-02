package com.wafflestudio.seminar.user.api

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError

open class CreateUserException(val msg: String, val status: HttpStatus): RuntimeException(msg)

class CreateUserRequestBodyException(val errors: List<FieldError>)
    : CreateUserException("Given request body has error!", HttpStatus.BAD_REQUEST)

//class CreateUserEmptyNicknameException(): CreateUserException("Empty nickname given!", HttpStatus.BAD_REQUEST)
//class CreateUserEmptyEmailException(): CreateUserException("Empty email given!", HttpStatus.BAD_REQUEST)
//class CreateUserEmptyPasswordException(): CreateUserException("Empty password given!", HttpStatus.BAD_REQUEST)

class CreateUserNotUniqueEmailException(): CreateUserException("Given email is already in use!", HttpStatus.CONFLICT)