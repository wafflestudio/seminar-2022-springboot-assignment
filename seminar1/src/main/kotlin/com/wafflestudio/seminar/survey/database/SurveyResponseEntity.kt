package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.user.domain.UserEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotEmpty

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @NotEmpty
    val operatingSystem: OperatingSystemEntity,
    @NotEmpty
    val springExp: Int,
    @NotEmpty
    val rdbExp: Int,
    @NotEmpty
    val programmingExp: Int,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val user: UserEntity? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}