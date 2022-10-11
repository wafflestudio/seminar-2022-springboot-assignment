package com.wafflestudio.seminar.core.user.database

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="user")
class UserEntity(
    @Column
    var username: String,
    
    @Column
    var email: String,
    
    @Column
    var password: String,
    
    @Column
    var dateJoined: LocalDateTime
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}