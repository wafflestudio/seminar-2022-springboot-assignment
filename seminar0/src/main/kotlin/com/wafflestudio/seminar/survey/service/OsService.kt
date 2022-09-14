package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepositoryImpl
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsService(
    private val osRepository: OsRepositoryImpl
) {
    
    fun findAllOs(): List<OperatingSystem> {
        return osRepository.findAll()
    }
    
    fun findOsById(id: Long): OperatingSystem {
        val os: OperatingSystem? = osRepository.findById(id)
        return os ?: throw IllegalArgumentException("No OS with id $id")
    }
}