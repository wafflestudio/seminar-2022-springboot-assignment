package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

open class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)
open class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)
open class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)
open class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)
open class Seminar409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)

// 401
object AuthTokenMissingException :
    Seminar401("Authorization token is missing")


open class Seminar500(msg: String) : SeminarException(msg, HttpStatus.INTERNAL_SERVER_ERROR)

// 500
object MismatchingAnnotationException :
    Seminar500("UserContext annotated without Authenticated annotation")
object UserContextWrongParameterTypeException :
    Seminar500("UserContext annotated parameter is not Long type")
