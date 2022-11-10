package com.wafflestudio.seminar.core.userSeminar.repository

import com.wafflestudio.seminar.core.userSeminar.database.UserSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
}