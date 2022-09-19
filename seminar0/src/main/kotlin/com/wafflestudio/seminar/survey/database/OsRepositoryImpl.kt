package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB): OsRepository {
    override fun findByName(name: String): OperatingSystem?{
        return memoryDB.getOperatingSystems().find{operatingSystem -> operatingSystem.osName == name }
    }
    
    override fun findById(id: Long): OperatingSystem?{
        return memoryDB.getOperatingSystems().find{operatingSystem -> operatingSystem.id == id}
    }

}