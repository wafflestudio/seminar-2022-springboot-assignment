package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

open class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)
open class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)
open class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)
open class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)
open class Seminar409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)

// 400
object BlankSeminarNameNotAllowedException :
    Seminar400("Seminar name is not allowed to be empty string")
object NoInstructingSeminarException :
    Seminar400("No instructing seminar to update exists")
object SeminarCapacityFullException :
    Seminar400("Seminar capacity is full")
object AlreadyParticipatingException :
    Seminar400("Already participating this seminar")
object DroppedSeminarException :
    Seminar400("Cannot participate dropped seminar again")
object MultipleInstructingSeminarException :
    Seminar400("Cannot join multiple seminars as instructor")

// 401
object AuthTokenMissingException :
    Seminar401("Authorization token is missing")

// 403
object NotAllowedToUpdateSeminarException :
    Seminar403("Only instructors can update seminar info")
object NotAllowedToParticipateException :
    Seminar403("Current user is not participant")
object NotAllowedToInstructException :
    Seminar403("Current user is not instructor")
object InstructorNotAllowedToDropException :
    Seminar403("Instructor cannot drop instructing seminar")

// 404
object SeminarNotFoundException :
    Seminar404("Seminar not found with given id")

open class Seminar500(msg: String) : SeminarException(msg, HttpStatus.INTERNAL_SERVER_ERROR)

// 500
object MismatchingAnnotationException :
    Seminar500("UserContext annotated without Authenticated annotation")
object UserContextWrongParameterTypeException :
    Seminar500("UserContext annotated parameter is not Long type")
