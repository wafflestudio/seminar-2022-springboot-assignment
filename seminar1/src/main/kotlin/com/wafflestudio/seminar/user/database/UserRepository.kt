package com.wafflestudio.seminar.user.database

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByNickname(nickname : String) : Optional<UserEntity>
    fun findByEmail(email : String) : Optional<UserEntity>
    override fun findById(id : Long) : Optional<UserEntity>
}