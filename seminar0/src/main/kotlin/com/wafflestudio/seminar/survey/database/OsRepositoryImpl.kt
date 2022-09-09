package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB) : OsRepository {
    override fun findByName(name: String): OperatingSystem {
        return memoryDB.getOperatingSystems().filter { it.osName == name }[0]
    }

    override fun findById(id: Long): OperatingSystem {
        return memoryDB.getOperatingSystems().filter { it.id == id }[0]
    }
}