package com.wafflestudio.seminar.user.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
class UserEntity (
        @Column(nullable = false, name="nickname")
        val nickname:String,
        
        @Column(nullable = false, unique = true, name="email")
        val email:String,
        
        @Column(name="password")
        var password: String,
        ){
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
    }
