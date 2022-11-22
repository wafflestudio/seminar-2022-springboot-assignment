package com.wafflestudio.seminar.core.user.database

import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.dto.seminar.StudentDto
import com.wafflestudio.seminar.core.user.dto.seminar.TeacherDto
import com.wafflestudio.seminar.core.user.dto.user.InstructingSeminarsDto
import com.wafflestudio.seminar.core.user.dto.user.SeminarsDto
import org.springframework.stereotype.Component

@Component
class UserSeminarDslRepository(
        private val queryFactory: JPAQueryFactory
) {
    fun getProfileSeminars(userId: Long?) : List<SeminarsDto>? {
        return queryFactory.select(Projections.constructor(
                SeminarsDto::class.java, QSeminarEntity.seminarEntity.id, QSeminarEntity.seminarEntity.name,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt))
                .from(userSeminarEntity)
                .leftJoin(QSeminarEntity.seminarEntity).on(QSeminarEntity.seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("PARTICIPANT")).fetch()
    }
    
    fun getProfileInstructingSeminars(userId: Long?): List<InstructingSeminarsDto>? {
        return queryFactory.select(Projections.constructor(
                InstructingSeminarsDto::class.java, QSeminarEntity.seminarEntity.id, QSeminarEntity.seminarEntity.name, userSeminarEntity.joinedAt))
                .from(QSeminarEntity.seminarEntity)
                .leftJoin(userSeminarEntity).on(QSeminarEntity.seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("INSTRUCTOR")).fetch()
    }
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
    fun instructorQuery(name: String?) :JPAQuery<Tuple> {
        return queryFactory.select(
                userSeminarEntity.seminar.id,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt).from(userSeminarEntity)
                .leftJoin(userEntity).on(userSeminarEntity.user.eq(userEntity)).fetchJoin()
                .where(userSeminarEntity.role.eq("INSTRUCTOR")).where(name?.let { userSeminarEntity.seminar.name.contains(name) })
        
        
    }
    
    fun participantQuery(name: String?) :JPAQuery<Tuple> {
        return queryFactory.select(
                userSeminarEntity.seminar.id,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt).from(userSeminarEntity)
                .leftJoin(userEntity).on(userSeminarEntity.user.eq(userEntity)).fetchJoin()
                .where(userSeminarEntity.role.eq("PARTICIPANT")).where(name?.let { userSeminarEntity.seminar.name.contains(name) })
    }
    
    fun getTeacherListById(seminarId: Long?) :List<TeacherDto> {
        return queryFactory.select(Projections.constructor(
                TeacherDto::class.java,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userEntity.eq(userSeminarEntity.user)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(seminarId), userSeminarEntity.role.eq("INSTRUCTOR"))
                .fetch()
    }
    
    fun getStudentListById(seminarId: Long) : List<StudentDto> {
        return queryFactory.select(Projections.constructor(
                StudentDto::class.java,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userEntity.eq(userSeminarEntity.user)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(seminarId), userSeminarEntity.role.eq("PARTICIPANT"))
                .fetch()
    }
}