package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<UserEntity, Long> {
    fun save(userEntity: UserEntity) : UserEntity
    fun findByEmail(email: String) : UserEntity

}

interface ParticipantProfileRepository: JpaRepository<ParticipantProfileEntity,Long>{
    fun findByEmailParticipant(emailParticipant: String) : ParticipantProfileEntity
}

interface InstructorProfileRepository: JpaRepository<InstructorProfileEntity,Long>{
    fun findByEmailInstructor(emailInstructor: String) : InstructorProfileEntity
}

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun save(seminarEntity: SeminarEntity) : SeminarEntity
  override fun findById(id: Long): Optional<SeminarEntity>
}

interface ParticipantSeminarRepository: JpaRepository<ParticipantSeminarEntity, Long>{
    fun save(participantSeminarEntity: ParticipantSeminarEntity): ParticipantSeminarEntity

}