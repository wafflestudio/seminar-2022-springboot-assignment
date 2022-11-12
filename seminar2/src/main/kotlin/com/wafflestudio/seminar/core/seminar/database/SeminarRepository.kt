package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.Tuple
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
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
    fun findByName(name: String) : SeminarEntity?
}

interface SeminarRepositoryCustom {
    fun findSeminarsByNameAndOrder(name: String?, earliest: Boolean, pageable: Pageable): Page<SeminarsQueryResponse>
}

class SeminarQueryVO @QueryProjection constructor(
    val seminarId: Long? = null,
    val name: String? = null,
    val seminarUserId: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val createdAt: LocalDateTime? = null
)

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
        val result = queryFactory
            .select(
                QSeminarQueryVO(
                    seminarEntity.id,
                    seminarEntity.name,
                    userEntity.id,
                    userEntity.username,
                    userEntity.email,
                    userSeminarEntity.createdAt
                )
            )
            .from(seminarEntity)
            .innerJoin(userSeminarEntity)
            .on(seminarEntity.id.eq(userSeminarEntity.seminar.id))
            .innerJoin(userEntity)
            .on(userSeminarEntity.user.id.eq(userEntity.id))
            .where(whereCondition)
            .orderBy(orderSpecifier)
            .fetch()
        
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

    fun createSeminarsQueryResponseFromQueryResult(result: List<SeminarQueryVO>): MutableList<SeminarsQueryResponse> {
        val participantCountMap = getParticipantCountMap()
        val seminarsQueryResponseList: MutableList<SeminarsQueryResponse> = mutableListOf()

        var lastPushedSeminarId : Long = -1
        result.forEach {
            val instructorInfo = InstructorInfo(
                id = it.seminarUserId!!,
                username = it.username!!,
                email = it.email!!,
                joinedAt = it.createdAt
            )
            if (it.seminarId != lastPushedSeminarId) {
                seminarsQueryResponseList.add(
                    SeminarsQueryResponse(
                        id = it.seminarId!!,
                        name = it.name!!,
                        instructors = mutableListOf(instructorInfo),
                        participantCount = participantCountMap.get(it.seminarId) ?: 0
                    )
                )
            } else {
                seminarsQueryResponseList.last().instructors.add(instructorInfo)
            }
            lastPushedSeminarId = it.seminarId
        }
        return seminarsQueryResponseList
    }
}