package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository


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
}