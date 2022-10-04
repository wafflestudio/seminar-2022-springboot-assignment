package com.wafflestudio.seminar.survey.api.request

import java.time.LocalDateTime

data class CreateSurveyRequest(
    val spring_exp:Int?,
    val rdb_exp:Int?,
    val programming_exp:Int?,
    val os:String,
    val grade:String?,
    val major: String?,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
)

//spring_exp, rdb_exp, programming_exp, os