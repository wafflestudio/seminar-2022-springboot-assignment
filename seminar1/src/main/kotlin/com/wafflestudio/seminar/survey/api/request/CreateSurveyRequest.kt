package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val major: String?,
    val os : String?,
    val springExp: Int?,
    val rdbExp: Int?,
    val programmingExp: Int?,
    val grade: String?,
    val backendReason: String?,
    val waffleReason: String?,
    val somethingToSay: String?
    
) {
    fun toSurvey(userEntity: UserEntity, operatingSystemEntity: OperatingSystemEntity): SurveyResponseEntity{
        
        return SurveyResponseEntity(
            operatingSystem = operatingSystemEntity,
            userEntity = userEntity,
            springExp = this.springExp,
            rdbExp = this.rdbExp,
            programmingExp = this.programmingExp,
            major = this.major,
            grade = this.grade,
            timestamp = LocalDateTime.now(),
            backendReason = this.backendReason,
            waffleReason = this.waffleReason,
            somethingToSay = this.somethingToSay
        )
    }
}