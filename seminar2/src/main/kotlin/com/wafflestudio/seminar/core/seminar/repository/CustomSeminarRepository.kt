package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity

interface CustomSeminarRepository {
    fun findByNameWithOrder(name: String, order: String): List<SeminarEntity>
}