package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QInstructorEntity.instructorEntity
import com.wafflestudio.seminar.core.seminar.database.QParticipantEntity.participantEntity
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.stereotype.Component

@Component
class SeminarDslRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getList(name: String?, isAscending: Boolean): List<SeminarEntity> {
        val ordering = when(isAscending) {
            true -> seminarEntity.createdAt.asc()
            else -> seminarEntity.createdAt.desc()
        }
        
        return queryFactory
            .select(seminarEntity)
            .from(seminarEntity)
            .leftJoin(userSeminarEntity)
            .on(userSeminarEntity.seminar.eq(seminarEntity))
            .fetchJoin()
            .where(name?.let { seminarEntity.name.contains(name) })
            .orderBy(ordering)
            .fetch()
    }
}