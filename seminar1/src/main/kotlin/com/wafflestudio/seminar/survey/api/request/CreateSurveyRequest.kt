package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.domain.UserEntity
import org.hibernate.validator.constraints.Range
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class CreateSurveyRequest(
    @field: NotBlank(message = "os 이름을 입력해주세요.")
    val os: String,
    
    @field: Range(min = 1, max = 5, message = "springExp : 1 ~ 5 값을 입력해주세요.")
    val springExp: Int,
    
    @field: Range(min = 1, max = 5, message = "rdbExp : 1 ~ 5 값을 입력해주세요.")
    val rdbExp: Int,
    
    @field: Range(min = 1, max = 5, message = "programmingExp : 1 ~ 5 값을 입력해주세요.")
    val programmingExp: Int,
    
    val major: String?,
    val grade: String?,
    val backendReason: String?,
    val waffleReason: String?,
    val somethingToSay: String?
) {
    fun toEntity(user: UserEntity?, os: OperatingSystem, request: CreateSurveyRequest): SurveyResponse {
        return SurveyResponse(
            operatingSystem = os,
            userEntity = user,
            springExp = request.springExp,
            rdbExp = request.rdbExp,
            programmingExp = request.programmingExp,
            major = request.major,
            grade = request.grade,
            timestamp = LocalDateTime.now(),
            backendReason = request.backendReason,
            waffleReason = request.waffleReason,
            somethingToSay = request.somethingToSay
        )
    }
}