package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity(name = "user")
class UserEntity(
    @Column(nullable = false)
    val nickname: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val encodedPassword: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}