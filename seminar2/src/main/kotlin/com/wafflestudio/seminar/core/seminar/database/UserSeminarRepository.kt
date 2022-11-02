package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.user.database.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminar, Long> {
    fun findByUserAndSeminar(user : UserEntity, seminar: SeminarEntity) : UserSeminar?
    fun findAllBySeminar(seminar: SeminarEntity) : List<UserSeminar>
    fun findByUserAndRole(user: UserEntity, role: Role) : UserSeminar?
    fun findAllByUserAndRole(user: UserEntity, role: Role) : List<UserSeminar>
    
}