package com.wafflestudio.seminar.core.userseminar.database


import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
    fun findAllBySeminarEntity_Id(seminarId: Long): List<UserSeminarEntity>?
    fun findByUserEntity_Id(userId: Long): UserSeminarEntity?
}