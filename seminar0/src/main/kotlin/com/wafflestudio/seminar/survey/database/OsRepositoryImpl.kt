package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(
    private val memoryDb: MemoryDB,
): OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDb.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem? {
        return memoryDb.getOperatingSystems().find {it.id == id}
    }

    override fun findByName(name: String): OperatingSystem? {
        return memoryDb.getOperatingSystems().find {it.osName == name}
    }
}