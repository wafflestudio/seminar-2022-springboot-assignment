package com.wafflestudio.seminar.survey.api.request

import com.sun.istack.NotNull
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.api.User404
import java.time.LocalDateTime

data class CreateSurveyRequest(
    val major: String,
    val springExp: Int = -1,
    val rdbExp: Int = -1,
    val programmingExp: Int = -1,
    val osName: String,
    val grade: String,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
) {
    fun toEntity(userId: Long, osRepository: OsRepository): SurveyResponseEntity {
        return SurveyResponseEntity(
            springExp = springExp,
            rdbExp = rdbExp,
            programmingExp = programmingExp,
            major = major,
            grade = grade,
            backendReason = backendReason,
            waffleReason = waffleReason,
            somethingToSay = somethingToSay,
            userId = userId,
            timestamp = LocalDateTime.now(),
            operatingSystem = osRepository.findByOsName(osName) ?: throw User404("존재하지 않는 OS입니다")
        )
    }
}