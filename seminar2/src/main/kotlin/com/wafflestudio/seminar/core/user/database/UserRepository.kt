package com.wafflestudio.seminar.core.user.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface UserRepository : JpaRepository<UserEntity, Long>, UserRepositoryCustom

interface UserRepositoryCustom {
    fun findByEmail(email: String): UserEntity?
//    fun getProfile(userId: Long): ProfileResponse?
}

@Component
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    val user: QUserEntity = QUserEntity.userEntity
    val seminar: QSeminarEntity = QSeminarEntity.seminarEntity
    val userSeminar: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity
    val participantProfile: QParticipantProfileEntity = QParticipantProfileEntity.participantProfileEntity

    @Transactional
    override fun findByEmail(email: String): UserEntity? {
        return queryFactory
            .selectFrom(user)
            .where(user.email.eq(email))
            .fetchOne()
    }

//    @Transactional
//    override fun getProfile(userId: Long): ProfileResponse? {
//        val userEntity = queryFactory.selectFrom(user).where(user.id.eq(userId)).fetchOne() ?: return null
//        val seminars: List<ParticipatingSeminar> =
//            queryFactory.select(
//                Projections.constructor(
//                    ParticipatingSeminar::class.java,
//                    userSeminar.seminar.id,
//                    userSeminar.seminar.name,
//                    userSeminar.joinedAt,
//                    userSeminar.isActive,
//                    userSeminar.droppedAt
//                )
//            ).from(userSeminar)
//                .innerJoin(userSeminar.seminar, seminar).where(seminar.id.eq(userSeminar.seminar.id))
//                .innerJoin(userSeminar.user, user).where(user.id.eq(userSeminar.user.id))
//                .fetch()
//
//        val user = User(id = userEntity.id, username = userEntity.)
//
//        return userEntity.toUser()
//    }
}