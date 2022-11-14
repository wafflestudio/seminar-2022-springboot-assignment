package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.join.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.profile.database.*
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.database.QUserEntity.userEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface SeminarRepository :
    JpaRepository<SeminarEntity, Long>,
    SeminarRepositoryCustom

interface SeminarRepositoryCustom {
    fun queryWithNameByOrder(name: String = "", order: String = ""): MutableList<SeminarEntity>
    fun querySeminarDetail(seminarId: Long):
        Pair<SeminarEntity, List<UserSeminarEntity>>?
}

@Repository
class SeminarRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : SeminarRepositoryCustom {

    override fun queryWithNameByOrder(name: String, order: String): MutableList<SeminarEntity> {
        val queryResult = jpaQueryFactory
            .select(seminarEntity)
            .from(seminarEntity, userSeminarEntity, userEntity)
            .where(seminarEntity.name.contains(name))
            .orderBy(
                if (order == ASC) {
                    seminarEntity.createdAt.asc()
                } else {
                    seminarEntity.createdAt.desc()
                }
            )
            .leftJoin(seminarEntity.users, userSeminarEntity)
            .fetchJoin()
            .leftJoin(userSeminarEntity.user, userEntity)
            .fetchJoin()
            .fetch()
        return queryResult
    }

    override fun querySeminarDetail(seminarId: Long):
        Pair<SeminarEntity, List<UserSeminarEntity>>? {
            val tupleList: MutableList<Tuple> = jpaQueryFactory
                .select(
                    seminarEntity,
                    userSeminarEntity,
                )
                .from(
                    seminarEntity,
                    userSeminarEntity,
                    userEntity,
                )
                // Select seminar with id
                .where(seminarEntity.id.eq(seminarId))
                .leftJoin(seminarEntity.users)
                .fetchJoin()
                .leftJoin(userSeminarEntity.user)
                .fetchJoin()
                .fetch()

            if (tupleList.isEmpty()) { return null } else {
                val seminar = tupleList[0].get(seminarEntity)!!
                val userSeminarList = tupleList.map { it.get(userSeminarEntity)!! }
                return Pair(seminar, userSeminarList)
            }
        }
}

fun changeTimeStringToMinutes(timeString: String): Int {
    val matchResult = TIME_FORMAT.toRegex().matchEntire(timeString)
        ?: throw Seminar400("Wrong time format given")

    val hour = matchResult.groupValues[1].toInt()
    val minute = matchResult.groupValues[2].toInt()

    if (!((0 <= hour && hour < 24) && (0 <= minute && minute < 60))) {
        throw Seminar400("Wrong time format given")
    }

    return (hour * 60) + minute
}

fun changeTotalMinutesToTimeString(totalMinutes: Int): String {
    return "${totalMinutes / 60}:${totalMinutes % 60}"
}