package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository :JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}