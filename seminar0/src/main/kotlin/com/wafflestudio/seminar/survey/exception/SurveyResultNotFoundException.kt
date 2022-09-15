package com.wafflestudio.seminar.survey.exception


class SurveyResultNotFoundException: IllegalArgumentException {
    constructor() : super()
    constructor(message: String) : super(message)
}