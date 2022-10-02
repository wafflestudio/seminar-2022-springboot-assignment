package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    val nickname:String,
    val email:String,
    @Column(name = "user_password")
    val password:String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}