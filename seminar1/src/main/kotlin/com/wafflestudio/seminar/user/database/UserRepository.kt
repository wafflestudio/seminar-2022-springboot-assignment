package com.wafflestudio.seminar.user.database

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): Optional<UserEntity>
    fun findByEmailAndPassword(email: String, password: String): Optional<UserEntity>
    fun save(userEntity: UserEntity): UserEntity
}