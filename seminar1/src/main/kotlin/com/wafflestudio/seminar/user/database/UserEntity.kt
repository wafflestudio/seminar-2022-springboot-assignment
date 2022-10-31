package com.wafflestudio.seminar.user.database


import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
class UserEntity (
    val name : String,
    
    @Column(unique = true)
    val email : String,
    
    val encodedPassword : String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    fun toUser(): User {
        return User(
            name = name,
            email = email,
        )
    } 
}