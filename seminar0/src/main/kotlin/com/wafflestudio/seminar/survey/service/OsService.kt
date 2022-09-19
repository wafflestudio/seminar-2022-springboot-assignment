package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsService(
    private val osDB: OsDB,
    private val exception: SeminarExceptionHandler,
): OsRepository {
    
    private val db = osDB.getOperatingSystems()
    
    override fun findAll(): List<OperatingSystem> {
        return db
    }
    override fun findById(id: Long): OperatingSystem {
        for(data in db){
            if (data.id == id)
                return data
        }
        throw exception.SeminarException()
    }

    override fun findByName(name: String): OperatingSystem {
        for(data in db){
            if (data.osName == name)
                return data
        }
        throw exception.SeminarException()
    }
}