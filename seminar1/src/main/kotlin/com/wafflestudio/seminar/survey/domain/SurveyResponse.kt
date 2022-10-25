package com.wafflestudio.seminar.survey.domain

<<<<<<< HEAD
import com.wafflestudio.seminar.user.domain.User
=======
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
import java.time.LocalDateTime

data class SurveyResponse(
    val id: Long,
    val operatingSystem: OperatingSystem,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
<<<<<<< HEAD
    val timestamp: LocalDateTime? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null, 
    val user: User? = null
=======
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
)