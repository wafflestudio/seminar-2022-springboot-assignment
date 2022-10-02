package com.wafflestudio.seminar.survey.database

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "survey_responses")
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id")
    val operatingSystem: OperatingSystemEntity,
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
}