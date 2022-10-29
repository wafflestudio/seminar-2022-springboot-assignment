package com.wafflestudio.seminar.user.domain


import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserSurvey (
    val id: Long,
    
    val os: String ="",
    
    val springExp: Int = 0,
    val rdbExp: Int = 0,
    val programmingExp: Int =0,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
    
){
    
    
}