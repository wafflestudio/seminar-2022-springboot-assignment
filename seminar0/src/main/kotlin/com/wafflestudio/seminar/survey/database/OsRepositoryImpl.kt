package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(
    val memoryDB: MemoryDB
) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    @Throws(Exception::class)
    override fun findById(id: Long): OperatingSystem {
        return memoryDB.getOperatingSystems()
            .associateBy { it.id }[id]
            ?: throw NoSuchElementException()
    }
}