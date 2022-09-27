package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(
    private val memoryDB: MemoryDB
) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperatingSystem? {
        return memoryDB.getOperatingSystems().find { it.id == id }
    }

    override fun findByName(name: String): OperatingSystem? {
        return memoryDB.getOperatingSystems().find { it.osName == name }
    }

}