package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.seminar.domain.SeminarForInstructorProfile
import com.wafflestudio.seminar.core.seminar.domain.SeminarForParticipantProfile

interface SeminarSupport {
    fun findSeminarByName(seminarName: String): List<SeminarEntity>
    fun findSeminarsByParticipantId(participantId: Long): List<SeminarForParticipantProfile>
    fun findSeminarsByInstructorId(instructorId: Long): List<SeminarForInstructorProfile>
}

