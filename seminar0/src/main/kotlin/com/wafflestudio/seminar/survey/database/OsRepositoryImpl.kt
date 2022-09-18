package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.exception.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.ErrorCode
import com.wafflestudio.seminar.survey.exception.SeminarException
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB) :
    OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        val findResults = memoryDB.getOperatingSystems().find{it.id == id}
        return findResults ?: throw SeminarException(ErrorCode.OS_NOT_FOUND)
    }

    override fun findByName(name: String): OperatingSystem {
        val findResults = memoryDB.getOperatingSystems().find{it.osName == name}
        return findResults ?: throw SeminarException(ErrorCode.OS_NOT_FOUND)
    }

}