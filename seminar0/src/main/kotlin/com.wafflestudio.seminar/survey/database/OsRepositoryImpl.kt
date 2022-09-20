package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(
    private val memoryDb: MemoryDB
): OsRepository {
    
    override fun findById(id: Long): OperatingSystem {
        return memoryDb.getOperatingSystems().find{operatingSystem: OperatingSystem -> operatingSystem.id == id} ?: throw SeminarExceptionHandler().OsIdException()
    }
    
    override fun findByName(osName: String): OperatingSystem {
        return memoryDb.getOperatingSystems().find{operatingSystem: OperatingSystem -> operatingSystem.osName == osName} ?: throw SeminarExceptionHandler().OsNameException()
    }
}