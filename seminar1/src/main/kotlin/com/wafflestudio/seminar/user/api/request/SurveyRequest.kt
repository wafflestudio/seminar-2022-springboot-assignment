package com.wafflestudio.seminar.user.api.request

import java.time.LocalDateTime

data class SurveyRequest(
    val spring_exp:Int,
    val rdb_exp:Int,
    val programming_exp:Int,
    val os:String? = null,
    val major: String? = null,
    val grade: String? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
) {
}