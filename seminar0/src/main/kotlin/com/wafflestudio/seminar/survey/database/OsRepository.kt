package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarException
import com.wafflestudio.seminar.survey.api.SeminarExceptionType
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

interface OsRepository {
    fun findByName(name: String): OperatingSystem
    fun findById(id: Long): OperatingSystem
}

@Component
class DefaultOSRepository(
    db: MemoryDB
): OsRepository {
    private val osList = db.getOperatingSystems()
    override fun findByName(name: String): OperatingSystem {
        if (name.equals("")) {
            return throw SeminarException(SeminarExceptionType.InputNeedsOSName)
        }
        return osList.filter {
            it.osName.equals(name)
        }.firstOrNull() ?: throw SeminarException(SeminarExceptionType.NotExistOSForName)
    }

    override fun findById(id: Long): OperatingSystem {
        return osList.filter {
            it.id == id
        }.firstOrNull() ?: throw SeminarException(SeminarExceptionType.NotExistOSForId)
    }
}