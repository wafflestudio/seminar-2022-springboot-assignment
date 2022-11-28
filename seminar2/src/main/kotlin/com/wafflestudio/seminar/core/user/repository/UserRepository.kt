package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>, UserCustomRepository {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}