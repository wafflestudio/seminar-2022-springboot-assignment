package com.wafflestudio.seminar.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)


class WrongEmailException(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class WrongSeminarIdException(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class WrongRoleException(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class WrongPasswordException(msg: String): SeminarException(msg, HttpStatus.UNAUTHORIZED)

class DuplicatedEmailException(msg: String): SeminarException(msg, HttpStatus.CONFLICT)

class NoPermissionException(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class InValidYearInputException(msg:String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class AlreadyReigsteredException(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class AlreadyDroppedException(msg :String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class FullClassException(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

