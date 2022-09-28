package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.api.SeminarException
import org.springframework.http.HttpStatus

open class UserException(msg: String, val status: HttpStatus) : RuntimeException(msg){
    class Seminar409(msg: String) : UserException(msg, HttpStatus.CONFLICT)

    class Seminar404(msg: String) : UserException(msg, HttpStatus.NOT_FOUND)

    class Seminar403(msg: String) : UserException(msg, HttpStatus.FORBIDDEN)

    class Seminar401(msg: String) : UserException(msg, HttpStatus.UNAUTHORIZED)

    class Seminar400(msg: String) : UserException(msg, HttpStatus.BAD_REQUEST)

}