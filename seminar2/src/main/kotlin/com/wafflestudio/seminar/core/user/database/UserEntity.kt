package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
    @Column(name = "email", unique = true, nullable = false)
    val email: String,
    @Column(name = "name", nullable = false)
    val username: String,
    @Column(name = "password", nullable = false)
    val password: String,
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: User.Role,
) : BaseTimeEntity() {
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private val seminars: MutableSet<UserSeminarEntity> = mutableSetOf()
    
    fun toUser(): User {
        return User(
            id = id,
            email = email,
            username = username,
            role = role,
        )
    }
}