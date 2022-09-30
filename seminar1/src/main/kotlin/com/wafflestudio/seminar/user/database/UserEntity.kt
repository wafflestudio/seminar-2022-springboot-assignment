package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import javax.persistence.*

@Entity
class UserEntity(
    val nickname: String,
    val email: String,
    val password: String,
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val survey : SurveyResponseEntity?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
   
}