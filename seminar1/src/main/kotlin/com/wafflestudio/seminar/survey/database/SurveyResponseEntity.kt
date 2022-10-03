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
import javax.validation.constraints.NotNull

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @field:NotNull(message = "OS는 필수 입력값입니다.")
    val operatingSystem: OperatingSystemEntity,
    @field:NotNull(message = "springExp는 필수 입력값입니다.")
    val springExp: Int,
    @field:NotNull(message = "rdbExp는 필수 입력값입니다.")
    val rdbExp: Int,
    @field:NotNull(message = "programmingExp는 필수 입력값입니다.")
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