package com.wafflestudio.seminar.core.maptable

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarUserRepository : JpaRepository<SeminarUser, Long> {
    fun findByUser(userEntity: UserEntity) : List<SeminarUser>
    fun findByUserAndSeminar(userEntity: UserEntity, seminarEntity: SeminarEntity): List<SeminarUser>
}