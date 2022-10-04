package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.constants.ErrorCode
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        val operatingSystems = findAll()
        return operatingSystems.find { it.id == id }
            ?: throw SeminarExceptionHandler().SeminarException(ErrorCode.OS_ID_NOT_FOUND)
    }

    override fun findByName(name: String): OperatingSystem {
        val operatingSystems = findAll()
        return operatingSystems.find { it.osName == name }
            ?: throw SeminarExceptionHandler().SeminarException(ErrorCode.OS_NAME_NOT_FOUND)
    }

}