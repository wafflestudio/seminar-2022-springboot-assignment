package com.wafflestudio.seminar.user.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
class UserEntity (
    @NotEmpty
    val nickname: String,
    @NotEmpty
    @Column(unique = true)
    val email: String,
    @NotEmpty
    val password: String,
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}