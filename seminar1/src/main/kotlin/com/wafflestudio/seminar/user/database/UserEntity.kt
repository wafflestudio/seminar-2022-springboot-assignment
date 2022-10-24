package com.wafflestudio.seminar.user.database

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class UserEntity(
    nickname: String,
    email: String,
    encodedPassword: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    @OneToOne(mappedBy = "userEntity")
    val surveyResponse: SurveyResponseEntity? = null
    
    @NotNull
    val nickname: String = nickname
    
    @Email 
    @NotNull
    @Column(unique = true)
    val email: String = email
    
    @NotBlank
    @JsonIgnore
    val encodedPassword: String = encodedPassword

}