package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String?,
    val grade: String?,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    val user : User? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}