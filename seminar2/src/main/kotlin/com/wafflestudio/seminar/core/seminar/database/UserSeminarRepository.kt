package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminar, Long> {
    fun findByUserEntity(userEntity : UserEntity) : List<UserSeminar>
    fun findBySeminarEntity(seminar : SeminarEntity) : List<UserSeminar>
}