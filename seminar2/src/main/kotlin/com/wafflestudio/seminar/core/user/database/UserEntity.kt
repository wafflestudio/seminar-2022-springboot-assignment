package com.wafflestudio.seminar.core.user.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class UserEntity(
    @Column(nullable=false)
    val username: String,
    @Column(nullable=false, unique=true)
    val email: String,
    @Column(nullable=false)
    val password: String
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}