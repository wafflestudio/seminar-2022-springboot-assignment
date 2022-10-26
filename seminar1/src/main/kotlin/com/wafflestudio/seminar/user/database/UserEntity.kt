package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val nickname: String,
    val email: String,
    val encodedPassword: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}