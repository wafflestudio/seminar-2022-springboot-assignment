package com.wafflestudio.seminar.user.database

import javax.persistence.*

@Entity
@Table(name="users")
class UserEntity(

    

    @Column(nullable = false, name="nickname")
    var nickname: String,

    @Column(nullable = false, unique = true, name="email")
    val email: String,

    @Column(name="password")
    val password: String,
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}