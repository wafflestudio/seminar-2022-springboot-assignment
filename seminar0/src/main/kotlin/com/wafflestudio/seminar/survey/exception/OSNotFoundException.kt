package com.wafflestudio.seminar.survey.exception

class OSNotFoundException: IllegalArgumentException {
    constructor() : super()
    constructor(message: String): super(message)
}