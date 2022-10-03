package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
class UserEntity(
    @Column
    val userName: String,
    @Column
    val email: String,
    val password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
