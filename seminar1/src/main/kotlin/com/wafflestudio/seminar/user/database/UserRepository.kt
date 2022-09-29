package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByNickname(nickname : String) : Optional<User>
}