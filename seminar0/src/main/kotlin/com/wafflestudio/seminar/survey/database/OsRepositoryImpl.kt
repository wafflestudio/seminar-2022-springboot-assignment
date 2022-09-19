package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(
    private val memoryDB: MemoryDB
) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        val osList = memoryDB.getOperatingSystems()
        return osList.find { os -> os.id == id } ?: throw SeminarExceptionHandler().NoSuchIdException()
    }

    override fun findByName(name: String): OperatingSystem {
        val osList = memoryDB.getOperatingSystems()
        return osList.find { os -> os.osName == name} ?: throw SeminarExceptionHandler().NoMatchingNameException()
    }
}