package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.transaction.Transactional


interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findUserEntityByEmail(email: String): UserEntity?
    fun existsUserEntityByEmail(email: String): Boolean
}