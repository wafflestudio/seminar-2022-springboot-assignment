package com.wafflestudio.seminar.core.seminar.database

import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long>, UserSeminarSupport {
    fun findBySeminarId(seminarId: Long): List<UserSeminarEntity>?
}
