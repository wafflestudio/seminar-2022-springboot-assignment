package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

interface OsRepository {
    fun findAll(): List<OperatingSystem>
    fun findById(id: Long): OperatingSystem
}

@Component
class DefaultOSRepository(
    db: MemoryDB
): OsRepository {
    override fun findAll(): List<OperatingSystem> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperatingSystem {
        TODO("Not yet implemented")
    }
}