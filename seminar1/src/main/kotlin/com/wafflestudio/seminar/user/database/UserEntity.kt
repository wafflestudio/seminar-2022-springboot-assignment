package com.wafflestudio.seminar.user.database

import javax.persistence.*


@Entity
class UserEntity(

        @Column(nullable = false)
        val nickname: String,

        @Column(nullable = false, unique = true)
        val email: String,
        
        val password: String
){
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
}