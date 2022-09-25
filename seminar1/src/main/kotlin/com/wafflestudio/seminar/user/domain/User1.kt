package com.wafflestudio.seminar.user.domain

import javax.persistence.*

@Entity
@Table(name="user4")
class User1(

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, name="nickname")
    val nickname: String?,

    @Column(nullable = false, name="email")
    val email: String?,

    @Column(name="password")
    val password: String?,
    
) {
    fun toUser(nickname: String?, email: String?, password: String?) :User1 {
        return User1(
            nickname = nickname,
            email = email,
            password = password,
        )
    }
}