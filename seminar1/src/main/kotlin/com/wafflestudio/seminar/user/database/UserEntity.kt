package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
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
    val survey : SurveyResponseEntity?=null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    fun toUser() : User{
        return User(this.nickname,this.email,this.password)
    }
}