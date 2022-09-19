package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.DataNotFoundException
import com.wafflestudio.seminar.survey.exception.ErrorCode
import org.springframework.stereotype.Repository

@Repository
class OsRepositoryImpl(val memoryDB: MemoryDB):OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        return memoryDB.getOperatingSystems().first{it.id == id} ?: throw DataNotFoundException(ErrorCode.DATA_NOT_FOUND)
    }

    override fun findByName(os: String): List<OperatingSystem> {
        return memoryDB.getOperatingSystems().filter{it.osName == os} ?: throw DataNotFoundException(ErrorCode.DATA_NOT_FOUND)
    }
    
}