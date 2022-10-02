package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val osName: String,
    
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
) {
    fun toEntity(os: OperatingSystemEntity, user: UserEntity): SurveyResponseEntity {
        return SurveyResponseEntity(
            os,
            user,
            springExp,
            rdbExp,
            programmingExp,
            major,
            grade,
            timestamp = LocalDateTime.now(),
            backendReason,
            waffleReason,
            somethingToSay
        )
    }
}