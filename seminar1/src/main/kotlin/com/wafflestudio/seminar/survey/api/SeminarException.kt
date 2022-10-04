package com.wafflestudio.seminar.survey.api

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class SurveyRequestBodyException(val errors: List<FieldError>)
    : SeminarException("Given request body has error!", HttpStatus.BAD_REQUEST)