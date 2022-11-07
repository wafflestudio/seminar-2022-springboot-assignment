package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserSeminarEntity

interface CustomUserSeminarRepository {
    fun findByUserIdAndSeminarId(userId: Long, seminarId: Long): UserSeminarEntity?
}