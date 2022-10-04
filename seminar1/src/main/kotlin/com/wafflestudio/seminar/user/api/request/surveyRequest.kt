package com.wafflestudio.seminar.user.api.request

import java.time.LocalDateTime

data class SurveyRequest(
    val os:String="",
    val springExp:Int=0,
    val rdbExp:Int=0,
    val programmingExp:Int=0,
    val major:String="",
    val grade:String="",
    val timestamp:LocalDateTime= LocalDateTime.now(),
    val backendReason:String?=null,
    val waffleReason:String?=null,
    val somethingToSay:String?=null
)