package com.wafflestudio.seminar.user.repository

import com.wafflestudio.seminar.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email:String): UserEntity?
}