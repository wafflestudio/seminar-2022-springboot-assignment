package com.wafflestudio.seminar.user.database

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
class UserEntity(
        @Size(min=1)
        @Column(nullable=false)
        val nickname: String,

        @Size(min=1)
        @Column(nullable=false, unique=true)
        val email: String,
        
        @Size(min=1)
        @Column(nullable=false)
        val password: String
) {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0L
}