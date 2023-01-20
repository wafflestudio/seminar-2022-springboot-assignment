package com.wafflestudio.seminar.user.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
class UserEntity (
    val name: String,
    @Column(nullable=false)
    val email: String,
    @Column(nullable=false, unique=true)
    val encPwd: String
    ) {
    @Id
    @GeneratedValue
    var id: Long? = null
}