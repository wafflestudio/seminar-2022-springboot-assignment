package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.UserEntity

interface CustomUserRepository {
    fun findByIdWithUserSeminar(id: Long): UserEntity?
}