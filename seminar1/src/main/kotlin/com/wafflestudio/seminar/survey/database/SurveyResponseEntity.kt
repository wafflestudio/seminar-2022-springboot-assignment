package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    @OneToOne(fetch = FetchType.EAGER)
    val userEntity: UserEntity?,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String?,
    val grade: String?,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}