package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.Tuple
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.seminar.api.response.InstructorInfo
import com.wafflestudio.seminar.core.seminar.api.response.SeminarsQueryResponse
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import kotlin.math.min


interface SeminarRepository : JpaRepository<SeminarEntity, Long>, SeminarRepositoryCustom {
    fun findByName(name: String) : Optional<SeminarEntity>
}

interface SeminarRepositoryCustom {
    fun findSeminarsByNameAndOrder(name: String?, earliest: Boolean, pageable: Pageable): Page<SeminarsQueryResponse>
}

@Component
class SeminarRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SeminarRepositoryCustom {
    
    override fun findSeminarsByNameAndOrder(
        name: String?, earliest: Boolean, pageable: Pageable
    ): Page<SeminarsQueryResponse> {
        val orderSpecifier: OrderSpecifier<LocalDateTime> = if (earliest) {
            seminarEntity.createdAt.asc()
        } else {
            seminarEntity.createdAt.desc()
        }
        var whereCondition : BooleanExpression = userSeminarEntity.isParticipant.isFalse
        if (name != null) {
            whereCondition = whereCondition.and(seminarEntity.name.contains(name))
        }
        // TODO: 이렇게 entity들을 일일히 select 하지 않고 DTO를 만들어 mapping을 통해 가져올 수 있다.
        val result = queryFactory
            .select(seminarEntity, userEntity, userSeminarEntity.createdAt)
            .from(seminarEntity)
            .innerJoin(userSeminarEntity)
            .on(seminarEntity.id.eq(userSeminarEntity.seminar.id))
            .innerJoin(userEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(whereCondition)
            .orderBy(orderSpecifier)
            .fetch()
        
        
        
//        val seminars = queryFactory.select(seminarEntity)
//            .from(seminarEntity).fetch()
//
//        Pag
//        
////        PageImpl(seminars, PageRequest.of(page, size), seminars.size.toLong())
//        
//        
        val elements = createSeminarsQueryResponseFromQueryResult(result)
        var start = pageable.offset.toInt()
        val end = min(start + pageable.pageSize, elements.size)
        
        if (start > end) {
            start = end
        }
        return PageImpl(elements.subList(start, end), pageable, elements.size.toLong())
    }
    
    fun getParticipantCountMap() : Map<Long, Int> {
        val count = queryFactory
            .select(userSeminarEntity.seminar.id, userSeminarEntity.seminar.id.count())
            .from(userSeminarEntity)
            .where(userSeminarEntity.isParticipant.isTrue.and(userSeminarEntity.isActive.isTrue))
            .groupBy(userSeminarEntity.seminar.id)
            .fetch()

        return count.associate {
            it.get(userSeminarEntity.seminar.id)!! to it.get(userSeminarEntity.seminar.id.count())!!.toInt()
        }
    }
    fun createSeminarsQueryResponseFromQueryResult(result: List<Tuple>): MutableList<SeminarsQueryResponse> {
        val participantCountMap = getParticipantCountMap()
        val seminarsQueryResponseList: MutableList<SeminarsQueryResponse> = mutableListOf()

        var lastPushedSeminarId : Long = -1
        result.forEach {
            val seminar = it.get(seminarEntity)!!
            val instructorInfo = InstructorInfo(
                id = it.get(userEntity)!!.id,
                username = it.get(userEntity)!!.username,
                email = it.get(userEntity)!!.email,
                joinedAt = it.get(userSeminarEntity.createdAt)
            )
            if (seminar.id != lastPushedSeminarId) {
                seminarsQueryResponseList.add(
                    SeminarsQueryResponse(
                        id = seminar.id,
                        name = seminar.name,
                        instructors = mutableListOf(instructorInfo),
                        participantCount = participantCountMap.get(seminar.id) ?: 0
                    )
                )
            } else {
                seminarsQueryResponseList.last().instructors.add(instructorInfo)
            }
            lastPushedSeminarId = seminar.id
        }
        return seminarsQueryResponseList
    }
}