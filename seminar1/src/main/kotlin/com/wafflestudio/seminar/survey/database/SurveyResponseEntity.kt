package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    val springExp: Int? = null,
    val rdbExp: Int? = null,
    val programmingExp: Int? = null,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    val user_id: UserEntity? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}