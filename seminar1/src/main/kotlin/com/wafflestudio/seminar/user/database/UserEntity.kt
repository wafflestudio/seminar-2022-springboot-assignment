package com.wafflestudio.seminar.user.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
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