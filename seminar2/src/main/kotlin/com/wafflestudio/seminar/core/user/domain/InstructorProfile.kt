package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.database.InstructorEntity

data class InstructorProfile(
    val id: Long,
    val company: String,
    val year: Long?,
    // TODO seminars
) {
    companion object {
        fun of(entity: InstructorEntity) = entity.run {
            InstructorProfile(
                id = id,
                company = company,
                year = year,
            )
        }
    }
}