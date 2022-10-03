package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.domain.UserInfo
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
class UserEntity (
    @NotEmpty
    val nickname: String,
    @NotEmpty
    @Column(unique = true)
    val email: String,
    @NotEmpty
    val password: String,
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    @OneToOne(fetch = FetchType.LAZY)
    var surveyResponse: SurveyResponseEntity? = null
    
    fun info() = UserInfo(nickname, email)
}