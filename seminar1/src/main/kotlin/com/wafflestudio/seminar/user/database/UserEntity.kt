package com.wafflestudio.seminar.user.database


import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false)
    val name: String,
    @Column(unique = true, nullable = false)
    val email: String,
    val password: String
) {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            id = id,
            password = password
        )
    }

}