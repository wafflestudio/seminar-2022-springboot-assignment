package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Repository

@Repository
class OsRepositoryImpl(
    private val db: MemoryDB
): OsRepository {
    
    @Override
    override fun findAll(): List<OperatingSystem> {
        return db.getOperatingSystems()
    }
    
    @Override
    override fun findById(id: Long): OperatingSystem? {
        return db.getOperatingSystems().associateBy { it.id }[id]
    }
}