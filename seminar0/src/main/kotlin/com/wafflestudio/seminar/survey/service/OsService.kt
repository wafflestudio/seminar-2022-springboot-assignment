package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsService(
    private val osRepository: OsRepository
) {
    
    fun findById(id: Long): OperatingSystem {
        return osRepository.findById(id)
    }
    
    fun findByName(name: String): OperatingSystem {
        return osRepository.findAll()
            .find { os -> os.osName == name }
            ?: throw NoSuchElementException()
    }
    
}