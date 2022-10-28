package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarForInstructorProfile
import com.wafflestudio.seminar.core.seminar.domain.SeminarForParticipantProfile
import org.springframework.stereotype.Component

interface SeminarSupport {
    fun findSeminarByName(seminarName: String): List<SeminarEntity>
    fun findSeminarsByParticipantId(participantId: Long): List<SeminarForParticipantProfile>
    fun findSeminarsByInstructorId(instructorId: Long): List<SeminarForInstructorProfile>
}

