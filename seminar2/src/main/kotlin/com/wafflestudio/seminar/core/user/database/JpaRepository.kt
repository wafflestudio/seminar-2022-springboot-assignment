package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.core.user.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<UserEntity, Long> {
    fun save(userEntity: UserEntity) : UserEntity
    fun findByEmail(email: String) : UserEntity

}

interface ParticipantProfileRepository: JpaRepository<ParticipantProfileEntity,Long>{

}

interface InstructorProfileRepository: JpaRepository<InstructorProfileEntity,Long>{

}

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun save(seminarEntity: SeminarEntity) : SeminarEntity
    fun findByName(name: String?) : SeminarEntity
  override fun findById(id: Long): Optional<SeminarEntity>
}

interface UserSeminarRepository: JpaRepository<UserSeminarEntity, Long>{
    fun save(userSeminarEntity: UserSeminarEntity): UserSeminarEntity

}