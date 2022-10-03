package com.wafflestudio.seminar.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmail(email: String): UserEntity?
}