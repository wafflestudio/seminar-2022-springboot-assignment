package com.wafflestudio.seminar.core.user.database

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
    var lastLogin: String,
    
    @Column
    var dateJoined: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}