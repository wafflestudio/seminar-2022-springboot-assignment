package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity

interface CustomSeminarRepository {
    fun findByNameAndOrder(name: String, order: String): List<SeminarEntity>
    fun findByIdWithUserSeminarAndUser(id: Long): SeminarEntity?
}