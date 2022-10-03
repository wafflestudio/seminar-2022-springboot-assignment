package com.wafflestudio.seminar.survey.api.request

import java.time.LocalDateTime

data class CreateSurveyRequest(
        val operatingSystem: String? = null,
        val springExp: Int? = null,
        val rdbExp: Int? = null,
        val programmingExp: Int? = null,
        val major: String? = null,
        val grade: String? = null,
        val timestamp: LocalDateTime? = null,
        val backendReason: String? = null,
        val waffleReason: String? = null,
        val somethingToSay: String? = null
)