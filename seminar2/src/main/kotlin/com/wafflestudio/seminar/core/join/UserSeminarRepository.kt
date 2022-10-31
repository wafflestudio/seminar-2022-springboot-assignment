package com.wafflestudio.seminar.core.join

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long> {
    fun findAllByUserAndRole(user: UserEntity, role: String)
        : List<UserSeminarEntity>
    
    fun findAllBySeminarAndRole(seminar: SeminarEntity, role: String)
        : List<UserSeminarEntity>
    
    fun findByUserAndSeminar(user: UserEntity, seminar: SeminarEntity)
        : UserSeminarEntity?
}