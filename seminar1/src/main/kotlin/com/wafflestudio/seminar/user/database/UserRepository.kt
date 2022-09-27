package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>{
    fun findByEmailAndPassword(email: String, password: String) : UserEntity
    fun findByEmail(email: String): UserEntity?
    
}