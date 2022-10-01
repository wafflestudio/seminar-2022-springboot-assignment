package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateSurveyRequest(
        @param: NotBlank val os: String,
        @param: NotNull val spring_exp: Int,
        @param: NotNull val rdb_exp: Int,
        @param: NotNull val programming_exp: Int,
        val major: String = "",
        val grade: String = "",
        val backendReason: String? = null,
        val waffleReason: String? = null,
        val somethingToSay: String? = null
)