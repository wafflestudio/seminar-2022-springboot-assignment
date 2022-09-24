package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarException
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

interface OsRepository {
    fun findById(id: Long): OperatingSystem
    fun findByName(name: String): OperatingSystem
}

@Component
class OsRepositoryImpl(
    db: MemoryDB
) : OsRepository {
    private val osList = db.getOperatingSystems()

    override fun findByName(name: String): OperatingSystem {
        return osList.find { it.osName == name } ?: throw SeminarException("${name}에 해당하는 운영체제가 없어요.")
    }

    override fun findById(id: Long): OperatingSystem {
        return osList.find { it.id == id } ?: throw SeminarException("ID에 해당하는 운영체제가 없어요.")
    }
}