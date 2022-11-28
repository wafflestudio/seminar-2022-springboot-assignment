package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QInstructorSeminarTableEntity.instructorSeminarTableEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

interface ParticipantSeminarTableRepository : JpaRepository<ParticipantSeminarTableEntity, Long>

interface InstructorSeminarTableRepository : JpaRepository<InstructorSeminarTableEntity, Long>, InstructorSeminarTableSupport

interface InstructorSeminarTableSupport {
    fun findByInstructorId(instructorId: Long): InstructorSeminarTableEntity?
}

@Repository
class InstructorSeminarTableSupportImpl(
    val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(InstructorSeminarTableEntity::class.java), InstructorSeminarTableSupport {

    override fun findByInstructorId(instructorId: Long): InstructorSeminarTableEntity? {
        return jpaQueryFactory
            .select(instructorSeminarTableEntity)
            .from(instructorSeminarTableEntity)
            .where(instructorSeminarTableEntity.instructor.id.eq(instructorId))
            .innerJoin(instructorSeminarTableEntity.seminar)
            .fetchJoin()
            .innerJoin(instructorSeminarTableEntity.instructor, userEntity)
            .fetchJoin()
            .innerJoin(userEntity.instructorProfile)
            .fetchJoin()
            .fetchOne()
    }
}