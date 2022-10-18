package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.UserSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
}