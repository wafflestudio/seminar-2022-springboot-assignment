package com.wafflestudio.seminar.user.domain

import javax.persistence.*

@Entity
class User(
    
    val nickname: String,
    val email: String,
    val password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
            
}