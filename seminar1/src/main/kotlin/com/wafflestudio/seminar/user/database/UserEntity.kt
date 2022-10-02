package com.wafflestudio.seminar.survey.database

import javax.persistence.*

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
class UserEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val surveyReponse: SurveyResponseEntity,
    val email: String,
    val password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}