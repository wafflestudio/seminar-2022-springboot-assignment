package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB): OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        val operatingSystems = findAll()
        // TODO: Handle the case when not found (returning null)
        return operatingSystems.find { it.id == id }!!
    }
    
    override fun findByName(name: String): OperatingSystem {
        val operatingSystems = findAll()
        // TODO: Handle the case when not found (returning null)
        return operatingSystems.find { it.osName == name }!!
    }

}