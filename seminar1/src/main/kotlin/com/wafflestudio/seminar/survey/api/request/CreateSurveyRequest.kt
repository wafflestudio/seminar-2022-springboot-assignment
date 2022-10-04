package com.wafflestudio.seminar.survey.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateSurveyRequest(
        // For Primitive type, server gets default value when empty.
        // So get the parameter with nullable type, and change to not nullable type.

        @field: NotBlank(message = "os field should not be blank.")
        val os: String?,
        @field: NotNull(message = "spring_exp field should be integer.")
        val spring_exp: Int?,
        @field: NotNull(message = "rdb_exp field should be integer.")
        val rdb_exp: Int?,
        @field: NotNull(message = "programming_exp field should be integer.") 
        val programming_exp: Int?,
        val major: String = "",
        val grade: String = "",
        val backendReason: String? = null,
        val waffleReason: String? = null,
        val somethingToSay: String? = null
)