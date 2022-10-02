package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateSurveyRequest(
    @field: NotBlank
    val osName: String,
    @field: NotNull
    val springExp: Int? = null,
    @field: NotNull
    val rdbExp: Int? = null,
    @field: NotNull
    val programmingExp: Int? = null,
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
            springExp!!,
            rdbExp!!,
            programmingExp!!,
            major,
            grade,
            timestamp = LocalDateTime.now(),
            backendReason,
            waffleReason,
            somethingToSay
        )
    }
}