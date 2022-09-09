package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val db: MemoryDB,): OsRepository {
    val operatingSystems = db.getOperatingSystems()
    
    override fun findAll(): List<OperatingSystem> {
        return operatingSystems
    }

    override fun findById(id: Long): OperatingSystem {
        val os: OperatingSystem? = operatingSystems.find { it.id == id }
        return os ?: throw IllegalArgumentException("OS$id NOT FOUND!")
    }

    override fun findByName(osName: String): OperatingSystem {
        val os: OperatingSystem? = operatingSystems.find { it.osName == osName}
        return os ?: throw IllegalArgumentException("OS$osName NOT FOUND!")
    }
}