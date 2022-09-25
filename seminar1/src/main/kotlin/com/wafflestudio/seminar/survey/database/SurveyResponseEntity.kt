package com.wafflestudio.seminar.survey.database

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    val userId: Long? = null,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String? = null,
    val grade: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now().withNano(0),
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}