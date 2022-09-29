package com.wafflestudio.seminar.user.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
final class User(
    val nickname: String,
    val email: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    /** 동일한 유저 이메일이 동작하면 에러내기 구현하기*/
}