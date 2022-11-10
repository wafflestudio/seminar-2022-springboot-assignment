package com.wafflestudio.seminar.core.userSeminar.repository

import com.wafflestudio.seminar.core.userSeminar.database.UserSeminarEntity

interface CustomUserSeminarRepository {
    fun findByUserIdAndSeminarId(userId: Long, seminarId: Long): UserSeminarEntity?
}