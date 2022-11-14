package com.wafflestudio.seminar.core.user.database

import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.dto.seminar.TeacherDto
import org.springframework.stereotype.Component

@Component
class UserSeminarDslRepository(
        private val queryFactory: JPAQueryFactory
) {
    
    fun getInstructorList(name: String?, order: String?) : List<Tuple> {

        return if(order == "earliest") {
            instructorQuery(name).orderBy(userSeminarEntity.seminar.id.asc()).fetch()
        } else {
            instructorQuery(name).orderBy(userSeminarEntity.seminar.id.desc()).fetch()
        }
    }
    
    fun getParticipantList(name: String?, order: String?) : List<Tuple> {
        return if(order == "earliest") {
            participantQuery(name).orderBy(userSeminarEntity.seminar.id.asc()).fetch()
        } else {
            participantQuery(name).orderBy(userSeminarEntity.seminar.id.desc()).fetch()
        }
    }
    
    private fun instructorQuery(name: String?) :JPAQuery<Tuple> {
        return queryFactory.select(
                userSeminarEntity.seminar.id,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt).from(userSeminarEntity)
                .leftJoin(userEntity).on(userSeminarEntity.user.eq(userEntity)).fetchJoin()
                .where(userSeminarEntity.role.eq("INSTRUCTOR")).where(name?.let { userSeminarEntity.seminar.name.contains(name) })
    }
    
    private fun participantQuery(name: String?) :JPAQuery<Tuple> {
        return queryFactory.select(
                userSeminarEntity.seminar.id,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt).from(userSeminarEntity)
                .leftJoin(userEntity).on(userSeminarEntity.user.eq(userEntity)).fetchJoin()
                .where(userSeminarEntity.role.eq("PARTICIPANT")).where(name?.let { userSeminarEntity.seminar.name.contains(name) })
    }
}