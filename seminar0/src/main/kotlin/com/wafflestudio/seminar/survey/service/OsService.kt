package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.os.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsService(
    val osRepository: OsRepository
) {
    fun findById(id: Long): OperatingSystem {
        return osRepository.findById(id)
    }

    fun findByName(name: String): OperatingSystem {
        return osRepository.findAll()
            .first { it.osName == name }
    }
}