package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import java.time.LocalDateTime

data class UserSurvey (
    val id: Long,
    val os: String,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
    
){
    
    
}