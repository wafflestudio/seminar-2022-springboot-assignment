package com.wafflestudio.seminar.core.user.database

import com.querydsl.core.QueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.SeminarEntity
import org.springframework.stereotype.Component


@Component
class SeminarDslRepository(
        private val queryFactory: JPAQueryFactory
) {
    fun getListById(id: Long) : List<SeminarEntity> {
        return queryFactory.select(seminarEntity).from(seminarEntity).where(seminarEntity.id.eq(id)).fetch()
    }
    fun getListByNameAndOrder(name: String? , order: String?) : List<SeminarEntity>{
        return if(order == "earliest"){
            queryFactory.select(seminarEntity).from(seminarEntity).where(name?.let { seminarEntity.name.contains(name) }).orderBy(seminarEntity.id.asc()).fetch()
        } else {
            queryFactory.select(seminarEntity).from(seminarEntity).where(name?.let { seminarEntity.name.contains(name) }).orderBy(seminarEntity.id.desc()).fetch()
        }
    }
}