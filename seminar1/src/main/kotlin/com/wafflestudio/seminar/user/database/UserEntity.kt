package com.wafflestudio.seminar.user.database

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email

@Entity
class UserEntity (
        val name:String,
        val email:String,
        ){
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
    }
