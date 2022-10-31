package com.wafflestudio.seminar.core.seminar.database

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.ASC
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.TIME_FORMAT
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity.seminarEntity
import org.springframework.stereotype.Component

interface SeminarRepository
    : JpaRepository<SeminarEntity, Long>,
    SeminarRepositoryCustom {
}

interface SeminarRepositoryCustom {
    fun queryWithNameByOrder(name: String = "", order: String = ""): MutableList<SeminarEntity>
}

@Repository
class SeminarRepositoryCustomImpl (
        private val jpaQueryFactory: JPAQueryFactory,
): SeminarRepositoryCustom {
    override fun queryWithNameByOrder(name: String, order: String): MutableList<SeminarEntity> {
        return jpaQueryFactory
                .selectFrom(seminarEntity)
                .where(seminarEntity.name.contains(name))
                .orderBy(
                        if (order == ASC) {
                            seminarEntity.createdAt.asc()
                        } else {
                            seminarEntity.createdAt.desc()
                        }
                )
                .fetch()
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