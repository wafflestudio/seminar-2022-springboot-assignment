package com.wafflestudio.seminar.core.UserSeminar.repository

import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long>, UserSeminarCustomRepository {
    fun findByUser_Id(userId: Long): List<UserSeminarEntity>?
    fun findByUser_IdAndSeminar_Id(userId: Long, seminarId: Long): UserSeminarEntity?
}