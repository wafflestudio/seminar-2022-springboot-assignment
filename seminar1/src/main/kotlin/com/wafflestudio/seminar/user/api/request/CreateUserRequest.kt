package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateUserRequest(
    @field:NotBlank
    val nickname: String,
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val password : String
)