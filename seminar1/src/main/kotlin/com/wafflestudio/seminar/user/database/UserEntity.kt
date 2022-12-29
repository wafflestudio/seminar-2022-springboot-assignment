package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
class UserEntity(
    val nickname: String,
    val email: String,
    val password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}