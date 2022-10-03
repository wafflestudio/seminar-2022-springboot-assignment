package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val nickname: String,
    val password: String,
    val email: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}