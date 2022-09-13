package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component


@Component
class OsRepositoryImpl(val db: MemoryDB) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return db.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem? {
        return db.getOperatingSystems().find { it.id == id }
    }

    override fun findByName(osName: String): OperatingSystem? {
        return db.getOperatingSystems().find { it.osName == osName }
    }

}