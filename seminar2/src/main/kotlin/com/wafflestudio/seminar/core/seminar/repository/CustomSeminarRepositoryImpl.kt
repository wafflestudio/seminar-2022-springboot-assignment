package com.wafflestudio.seminar.core.seminar.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.QInstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.QParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity
import com.wafflestudio.seminar.core.userSeminar.database.QUserSeminarEntity
import org.springframework.stereotype.Repository

@Repository
class CustomSeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomSeminarRepository {

    override fun findByNameAndOrder(name: String, order: String): List<SeminarEntity> {
        val seminarEntity = QSeminarEntity.seminarEntity
        return queryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.name.contains(name))
            .orderBy(
                when (order) {
                    "earliest" -> seminarEntity.createdAt.asc()
                    else -> seminarEntity.createdAt.desc()
                }
            )
            .fetch()
    }

    override fun findByIdWithUserSeminarAndUser(id: Long): SeminarEntity? {
        val seminarEntity = QSeminarEntity.seminarEntity
        val userSeminarEntity = QUserSeminarEntity.userSeminarEntity
        val userEntity = QUserEntity.userEntity
        val participantProfileEntity = QParticipantProfileEntity.participantProfileEntity
        val instructorProfileEntity = QInstructorProfileEntity.instructorProfileEntity
        return queryFactory
            .selectFrom(seminarEntity)
            .where(seminarEntity.id.eq(id))
            .leftJoin(seminarEntity.userSeminars, userSeminarEntity)
            .fetchJoin()
            .leftJoin(userSeminarEntity.user, userEntity)
            .fetchJoin()
            .leftJoin(userEntity.participantProfile, participantProfileEntity)
            .fetchJoin()
            .leftJoin(userEntity.instructorProfile, instructorProfileEntity)
            .fetchJoin()
            .fetchOne()
    }
}