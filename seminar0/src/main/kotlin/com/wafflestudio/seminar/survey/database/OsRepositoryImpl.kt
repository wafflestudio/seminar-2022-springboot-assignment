package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Repository

@Repository
class OsRepositoryImpl(private val memoryDB: MemoryDB) : OsRepository{
    
    override fun findAll(): List<OperatingSystem> {
        return memoryDB!!.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        
        var list = memoryDB.getOperatingSystems()
        
        return list[id.toInt()-1]
    }
}