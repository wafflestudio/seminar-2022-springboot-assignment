package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable=false)
    var name: String,
    @Column(unique=true, nullable=false)
    var email: String,
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun toUser(): User {
        return User(id = id, name = name, email = email, password = password)
    }
}