package com.wafflestudio.seminar.survey.database

<<<<<<< HEAD
import com.wafflestudio.seminar.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*
=======
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc

@Entity
class SurveyResponseEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val operatingSystem: OperatingSystemEntity,
    val springExp: Int,
    val rdbExp: Int,
    val programmingExp: Int,
    val major: String,
    val grade: String,
    val timestamp: LocalDateTime,
    val backendReason: String? = null,
    val waffleReason: String? = null,
<<<<<<< HEAD
    val somethingToSay: String? = null,
    @OneToOne(fetch = FetchType.LAZY)
    val userEntity: UserEntity? = null
=======
    val somethingToSay: String? = null
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
<<<<<<< HEAD
    
=======
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
}