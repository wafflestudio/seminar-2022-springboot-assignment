package com.wafflestudio.seminar.survey.database.os

import com.wafflestudio.seminar.survey.database.MemoryDB
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.ErrorCode
import com.wafflestudio.seminar.survey.exception.SurveyNotFoundException
import org.springframework.stereotype.Repository

@Repository
class OsRepositoryImpl(
    val memoryDB: MemoryDB,
): OsRepository {
    
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        return memoryDB.getOperatingSystems()
            .firstOrNull { it.id == id }
            ?: throw SurveyNotFoundException(ErrorCode.OS_NOT_FOUND)
    }
}