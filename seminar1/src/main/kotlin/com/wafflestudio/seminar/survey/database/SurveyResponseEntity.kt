package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserSurvey
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "surveyResponse")
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity? = null,
    
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    fun toSurveyResponse(): SurveyResponse {
        return SurveyResponse(
            id = id,
            user = user?.toUserResponse(),
            operatingSystem = operatingSystem.toOperationgSystem(),
            springExp,
            rdbExp,
            programmingExp,
            major,
            grade,
            timestamp,
            backendReason,
            waffleReason,
            somethingToSay
        )
    }
    
    fun toSurveyForUser(): UserSurvey {
        return UserSurvey(
            id = id,
            operatingSystem = operatingSystem.toOperationgSystem(),
            springExp,
            rdbExp,
            programmingExp,
            major,
            grade,
            timestamp,
            backendReason,
            waffleReason,
            somethingToSay
        )
    }
}