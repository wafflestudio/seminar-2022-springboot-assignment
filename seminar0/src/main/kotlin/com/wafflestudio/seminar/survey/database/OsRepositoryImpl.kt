package com.wafflestudio.seminar.survey.database

import org.springframework.stereotype.Component

import com.wafflestudio.seminar.survey.domain.OperatingSystem

@Component
class OsRepositoryImpl(
        private val memoryDB: MemoryDB
) : OsRepository {

    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem? {
        return memoryDB.getOperatingSystems().find{it.id == id}
    }

    override fun findByName(name: String): OperatingSystem? {
        return memoryDB.getOperatingSystems().find{it.osName == name}
    }
}