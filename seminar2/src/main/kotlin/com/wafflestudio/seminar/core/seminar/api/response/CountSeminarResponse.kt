package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse.Companion.getInstructorList
import com.wafflestudio.seminar.core.seminar.repository.Seminar

data class CountSeminarResponse(
    val id: Long,
    val name: String,
    val instructors: List<SimpleInstructorDto>,
    val participantCount: Int,
) {
    companion object {
        fun of(seminar: Seminar): CountSeminarResponse {
            return CountSeminarResponse(
                id = seminar.id,
                name = seminar.name,
                instructors = getInstructorList(seminar),
                participantCount = seminar.participantCount
            )
        }
    }
} 
