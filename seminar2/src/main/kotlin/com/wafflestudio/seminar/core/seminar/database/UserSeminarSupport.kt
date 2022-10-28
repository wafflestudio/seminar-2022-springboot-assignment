package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import com.wafflestudio.seminar.core.user.domain.User

interface UserSeminarSupport {
    fun findInstructorsById(seminarId: Long): List<Instructor>
    fun findParticipantsById(seminarId: Long): List<Participant>
    fun findActiveParticipantCountById(seminarId: Long): Long?
    fun findAllSeminarByInstructorId(userId: Long): List<SeminarEntity>
    fun findUserSeminarBySeminarIdAndUserId(seminarId: Long, userId: Long): UserSeminarEntity?
}