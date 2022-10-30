package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.user.repository.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserSeminarRepository: JpaRepository<UserSeminar, Long> {
    fun findByUserEntity(userEntity: UserEntity): List<UserSeminar>
    fun findByUserEntityAndSeminar(userEntity: UserEntity, seminar: Seminar): Optional<UserSeminar>
    fun findBySeminar(seminar: Seminar): List<UserSeminar>
}