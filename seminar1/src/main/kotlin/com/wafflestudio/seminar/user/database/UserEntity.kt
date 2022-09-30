package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.domain.User
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity(
    val nickname: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    @CreationTimestamp
    var createDt: LocalDateTime = LocalDateTime.now(),
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    var survey : SurveyResponseEntity?=null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    fun toUser() : User{
        return User(this.id,this.nickname,this.email,this.password)
    }
    fun updateSurvey(surveyRequest: CreateSurveyRequest,os: OperatingSystemEntity){
        this.survey= SurveyResponseEntity(
            os,
            surveyRequest.springExp,
            surveyRequest.rdbExp,
            surveyRequest.programmingExp,
            surveyRequest.major,
            surveyRequest.grade,
            surveyRequest.timestamp,
            surveyRequest.backendReason,
            surveyRequest.waffleReason,
            surveyRequest.somethingToSay
        )

    }
}