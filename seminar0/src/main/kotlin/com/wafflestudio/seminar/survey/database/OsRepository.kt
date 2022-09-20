package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

interface OsRepository {
    fun findByName(name: String): OperatingSystem
    fun findById(id: Long): OperatingSystem
}

@Component
class DefaultOSRepository(
    db: MemoryDB
): OsRepository {
    private val osList = db.getOperatingSystems()
    override fun findByName(name: String): OperatingSystem {
        return osList.filter {
            it.osName.equals(name)
        }.first()
    }

    override fun findById(id: Long): OperatingSystem {
        return osList.filter {
            it.id == id
        }.first()
    }
}