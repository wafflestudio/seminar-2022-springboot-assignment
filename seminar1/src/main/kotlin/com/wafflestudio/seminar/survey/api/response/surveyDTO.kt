package com.wafflestudio.seminar.survey.api.response

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime

data class surveyInfo(
    val operatingSystem: OperatingSystemEntity?,
    val userEntity: UserEntity?,
    val springExp: Int?,
    val rdbExp: Int?,
    val programmingExp: Int?,
    val timestamp: LocalDateTime,
) {
    companion object{
        fun toDTO(surveyResponseEntity: SurveyResponseEntity): surveyInfo{
            return surveyInfo(
                operatingSystem = surveyResponseEntity.operatingSystem,
                userEntity = surveyResponseEntity.userEntity,
                springExp = surveyResponseEntity.springExp,
                rdbExp = surveyResponseEntity.rdbExp,
                programmingExp = surveyResponseEntity.programmingExp,
                timestamp = surveyResponseEntity.timestamp
            )
        }
    }
}