package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.database.SeminarEntity

interface CustomSeminarRepository {
    fun findByNameWithOrder(name: String, order: String): List<SeminarEntity>
}