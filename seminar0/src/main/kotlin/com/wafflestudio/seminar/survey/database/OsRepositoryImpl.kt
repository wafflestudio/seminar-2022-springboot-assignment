package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val db: MemoryDB,): OsRepository {
    val operatingSystems = db.getOperatingSystems()
    
    override fun findAll(): List<OperatingSystem> {
        return operatingSystems
    }

    override fun findById(id: Long): OperatingSystem? {
        return operatingSystems.find { it.id == id }
    }

    override fun findByName(osName: String): OperatingSystem? {
        return operatingSystems.find { it.osName == osName}
    }
}