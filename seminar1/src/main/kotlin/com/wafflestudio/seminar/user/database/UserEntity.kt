package com.wafflestudio.seminar.user.database


import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*


@Entity
@Table(name = "users")
// "user" 는 중복된 이름이므로 안됨.
class UserEntity (
    val name:String,
    
    @Column(unique = true)
    val email:String,
    
    val pw:String,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    fun toUser(): User {
        return User(name,email, pw, id)
    }
}