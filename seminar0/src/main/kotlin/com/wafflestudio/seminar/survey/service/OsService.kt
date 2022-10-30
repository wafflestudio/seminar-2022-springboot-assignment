package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.OsRepositoryImpl
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsService(
    private val osRepositoryImpl: OsRepositoryImpl
) {
    
    fun findById(id: Long): OperatingSystem{
        return osRepositoryImpl.findById(id)
    }
    
    fun findByName(osName: String): OperatingSystem{
        return osRepositoryImpl.findByName(osName)
    }
}