package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.domain.*
import javax.persistence.*

@Entity
@Table(
    name = "users"
)
class UserEntity(
    
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val surveyResponse: SurveyResponseEntity?,

    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false, unique = true)
    val email: String,
    
    @Column(nullable = false)
    val password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    fun toSignUpResponse(): SignUpResponse {
        return SignUpResponse(id, name, email)
    }
    
    fun toSignInResponse(): SignInResponse {
        return SignInResponse(id)
    }
    
    fun toUserResponse(): UserResponse {
        return UserResponse(id, name, email)
    }
    
    fun toUserSurveyResponse(): UserSurveyResponse {
        return UserSurveyResponse(id, name, email, surveyResponse!!.toSurveyForUser())
    }
}