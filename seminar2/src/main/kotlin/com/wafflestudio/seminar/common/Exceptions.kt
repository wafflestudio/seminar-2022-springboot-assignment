package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

open class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)
open class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)
open class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)
open class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)
open class Seminar409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)

// 400

// 401
object AuthTokenMissingException :
    Seminar401("Authorization token is missing")
object InvalidTokenException :
    Seminar401("Authorization token is in invalid form (unsupported, malformed, etc.)")
object FailedToLogInException :
    Seminar401("Failed to log in")

// 403
object AuthTokenExpiredException :
    Seminar403("Authorization token is expired")

// 404
object UserNotFoundException :
    Seminar404("Cannot find user info")

// 409
object DuplicateEmailException :
    Seminar409("Given email is already signed up")



open class Seminar500(msg: String) : SeminarException(msg, HttpStatus.INTERNAL_SERVER_ERROR)

object MismatchingAnnotationException :
    Seminar500("UserContext annotated without Authenticated annotation")
object UserContextWrongParameterTypeException :
    Seminar500("UserContext annotated parameter is not Long type")
object HandlerCastingException :
    Seminar500("Exception thrown while casting handler")