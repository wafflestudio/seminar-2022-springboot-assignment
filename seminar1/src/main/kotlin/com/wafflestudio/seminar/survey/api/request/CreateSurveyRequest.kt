package com.wafflestudio.seminar.survey.api.request

import org.springframework.lang.NonNullFields
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateSurveyRequest(

//    @field:@NotEmpty
//    @field:@NotNull
    @field:NotEmpty @field:NotNull
    val osName: String,
    
    @field:NotNull
    val springExp: Int? = null,
    
    @field:NotNull
    val rdbExp: Int? = null,
    
    @field:NotNull
    val programmingExp: Int? = null,
    
    val major: String? = null,
    val grade: String? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
)