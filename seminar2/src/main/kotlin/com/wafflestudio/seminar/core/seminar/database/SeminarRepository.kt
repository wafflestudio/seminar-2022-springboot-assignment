package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QInstructorSeminarTableEntity.instructorSeminarTableEntity
import com.wafflestudio.seminar.core.seminar.database.QParticipantSeminarTableEntity.participantSeminarTableEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.stereotype.Repository

interface SeminarRepository : JpaRepository<SeminarEntity, Long>, SeminarSupport

interface SeminarSupport {
    fun findByNameLatest(name: String?): MutableList<SeminarEntity>
    fun findWithParticipants(seminarId: Long): SeminarEntity?
    fun findWithInstructors(seminarId: Long): SeminarEntity?
    fun findWithAllInfo(seminarId: Long): SeminarEntity?
}

@Repository
class SeminarSupportImpl(
    val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(SeminarEntity::class.java), SeminarSupport {
    
    override fun findByNameLatest(name: String?): MutableList<SeminarEntity> {
        return jpaQueryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.name.contains(name))
            .orderBy(seminarEntity.createdAt.desc())
            .fetch()
    }

    override fun findWithParticipants(seminarId: Long): SeminarEntity? {
        return jpaQueryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.id.eq(seminarId))
            .leftJoin(seminarEntity.participantSet, participantSeminarTableEntity)
            .fetchJoin()
            .leftJoin(participantSeminarTableEntity.participant, userEntity)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile)
            .fetchJoin()
            .fetchOne()
    }

    override fun findWithInstructors(seminarId: Long): SeminarEntity? {
        return jpaQueryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.id.eq(seminarId))
            .leftJoin(seminarEntity.instructorSet, instructorSeminarTableEntity)
            .fetchJoin()
            .leftJoin(instructorSeminarTableEntity.instructor, userEntity)
            .fetchJoin()
            .leftJoin(userEntity.instructorProfile)
            .fetchJoin()
            .fetchOne()
    }

    override fun findWithAllInfo(seminarId: Long): SeminarEntity? {
        return jpaQueryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.id.eq(seminarId))
            .leftJoin(seminarEntity.participantSet, participantSeminarTableEntity)
            .fetchJoin()
            .leftJoin(participantSeminarTableEntity.participant, userEntity)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile)
            .fetchJoin()
            .leftJoin(seminarEntity.instructorSet, instructorSeminarTableEntity)
            .fetchJoin()
            .leftJoin(instructorSeminarTableEntity.instructor, userEntity)
            .fetchJoin()
            .leftJoin(userEntity.instructorProfile)
            .fetchJoin()
            .fetchOne()
    }
    
}