package com.wafflestudio.seminar.user.database

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import org.springframework.stereotype.Repository
import javax.persistence.*
import javax.validation.constraints.Email


@Entity(name = "users")
class UserEntity (
    userName: String,
    email: String,
    password: String
        ){
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long = 0L
    
    @OneToOne(mappedBy = "userEntity")
    val surveyResponse: SurveyResponseEntity? = null
    
    @Column(nullable = false)
    val userName: String = userName

    @Column(nullable = false, unique = true)
    @Email
    val email: String = email

    @Column(nullable = false)
    @JsonIgnore
    val password: String = password;
}