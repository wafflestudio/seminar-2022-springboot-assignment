package com.wafflestudio.seminar.core.jointable

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long> {
    fun findAllByUserAndIsParticipant(user: UserEntity, isParticipant: Boolean)
        : List<UserSeminarEntity>
    
    fun findAllBySeminarAndIsParticipant(seminar: SeminarEntity, isParticipant: Boolean)
        : List<UserSeminarEntity>
}