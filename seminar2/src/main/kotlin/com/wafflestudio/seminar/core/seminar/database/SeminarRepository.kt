package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.QUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long>,
    SeminarRepositoryCustom

interface SeminarRepositoryCustom {
    fun findContainingName(name: String): MutableList<SeminarEntity>
}

@Component
class SeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SeminarRepositoryCustom {
    val user: QUserEntity = QUserEntity.userEntity
    val seminar: QSeminarEntity = QSeminarEntity.seminarEntity
    override fun findContainingName(name: String): MutableList<SeminarEntity> {
        return queryFactory.selectFrom(seminar).where(seminar.name.contains(name)).fetch()
    }
}