package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
@Table(name = "user_table")
class UserEntity(
        val nickname: String,
        val email: String,
        val password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long = 0L
}