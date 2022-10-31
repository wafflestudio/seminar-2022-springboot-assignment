package com.wafflestudio.seminar.core.join

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long> {
    fun findAllByUserAndRole(user: UserEntity, role: String)
        : List<UserSeminarEntity>
    
    fun existsByUserAndSeminar(user: UserEntity, seminar: SeminarEntity)
        : Boolean
    
    fun existsByUserAndRole(user: UserEntity, role: String)
        : Boolean
    
    fun countAllBySeminarAndRole(seminar: SeminarEntity, role: String)
        : Int
    
    fun findAllBySeminarAndRole(seminar: SeminarEntity, role: String)
        : List<UserSeminarEntity>
    
    fun findByUserAndSeminar(user: UserEntity, seminar: SeminarEntity)
        : UserSeminarEntity?
    
    fun findByUserAndSeminarAndRole(user: UserEntity, seminar: SeminarEntity, role: String)
        : UserSeminarEntity?
}