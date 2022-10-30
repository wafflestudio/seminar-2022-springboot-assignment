package com.wafflestudio.seminar.user.api

class SameEmailException(msg: String) : RuntimeException(msg)
class DiffPasswordException(msg: String) : RuntimeException(msg)
class UserNotExistException(msg: String) : RuntimeException(msg)
class MissingHeaderException(msg: String) : RuntimeException(msg)
class MissingValueException(msg: String) : RuntimeException(msg)