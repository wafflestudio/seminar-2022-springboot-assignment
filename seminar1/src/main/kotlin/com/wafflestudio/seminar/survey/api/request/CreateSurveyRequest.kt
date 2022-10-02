package com.wafflestudio.seminar.survey.api.request

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import org.hibernate.validator.constraints.Range

import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateSurveyRequest(
    @field:NotBlank 
    val operatingSystem: String,
    @field:NotNull
    val springExp: Int?=null,
    @field:NotNull
    val rdbExp: Int?=null,
    @field:NotNull
    val programmingExp: Int?=null,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime= LocalDateTime.now(),
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)
{
    
}