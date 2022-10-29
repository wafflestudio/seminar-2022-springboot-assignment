package com.wafflestudio.seminar.core.user.database

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
class UserEntity (
        @Size(min = 1) @Column(nullable = false)
        val username: String,
        
        @Size(min = 1) @Column(nullable = false, unique = true)
        val email: String,

        @Size(min = 1) @Column(nullable = false)
        val password: String,
        
        @CreationTimestamp @Column(nullable = false)
        var lastLogin: LocalDateTime? = null

) {
    @CreationTimestamp @Column(nullable = false, updatable = false)
    val dataJoined: LocalDateTime? = null

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}