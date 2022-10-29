package com.wafflestudio.seminar.survey.database


import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.jpa.domain.AbstractAuditable_.createdDate
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
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    var timestamp : LocalDateTime,
    val major: String? = null,
    val grade: String? = null,
    val backendReason: String? = null,
    val waffleReason: String? = null,
    val somethingToSay: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val user: UserEntity? = null
    
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
